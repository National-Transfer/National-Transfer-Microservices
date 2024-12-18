package com.ensa.transferservice.services;

import com.ensa.transferservice.dto.responses.AccountResponse;
import com.ensa.transferservice.dto.requests.NotificationRequest;
import com.ensa.transferservice.dto.requests.TransferAmountRequest;
import com.ensa.transferservice.dto.requests.TransferRequest;
import com.ensa.transferservice.dto.requests.ValidateTransferRequest;
import com.ensa.transferservice.dto.responses.SironCheckResponse;
import com.ensa.transferservice.dto.responses.TransferAmountResponse;
import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.enums.TransferState;
import com.ensa.transferservice.enums.TransferType;
import com.ensa.transferservice.exceptions.InvalidTransferException;
import com.ensa.transferservice.exceptions.ResourceNotFoundException;
import com.ensa.transferservice.exceptions.TransferAmountException;
import com.ensa.transferservice.feignClients.AccountFeignClient;
import com.ensa.transferservice.feignClients.ChargeFeignClient;
import com.ensa.transferservice.feignClients.FraudFeignClient;
import com.ensa.transferservice.repositories.TransferRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.UUID;

import static com.ensa.transferservice.helper.Utils.*;

@Service
@RequiredArgsConstructor
public class SendTransferService {

    private final TransferRepo transferRepo;
    private final TransferService transferService;

    private final RabbitTemplate rabbitTemplate;

    private final FraudFeignClient fraudFeignClient;
    private final AccountFeignClient accountFeignClient;
    private final ChargeFeignClient chargeFeignClient;

    @Value("${notification.exchange}")
    private String exchangeName;

    public Boolean checkSIRON(String id){
        SironCheckResponse sironCheck = fraudFeignClient.checkSIRON(id).getBody();

        if(sironCheck != null && sironCheck.getIsValid())
            throw new IllegalArgumentException("The client is BlackListed");

        return Boolean.TRUE;
    }

    private AccountResponse getAccount (TransferType transferType, String clientId, String agentId) {
        String id = switch (transferType) {
            case BY_WALLET -> clientId;
            case IN_CASH -> agentId;
            default -> throw new IllegalArgumentException("Unsupported TransferType: " + transferType);
        };
        return accountFeignClient.getAccountByOwnerId(id).getBody();
    }

    public TransferAmountResponse calculateTransferAmount(TransferAmountRequest transferAmountRequest){
        AccountResponse account = getAccount(transferAmountRequest.getTransferType(),
                transferAmountRequest.getClientId(),
                transferAmountRequest.getAgentId()
        );

        if (account == null )
            throw new ResourceNotFoundException("Account not found");

        //commission
        TransferAmountResponse amountResponse = chargeFeignClient.getCommissionTotal(transferAmountRequest).getBody();

        if(amountResponse == null )
            throw new TransferAmountException("Error while returning the total amount !");

        //add notification cost
        if(transferAmountRequest.getTransferNotification())
            amountResponse.setTotalAmount(
                    amountResponse.getTotalAmount().add(TRANSFER_NOTIFICATION_COST)
            );

        if (amountResponse.getTotalAmount().compareTo(account.getBalance()) > 0 )
            throw new TransferAmountException("The account doesn't have enough balance");


        if(transferAmountRequest.getTransferType().equals(TransferType.BY_WALLET)){
            if(MAX_TRANSFER_LIMIT_PER_TRANSACTION.compareTo(amountResponse.getTotalAmount()) > 0)
                throw new TransferAmountException("The max amount allowed is " + MAX_TRANSFER_LIMIT_PER_TRANSACTION);

            if(ANNUAL_AMOUNT_TRANSFER_LIMIT.compareTo(account.getAnnualAmountTransfer()) > 0)
                throw new TransferAmountException("The max of transfers allowed in a single year should be < " + ANNUAL_AMOUNT_TRANSFER_LIMIT);

        } else
        if (transferAmountRequest.getTransferType().equals(TransferType.IN_CASH)) {
            if(MAX_TRANSFER_LIMIT_PER_TRANSACTION_FOR_AGENT.compareTo(amountResponse.getTotalAmount()) > 0)
                throw new TransferAmountException("The max amount allowed is " + MAX_TRANSFER_LIMIT_PER_TRANSACTION_FOR_AGENT);
        }

        return amountResponse;
    }

