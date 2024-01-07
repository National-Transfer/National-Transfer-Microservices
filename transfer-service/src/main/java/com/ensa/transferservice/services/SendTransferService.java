package com.ensa.transferservice.services;

import com.ensa.transferservice.dto.requests.NotificationRequest;
import com.ensa.transferservice.dto.requests.TransferAmountRequest;
import com.ensa.transferservice.dto.requests.TransferRequest;
import com.ensa.transferservice.dto.requests.ValidateTransferRequest;
import com.ensa.transferservice.dto.responses.AccountResponse;
import com.ensa.transferservice.dto.responses.SironCheckResponse;
import com.ensa.transferservice.dto.responses.TransferAmountResponse;
import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.enums.MsgType;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static com.ensa.transferservice.helper.Utils.*;

@Service
@Transactional
@RequiredArgsConstructor
public class SendTransferService {

    private final TransferRepo transferRepo;
    private final TransferService transferService;

    private final RabbitTemplate rabbitTemplate;

    private final FraudFeignClient fraudFeignClient;
    private final AccountFeignClient accountFeignClient;
    private final ChargeFeignClient chargeFeignClient;
    @Value("${notification.routingKey1}")
    private String msgRoutingKey;

    @Value("${notification.routingKey2}")
    private String otpRoutingKey;

    @Value("${notification.exchange}")
    private String exchangeName;

    public Boolean checkSIRON(String id) {
        SironCheckResponse sironCheck = fraudFeignClient.checkSIRON(id).getBody();

        if (sironCheck != null && sironCheck.getIsValid())
            throw new IllegalArgumentException("The client is BlackListed");

        return Boolean.TRUE;
    }

    private AccountResponse getAccount(TransferType transferType, String clientId, String agentId) {
        String id = switch (transferType) {
            case BY_WALLET -> clientId;
            case IN_CASH -> agentId;
            default -> throw new IllegalArgumentException("Unsupported TransferType: " + transferType);
        };
        return accountFeignClient.getAccountByOwnerId(id).getBody();
    }

    public TransferAmountResponse calculateTransferAmount(TransferAmountRequest transferAmountRequest) {
        AccountResponse account = getAccount(transferAmountRequest.getTransferType(),
                transferAmountRequest.getClientId(),
                transferAmountRequest.getAgentId()
        );

        if (account == null)
            throw new ResourceNotFoundException("Account not found");

        //commission
        TransferAmountResponse amountResponse = chargeFeignClient.getCommissionTotal(transferAmountRequest).getBody();

        if (amountResponse == null)
            throw new TransferAmountException("Error while returning the total amount !");

        //add notification cost
        if (transferAmountRequest.getTransferNotification())
            amountResponse.setTotalAmount(
                    amountResponse.getTotalAmount().add(TRANSFER_NOTIFICATION_COST)
            );

        if (amountResponse.getTotalAmount().compareTo(account.getBalance()) > 0)
            throw new TransferAmountException("The account doesn't have enough balance");


        if (transferAmountRequest.getTransferType().equals(TransferType.BY_WALLET)) {
            if (MAX_TRANSFER_LIMIT_PER_TRANSACTION.compareTo(amountResponse.getTotalAmount()) < 0)
                throw new TransferAmountException("The max amount allowed is " + MAX_TRANSFER_LIMIT_PER_TRANSACTION);

            if (ANNUAL_AMOUNT_TRANSFER_LIMIT.compareTo(account.getAnnualAmountTransfer()) < 0)
                throw new TransferAmountException("The max of transfers allowed in a single year should be < " + ANNUAL_AMOUNT_TRANSFER_LIMIT);

        } else if (transferAmountRequest.getTransferType().equals(TransferType.IN_CASH)) {
            if (MAX_TRANSFER_LIMIT_PER_TRANSACTION_FOR_AGENT.compareTo(amountResponse.getTotalAmount()) < 0)
                throw new TransferAmountException("The max amount allowed is " + MAX_TRANSFER_LIMIT_PER_TRANSACTION_FOR_AGENT);
        }

        return amountResponse;
    }


    public Transfer issueTransfer(TransferRequest transferRequest) {
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
                .commissionType(transferRequest.getCommissionType())
                .build();

        if (transferRequest.getTransferType().equals(TransferType.IN_CASH)) {
            transfer.setTransferState(TransferState.TO_SERVE);
            String pin = transferService.generatePinCode();

            //transfer already saved and here we only check on the notification

                if (transfer.getTransferNotification()) {
                    transfer.setPinCode(pin);

                    //msg to recipient with info
                    transferService.sendNotification(transferRequest.getPhone(), transfer.getReference(), transfer.getTransferAmount(), transfer.getTransferState(), MsgType.TO_RECIPIENT, pin);
                }

                //update agentAccount balance from front
                accountFeignClient.updateAccountBalancePlus(transfer.getAgentId(), transfer.getTransferAmount());

                return transferRepo.save(transfer);
            //end of function
        } else if (transferRequest.getTransferType().equals(TransferType.BY_WALLET)) {
            //generate otp
            String otp = transferService.generateOtpForSms();
            transfer.setTransferState(TransferState.TO_VALIDATE);
            transfer.setOtpCode(otp);

            transferService.sendNotification(transferRequest.getPhone(), transfer.getReference(), transfer.getTransferAmount(), transfer.getTransferState(), MsgType.OTP, otp);

            transferToReturn = transferRepo.save(transfer);
        }

        return transferToReturn;

    }


    public Transfer validateTransferByWallet(ValidateTransferRequest request) {
        Transfer transfer = transferRepo.findByReference(request.getReference()).orElseThrow(
                () -> new ResourceNotFoundException("Transfer not found")
        );
        if (transfer.getTransferType().equals(TransferType.BY_WALLET)) {
            String pin = transferService.generatePinCode();
            if (transfer.getTransferState() != TransferState.TO_VALIDATE)
                throw new InvalidTransferException("Invalid transfer state !");

            if (!transfer.getOtpCode().equals(request.getOtp()))
                throw new IllegalArgumentException("Incorrect OTP code !");

            transfer.setTransferState(TransferState.TO_SERVE);

            if (transfer.getTransferNotification()) {
                transfer.setPinCode(pin);

                //msg to recipient with info
                transferService.sendNotification(request.getPhone(), transfer.getReference(), transfer.getTransferAmount(), transfer.getTransferState(), MsgType.TO_RECIPIENT, pin);
            }
            //update clientAccount balance from front
            accountFeignClient.updateAccountBalanceMinus(transfer.getClientId(), transfer.getTransferAmount());
            return transferRepo.save(transfer);
        }

        throw new InvalidTransferException("Invalid transfer type !");

    }
}
