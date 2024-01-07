package com.ensa.notificationservice.services;


import com.ensa.notificationservice.dto.NotificationRequest;
import com.ensa.notificationservice.entities.Notification;
import com.ensa.notificationservice.repositories.NotificationRepo;
import com.vonage.client.sms.SmsSubmissionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

    private final SmsService smsService;
    private final NotificationRepo notificationRepo;

    @RabbitListener(queues = "${notification.queues.queue1}")
    public void msgReceiveMessage(NotificationRequest request) {
        String verificationMsg = null;
        log.info("ON TOP {}", request);

        if ("TO_RECIPIENT".equals(request.getMsgType())) {
            verificationMsg = generateRecipientNotification(request);

        } else if ("TO_CLIENT".equals(request.getMsgType())) {
            if ("SERVED".equals(request.getTransferState())) {
                verificationMsg = generateClientNotification(request);

            } else {
                String operation = mapTransferStateToOperation(request.getTransferState());
                verificationMsg = generateOperationsNotification(request, operation);
            }
        }
        log.info("outside: {}", verificationMsg);
        if (verificationMsg != null) {
            log.info("inside {}", verificationMsg);
            generateNotification(request, verificationMsg);
        }
    }

    @RabbitListener(queues = "${notification.queues.queue2}")
    public void otpReceiveMessage(NotificationRequest request) {

        String verificationMsg = generateSmsForOtp(request);
        generateNotification(request, verificationMsg);
    }

    private void generateNotification(NotificationRequest request, String verificationMsg) {
        SmsSubmissionResponse response = smsService.sendSMS(request.getPhone(), verificationMsg);
        Notification notification = Notification.builder()
                .recipientPhone(request.getPhone())
                .textBody(verificationMsg)
                .messageId(response.getMessages().get(0).getId())
                .responseStatus(response.getMessages().get(0).getStatus().toString())
                .messagePrice(response.getMessages().get(0).getMessagePrice())
                .messagePrice(BigDecimal.valueOf(0.15))
                .errorMessage(response.getMessages().get(0).getErrorText())
                .build();
        notificationRepo.save(notification);
    }

    private String mapTransferStateToOperation(String transferState) {
        return switch (transferState) {
            case "BLOCKED" -> "Bloqué";
            case "UNBLOCKED" -> "Débloqué";
            case "REVERSED" -> "Extourné";
            case "RETURNED" -> "Restitué";
            default -> throw new IllegalArgumentException("Unknown transfer state: " + transferState);
        };
    }

    private String generateSmsForOtp(NotificationRequest request) {
        return "Votre code de confirmation pour le Transfer de la référence " +
                request.getTransferReference() +
                " d'un montant de " +
                request.getTransferAmount() +
                " MAD est : " +
                request.getCode() +
                ". Veuillez saisir ce code pour compléter la transaction. ";
    }

    private String generateRecipientNotification(NotificationRequest request) {
        return "Vous avez reçu un transfert d'un montant de " +
                request.getTransferAmount() +
                " MAD avec la référence " +
                request.getTransferReference() +
                ". Pour retirer l'argent au GAB, utilisez le code de confirmation : " +
                request.getCode() +
                ". Ne partagez pas ce code avec d'autres personnes.";
    }

    private String generateClientNotification(NotificationRequest request) {
        return "Le transfert d'un montant de " +
                request.getTransferAmount() +
                " MAD avec la référence " +
                request.getTransferReference() +
                " a été retiré avec succès. Merci de votre confiance.";
    }

    private String generateOperationsNotification(NotificationRequest request, String operation) {
        return "Le transfert d'un montant de " +
                request.getTransferAmount() +
                " MAD avec la référence " +
                request.getTransferReference() +
                " a été " + operation + ". Pour plus d'informations, veuillez contacter notre service client.";
    }
}
