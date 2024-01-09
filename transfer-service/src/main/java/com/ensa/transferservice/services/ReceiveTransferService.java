package com.ensa.transferservice.services;

import com.ensa.transferservice.dto.requests.ServeTransferRequest;
import com.ensa.transferservice.dto.requests.ValidateTransferRequest;
import com.ensa.transferservice.dto.responses.AccountResponse;
import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.enums.MsgType;
import com.ensa.transferservice.enums.TransferState;
import com.ensa.transferservice.enums.TransferType;
import com.ensa.transferservice.exceptions.InvalidTransferException;
import com.ensa.transferservice.exceptions.ResourceNotFoundException;
import com.ensa.transferservice.feignClients.AccountFeignClient;
import com.ensa.transferservice.feignClients.FraudFeignClient;
import com.ensa.transferservice.repositories.TransferRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReceiveTransferService {

    private final TransferRepo transferRepo;
    private final TransferService transferService;
    private final AmqpTemplate rabbitTemplate;
    private final FraudFeignClient fraudFeignClient;
    private final AccountFeignClient accountFeignClient;
    private static final Logger logger = LoggerFactory.getLogger(ReceiveTransferService.class);


    @Value("${notification.exchange}")
    private String exchangeName;

    @Value("${notification.routingKey1}")
    private String msgRoutingKey;

    @Value("${notification.routingKey2}")
    private String otpRoutingKey;

    private AccountResponse getAccount(TransferType transferType, String clientId, String agentId) {
        String id = switch (transferType) {
            case BY_WALLET -> clientId;
            case IN_CASH -> agentId;
            default -> throw new IllegalArgumentException("Unsupported TransferType: " + transferType);
        };
        return accountFeignClient.getAccountByOwnerId(id).getBody();
    }

    public Transfer checkTransferToServe(String reference) {
        logger.info("Checking transfer to serve with reference: {}", reference);
        Transfer transfer = transferRepo.findByReference(reference).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );

        validateTransferIntegrity(transfer);

        if ((transfer.getTransferState() == TransferState.TO_SERVE && transfer.getExpirationDate().isAfter(LocalDate.now()))
                || (transfer.getTransferState() == TransferState.UNBLOCKED /* && jour J de debloquage */)) {
            System.out.println("transfer " + transfer);
            return transfer;
        }
        throw new InvalidTransferException("Error getting Transfer");
    }

    public Transfer serveTransferCash(ServeTransferRequest serveRequest) {
        logger.info("Serving transfer in cash for reference: {}", serveRequest.getReference());
        Transfer transfer = transferRepo.findByReference(serveRequest.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );


//        Serve transfer in cash and update balance
        if (transfer.getTransferType().equals(TransferType.IN_CASH)) {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String receiverAgentId = (String) jwt.getClaims().get("userId");

            transfer.setTransferState(TransferState.SERVED);
            if (transfer.getTransferNotification()) {
                //msg to client with info
                transferService.sendNotification(serveRequest.getPhone(), transfer.getReference(), transfer.getTransferAmount(), transfer.getTransferState(), MsgType.TO_CLIENT, null);
                logger.info("Notification sent to client for transfer reference: {}", transfer.getReference());
            }
            // //update agentAccount balance
            accountFeignClient.updateAccountBalanceMinus(receiverAgentId, transfer.getTransferAmount());
        }
        Transfer savedTransfer = transferRepo.save(transfer);
        logger.info("Transfer served in cash and saved. Transfer reference: {}", savedTransfer.getReference());
        return savedTransfer;
    }

    public Transfer serveTransferToWallet(ServeTransferRequest serveRequest) {
        logger.info("Attempting to serve transfer to wallet for reference: {}", serveRequest.getReference());
        Transfer transfer = transferRepo.findByReference(serveRequest.getReference()).orElseThrow(
                () -> new NoSuchElementException("Transfer not found")
        );

        if (transfer.getTransferType().equals(TransferType.BY_WALLET)) {
            logger.info("Processing transfer to wallet for clientId: {}", serveRequest.getClientId());

            AccountResponse clientAccount = accountFeignClient.getAccountByOwnerId(serveRequest.getClientId()).getBody();

            String otp = transferService.generateOtpForSms();
            transfer.setOtpCode(otp);

//            send otp notification to client
            transferService.sendNotification(serveRequest.getPhone(), transfer.getReference(), transfer.getTransferAmount(), transfer.getTransferState(), MsgType.OTP, otp);
            logger.info("OTP notification sent for transfer reference: {}", transfer.getReference());

            return transferRepo.save(transfer);
        }
        return null;
    }

    public Transfer validateTransferToWallet(ValidateTransferRequest transferRequest) {
        logger.info("Validating transfer to wallet for reference: {}", transferRequest.getReference());

        Transfer transfer = transferRepo.findByReference(transferRequest.getReference()).orElseThrow(
                () -> new NoSuchElementException("Transfer not found")
        );
        if (transfer.getTransferType().equals(TransferType.BY_WALLET)) {
            if (!transfer.getOtpCode().equals(transferRequest.getOtp()))
                throw new IllegalArgumentException("Incorrect OTP code !");

            transfer.setTransferState(TransferState.SERVED);
            logger.info("Transfer state set to SERVED for reference: {}", transfer.getReference());

            if (transfer.getTransferNotification()) {
                //msg to recipient with info
                transferService.sendNotification(transferRequest.getPhone(), transfer.getReference(), transfer.getTransferAmount(), transfer.getTransferState(), MsgType.TO_RECIPIENT, null);
                logger.info("Notification sent to recipient for transfer reference: {}", transfer.getReference());
            }
            accountFeignClient.updateAccountBalancePlus(transferRequest.getRecipientId(), transfer.getTransferAmount());
            logger.info("Account balance updated for recipientId: {}", transferRequest.getRecipientId());
        }

        return transferRepo.save(transfer);
    }


