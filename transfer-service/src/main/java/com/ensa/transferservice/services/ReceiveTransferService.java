package com.ensa.transferservice.services;

import com.ensa.transferservice.dto.requests.ServeTransferRequest;
import com.ensa.transferservice.dto.responses.AccountResponse;
import com.ensa.transferservice.dto.requests.NotificationRequest;
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
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReceiveTransferService {

    private final TransferRepo transferRepo;
    private final TransferService transferService;
    private final AmqpTemplate rabbitTemplate;
    private final FraudFeignClient fraudFeignClient;
    private final AccountFeignClient accountFeignClient;


    @Value("${notification.exchange}")
    private String exchangeName;

    private AccountResponse getAccount (TransferType transferType, String clientId, String agentId) {
        String id = switch (transferType) {
            case BY_WALLET -> clientId;
            case IN_CASH -> agentId;
            default -> throw new IllegalArgumentException("Unsupported TransferType: " + transferType);
        };
        return accountFeignClient.getAccountByOwnerId(id).getBody();
    }

    public Transfer checkTransferToServe(String reference){
        Transfer transfer = transferRepo.findByReference(reference).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );

        validateTransferIntegrity(transfer);

        if( (transfer.getTransferState() == TransferState.TO_SERVE && transfer.getExpirationDate().isAfter(LocalDate.now()))
                || (transfer.getTransferState() == TransferState.UNBLOCKED /* && jour J de debloquage */  ) ) {
            System.out.println("transfer " + transfer);
            return transfer;
        }
        throw new InvalidTransferException("Error getting Transfer");
    }


//    public Transfer serveTransferToWallet(ServeTransferRequest serveRequest) {
//        Transfer transfer = transferRepo.findByReference(serveRequest.getReference()).orElseThrow(
//                () -> new NoSuchElementException("Transfer not found")
//        );
//
////            AccountResponse clientAccount = accountFeignClient.getAccountByOwnerId(transfer.getRecipientId()).getBody();
//            if(transfer.getTransferType().equals(TransferType.BY_WALLET)) {
//            //get the wallet
////            AccountResponse clientAccount = accountFeignClient.getAccountByOwnerId().getBody();
//
////            if (clientAccount == null) {
////                //create an accountWallet
////                clientAccount = accountFeignClient.createAccount(recipient).getBody();
////            }
////            if (clientAccount == null)
////                throw new IllegalStateException("Error creating account !");
//
//            String otp =  transferService.generateOtpForSms();
//            transfer.setOtpCode(otp);
//
            //send otp notification to client
//            NotificationRequest request = NotificationRequest.builder()
//                    .phone(client.getPhone())
//                    .transferState(transfer.getTransferState())
//                    .transferReference(transfer.getReference())
//                    .transferAmount(transfer.getTransferAmount())
//                    .code(otp)
//                    .msgType(MsgType.OTP)
//                    .build();
//
//            //send otp notification to client
//            rabbitTemplate.convertAndSend(exchangeName, "otpRoutingKey", request);
//
//            return transferRepo.save(transfer);
//        }
//        return null;
//    }
//    public Transfer validateTransfer(ValidateTransferRequest transferRequest){
//
//
//        return null;
//    }

    public Transfer serveTransferCash( ServeTransferRequest serveRequest ){
        Transfer transfer = transferRepo.findByReference(serveRequest.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );

        transfer.setTransferState(TransferState.SERVED);
        if(transfer.getTransferNotification()) {
            //msg to client with info
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .phone(serveRequest.getPhone())
                    .transferState(transfer.getTransferState().toString())
                    .transferReference(transfer.getReference())
                    .transferAmount(transfer.getTransferAmount())
                    .msgType(MsgType.TO_CLIENT.toString())
                    .build();

            rabbitTemplate.convertAndSend(exchangeName, "MsgRoutingKey", notificationRequest);
        }
        // //update agentAccount balance
        accountFeignClient.updateAccountBalanceMinus(transfer.getAgentId(), transfer.getTransferAmount());
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
//                rabbitTemplate.convertAndSend(exchangeName, "MsgRoutingKey", notificationRequest);
//            }
//            return transferRepo.save(transfer);
//
//        }
//        return null;
//    }

    private void validateTransferIntegrity(Transfer transfer) {
        if(transfer.getTransferState() == TransferState.SERVED)
            throw new InvalidTransferException("Transfer paid");

        if(transfer.getTransferState() == TransferState.BLOCKED)
            throw new InvalidTransferException("Transfer blocked");

        if(transfer.getTransferState() == TransferState.REVERSED)
            throw new InvalidTransferException("Transfer reversed");

        if(transfer.getTransferState() == TransferState.RETURNED)
            throw new InvalidTransferException("Transfer returned");

        if(transfer.getTransferState() == TransferState.ESCHEAT)
            throw new InvalidTransferException("Transfer desiré");

        if(transfer.getExpirationDate().isBefore(LocalDate.now()))
            throw new InvalidTransferException("Transfer expired");
    }

}
