package com.ensa.notificationservice.services;


import com.ensa.notificationservice.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SmsService smsService;

    @RabbitListener(queues = "${notification.queues.queue1}")
    public void msgReceiveMessage(NotificationRequest request) {
        String verificationMsg = null;

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
        if (verificationMsg != null) {
            smsService.sendSMS(request.getPhone(), verificationMsg);
            System.out.println(request);
        }
    }

    @RabbitListener(queues = "${notification.queues.queue2}")
    public void otpReceiveMessage(NotificationRequest request) {

        String verificationMsg = generateSmsForOtp(request);
        smsService.sendSMS(request.getPhone(), verificationMsg);
        System.out.println(request);
    }

    private String mapTransferStateToOperation(String transferState) {
        switch (transferState) {
            case "BLOCKED":
                return "Bloqué";
            case "UNBLOCKED":
                return "Débloqué";
            case "REVERSED":
                return "Extourné";
            case "RETURNED":
                return "Restitué";
            default:
                throw new IllegalArgumentException("Unknown transfer state: " + transferState);
        }
    }

    private String generateSmsForOtp( NotificationRequest request){
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
                " a été "+ operation +". Pour plus d'informations, veuillez contacter notre service client.";
    }
}
