package com.ensa.transferservice.services;

import com.ensa.transferservice.dto.requests.NotificationRequest;
import com.ensa.transferservice.dto.requests.OperationRequest;
import com.ensa.transferservice.dto.requests.TransferRestitutionRequest;
import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.enums.MsgType;
import com.ensa.transferservice.enums.TransferState;
import com.ensa.transferservice.exceptions.InvalidTransferException;
import com.ensa.transferservice.exceptions.ResourceNotFoundException;
import com.ensa.transferservice.feignClients.AccountFeignClient;
import com.ensa.transferservice.repositories.TransferRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransferOperationService {
    private final TransferRepo transferRepo;
    private final AmqpTemplate rabbitTemplate;
    private final AccountFeignClient accountFeignClient;
    private final TransferService transferService;

    @Value("${notification.exchange}")
    private String exchangeName;

    public Transfer extournerTransfer(OperationRequest operationRequest){
        Transfer transfer = transferRepo.findByReference(operationRequest.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );

        if(transfer.getTransferState() == TransferState.SERVED)
            throw new InvalidTransferException("Transfer paid");

        if(transfer.getTransferState() == TransferState.BLOCKED)
            throw new InvalidTransferException("Transfer blocked");

        if(transfer.getTransferState() == TransferState.TO_SERVE && transfer.getTransferDate().toLocalDate().isEqual(LocalDate.now()) ){

            transfer.setTransferState(TransferState.REVERSED);
            accountFeignClient.updateAccountBalancePlus(transfer.getAgentId(), transfer.getTransferAmount());

            return validateOperation( transfer, operationRequest.getClientPhone());
        }
        throw new InvalidTransferException("Error reversing transfer");
    }

    public Transfer restituerTransferByAgent(OperationRequest operationRequest) {
        Transfer transfer = transferRepo.findByReference(operationRequest.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );

        if(transfer.getTransferState() == TransferState.SERVED)
            throw new InvalidTransferException("Transfer paid");

        if(transfer.getTransferState() == TransferState.BLOCKED)
            throw new InvalidTransferException("Transfer blocked");

        if(transfer.getTransferState() == TransferState.TO_SERVE /*&& » et émise par le même agent.*/
                || transfer.getTransferState() == TransferState.UNBLOCKED  /*&&  e bénéficiaire se présente le jour J */){

            transfer.setTransferState(TransferState.RETURNED);
            accountFeignClient.updateAccountBalancePlus(transfer.getAgentId(), transfer.getTransferAmount());

            return validateOperation(transfer, operationRequest.getClientPhone());
        }
        throw new InvalidTransferException("Error returning transfer");
    }

    public Transfer restituerTransferByClient(OperationRequest operationRequest) {
        Transfer transfer = transferRepo.findByReference(operationRequest.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );

        if(transfer.getTransferState() == TransferState.SERVED)
            throw new InvalidTransferException("Transfer paid");

        if(transfer.getTransferState() == TransferState.BLOCKED)
            throw new InvalidTransferException("Transfer blocked");

        if(transfer.getTransferState() == TransferState.TO_SERVE /*&& » et émise par le client.*/){

            //otp
            String otp =  transferService.generateOtpForSms();
            transfer.setOtpCode(otp);

            NotificationRequest request = NotificationRequest.builder()
                    .phone(operationRequest.getClientPhone())
                    .code(otp)
                    .transferAmount(transfer.getTransferAmount())
                    .transferState(transfer.getTransferState().toString())
                    .transferReference(transfer.getReference())
                    .msgType(MsgType.OTP.toString())
                    .build();

            //send otp notification to client
            rabbitTemplate.convertAndSend(exchangeName, "otpRoutingKey", request);

            return transferRepo.save(transfer);
        }
        throw new InvalidTransferException("Error returning transfer");
    }

    public Transfer finalizeRestitutionByClient(TransferRestitutionRequest restitutionRequest){

        Transfer transfer = transferRepo.findByReference(restitutionRequest.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );

        if(!transfer.getOtpCode().equals(restitutionRequest.getOtp()))
            throw new IllegalArgumentException("Incorrect OTP code !");

        transfer.setTransferState(TransferState.RETURNED);

        accountFeignClient.updateAccountBalancePlus(transfer.getClientId(), transfer.getTransferAmount());

        return validateOperation(transfer, restitutionRequest.getClientPhone());
    }

    public Transfer blockTransfer(OperationRequest operationRequest){
        Transfer transfer = transferRepo.findByReference(operationRequest.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );
        if(transfer.getTransferState() == TransferState.SERVED)
            throw new InvalidTransferException("Transfer paid");

        transfer.setTransferState(TransferState.BLOCKED);
        validateOperation(transfer, operationRequest.getClientPhone());
        return transferRepo.save(transfer);
    }

    public Transfer unblockTransfer(OperationRequest operationRequest){
        Transfer transfer = transferRepo.findByReference(operationRequest.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );
        if(transfer.getTransferState() == TransferState.SERVED)
            throw new InvalidTransferException("Transfer paid");

        transfer.setTransferState(TransferState.UNBLOCKED);
        validateOperation(transfer, operationRequest.getClientPhone());

        return transferRepo.save(transfer);
    }

    private Transfer validateOperation(Transfer transfer, String phone) {
        if(transfer.getTransferNotification()){
            //notification to client
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .phone(phone)
                    .transferReference(transfer.getReference())
                    .transferAmount(transfer.getTransferAmount())
                    .msgType(MsgType.TO_CLIENT.toString())
                    .transferState(transfer.getTransferState().toString())
                    .build();
            rabbitTemplate.convertAndSend(exchangeName, "MsgRoutingKey", notificationRequest);
        }
        return transferRepo.save(transfer);
    }

}
