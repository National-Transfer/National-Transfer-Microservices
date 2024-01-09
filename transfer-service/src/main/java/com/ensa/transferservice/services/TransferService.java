package com.ensa.transferservice.services;

import com.ensa.transferservice.dto.requests.NotificationRequest;
import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.enums.MsgType;
import com.ensa.transferservice.enums.TransferState;
import com.ensa.transferservice.exceptions.ResourceNotFoundException;
import com.ensa.transferservice.repositories.TransferRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private final TransferRepo transferRepo;
    private final RabbitTemplate rabbitTemplate;

    @Value("${notification.exchange}")
    private String exchangeName;

    @Value("${notification.routingKey1}")
    private String msgRoutingKey;

    @Value("${notification.routingKey2}")
    private String otpRoutingKey;


    public Page<Transfer> getAllTransfers(int page, int size) {
        logger.info("Fetching all transfers with page: {} and size: {}", page, size);
        Pageable pageRequest = PageRequest.of(page, size);
        return transferRepo.findAll(pageRequest);
    }

    public Transfer getTransferByReference(String reference) {
        logger.info("Getting transfer with reference: {}", reference);
        return transferRepo.findByReference(reference).orElseThrow(
                () -> {
                    logger.error("Transfer with reference {} not found", reference);
                    return new ResourceNotFoundException("Transfer not found");
                }

        );
    }

    public String generatePinCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder pinCodeBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int randomDigit = secureRandom.nextInt(10);
            pinCodeBuilder.append(randomDigit);
        }
        return pinCodeBuilder.toString();
    }

    public List<Transfer> getAllTransfersForBatch() {

        logger.info("Fetching all transfers for batch processing");

        List<Transfer> transfers = new ArrayList<>();

        System.out.println(transferRepo.findByTransferState(TransferState.TO_VALIDATE));

        transfers.addAll(transferRepo.findByTransferState(TransferState.TO_VALIDATE));

        System.out.println(transfers);

        transfers.addAll(transferRepo.findByTransferState(TransferState.TO_SERVE));

        System.out.println(transfers);

        logger.debug("Found {} transfers for batch processing", transfers.size());
        return transfers;
    }

    public List<Transfer> getAllTransfersForClient(String clientId) {
        logger.info("Fetching all transfers for client with ID: {}", clientId);
        return transferRepo.findByClientId(clientId);
    }

    public String generateOtpForSms() {

        return String.valueOf(new Random().nextInt(90000) + 10000);
    }

    public List<Transfer> saveAllTransfers(List<Transfer> transfers) {
        logger.info("Saving a batch of {} transfers", transfers.size());
        return transferRepo.saveAll(transfers);
    }

    public void deleteAllTransfers(List<Transfer> transfers) {
        logger.info("Deleting a batch of {} transfers", transfers.size());
        transferRepo.deleteAll(transfers);
    }

    public void sendNotification(String phone, String transferReference, BigDecimal transferAmount, TransferState transferState, MsgType msgType, String code) {
        logger.info("Sending notification to phone: {}, reference: {}, amount: {}, state: {}, type: {}", phone, transferReference, transferAmount, transferState, msgType);
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .phone(phone)
                .transferReference(transferReference)
                .transferAmount(transferAmount)
                .transferState(transferState.toString())
                .msgType(msgType.toString())
                .build();
        if (code != null){
            notificationRequest.setCode(code);
            logger.debug("Notification code: {}", code);
        }
        if (msgType.equals(MsgType.OTP)) {
            rabbitTemplate.convertAndSend(exchangeName, otpRoutingKey, notificationRequest);
            return;
        }
        rabbitTemplate.convertAndSend(exchangeName, msgRoutingKey, notificationRequest);
    }
}