//    public Transfer serveTransferGAB(String reference, String pinCode){
//        Transfer transfer = transferRepo.findByReference(reference).orElseThrow(
//                () -> new ResourceNotFoundException("Transfer not found")
//        );
//
//        if(!pinCode.equals(transfer.getPinCode()))
//            throw new IllegalStateException("Pin incorrect");
//
//        validateTransferIntegrity(transfer);
//
//        //check if gab is empty
//
//        if( (transfer.getTransferState() == TransferState.TO_SERVE && transfer.getExpirationDate().isAfter(LocalDate.now()))
//                || (transfer.getTransferState() == TransferState.UNBLOCKED /* && jour J de debloquage */  ) )
//        {
//            transfer.setTransferState(TransferState.SERVED);
//            if(transfer.getTransferNotification()) {
//msg to client with info
//                NotificationRequest notificationRequest = NotificationRequest.builder()
//                        .phone(serveRequest.getPhone())
//                        .transferState(transfer.getTransferState().toString())
//                        .transferReference(transfer.getReference())
//                        .transferAmount(transfer.getTransferAmount())
//                        .msgType(MsgType.TO_CLIENT.toString())
//                        .build();
//                rabbitTemplate.convertAndSend(exchangeName, msgRoutingKey, notificationRequest);
//            }
//            return transferRepo.save(transfer);
//
//        }
//        return null;
//    }

    private void validateTransferIntegrity(Transfer transfer) {
        logger.info("Validating transfer integrity for reference: {}", transfer.getReference());

        if (transfer.getTransferState() == TransferState.SERVED){
            logger.warn("Transfer with reference {} is already served", transfer.getReference());
            throw new InvalidTransferException("Transfer paid");
        }


        if (transfer.getTransferState() == TransferState.BLOCKED){
            logger.warn("Transfer with reference {} is blocked", transfer.getReference());
            throw new InvalidTransferException("Transfer blocked");
        }


        if (transfer.getTransferState() == TransferState.REVERSED){
            logger.warn("Transfer with reference {} is reversed", transfer.getReference());
            throw new InvalidTransferException("Transfer reversed");
        }


        if (transfer.getTransferState() == TransferState.RETURNED){
            logger.warn("Transfer with reference {} is returned", transfer.getReference());
            throw new InvalidTransferException("Transfer returned");
        }


        if (transfer.getTransferState() == TransferState.ESCHEAT){
            logger.warn("Transfer with reference {} is escheated", transfer.getReference());
            throw new InvalidTransferException("Transfer deshéré");
        }


        if (transfer.getExpirationDate().isBefore(LocalDate.now())){
            logger.warn("Transfer with reference {} is expired", transfer.getReference());
            throw new InvalidTransferException("Transfer expired");
        }

        logger.info("Transfer with reference {} passed all integrity checks", transfer.getReference());
    }

}