    private String generatePinCode() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder pinCodeBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int randomDigit = secureRandom.nextInt(10);
            pinCodeBuilder.append(randomDigit);
        }
        return pinCodeBuilder.toString();
    }

    public Transfer issueTransfer(TransferRequest transferRequest){
        Transfer transferToReturn = null;

        //reference
        String ref = UUID.randomUUID().toString();
        String reference = "EDP-".concat(ref);

        //expirationDate
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = currentDate.plusDays(NUMBER_OF_DAYS_FOR_TRANSFER_TO_EXPIRE);

        Transfer transfer = Transfer.builder()
                .reference(reference)
                .expirationDate(expirationDate)
                .transferAmount(transferRequest.getAmount())
                .agentId(transferRequest.getAgentId())
                .transferReason(transferRequest.getReason())
                .transferType(transferRequest.getTransferType())
                .clientId(transferRequest.getClientId())
                .recipientId(transferRequest.getRecipientId())
                .transferNotification(transferRequest.getTransferNotification())
                .build();

        if(transferRequest.getTransferType().equals(TransferType.IN_CASH)) {
            transfer.setTransferState(TransferState.TO_SERVE);

            transferToReturn =  transferRepo.save(transfer);
            //end of function
        } else
            if (transferRequest.getTransferType().equals(TransferType.BY_WALLET)) {
            //generate otp
            String otp =  transferService.generateOtpForSms();
            transfer.setTransferState(TransferState.TO_VALIDATE);
            transfer.setOtpCode(otp);

            NotificationRequest request = NotificationRequest.builder()
                    .phone(transferRequest.getPhone())
                    .code(otp)
                    .build();

            //send otp notification to client
            rabbitTemplate.convertAndSend(exchangeName, "otpRoutingKey", request);

            transferToReturn =  transferRepo.save(transfer);
        }

        return transferToReturn;

    }

    public Transfer validateTransfer(ValidateTransferRequest request){
        Transfer transfer = transferRepo.findByReference(request.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );

        //transfer already saved and here we only check on the notification
        if(transfer.getTransferType().equals(TransferType.IN_CASH)){
            if(transfer.getTransferNotification()) {
                String pin = generatePinCode();
                //msg to recipient with info
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .phone(request.getRecipientPhone())
                        .transferReference(transfer.getReference())
                        .code(pin)
                        .transferAmount(transfer.getTransferAmount())
                        .transferState(transfer.getTransferState())
                        .build();
                rabbitTemplate.convertAndSend(exchangeName, "msgRoutingKey", notificationRequest);
            }

            //update agentAccount balance from front
            accountFeignClient.updateAccountBalancePlus(transfer.getAgentId(), transfer.getTransferAmount());
            return transfer;
        }
        else if (transfer.getTransferType().equals(TransferType.BY_WALLET)){

            if(transfer.getTransferState() != TransferState.TO_VALIDATE)
                throw new InvalidTransferException("Invalid transfer state !");

            if(!transfer.getOtpCode().equals(request.getOtp()))
                throw new IllegalArgumentException("Incorrect OTP code !");

            transfer.setTransferState(TransferState.TO_SERVE);

            if(transfer.getTransferNotification()) {
                //msg to recipient with info
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .phone(request.getRecipientPhone())
                        .transferReference(transfer.getReference())
                        .transferAmount(transfer.getTransferAmount())
                        .transferState(transfer.getTransferState())
                        .build();

                rabbitTemplate.convertAndSend(exchangeName ,"msgRoutingKey", notificationRequest);
            }
            //update clientAccount balance from front
            accountFeignClient.updateAccountBalanceMinus(transfer.getClientId(), transfer.getTransferAmount());
            return transferRepo.save(transfer);
        }

        throw new InvalidTransferException("Invalid transfer type !");

    }

}
