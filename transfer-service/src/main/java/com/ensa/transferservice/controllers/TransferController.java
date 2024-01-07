package com.ensa.transferservice.controllers;

import com.ensa.transferservice.dto.requests.*;
import com.ensa.transferservice.dto.responses.ClientResponse;
import com.ensa.transferservice.dto.responses.RecipientResponse;
import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.enums.TransferState;
import com.ensa.transferservice.repositories.TransferRepo;
import com.ensa.transferservice.services.ReceiveTransferService;
import com.ensa.transferservice.services.SendTransferService;
import com.ensa.transferservice.services.TransferOperationService;
import com.ensa.transferservice.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfer")
public class TransferController {

    private final TransferRepo transferRepo;
    private final TransferService transferService;
    private final SendTransferService sendTransferService;
    private final ReceiveTransferService receiveTransferService;
    private final RabbitTemplate rabbitTemplate;

    private final TransferOperationService transferOperationService;


    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllTransfers()
    {
        return ResponseEntity.ok(
                transferRepo.findAll()
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllTransfers(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "15") int size)
    {
        return ResponseEntity.ok(
                transferService.getAllTransfers(page, size)
        );
    }

    @GetMapping("TransfersForClient/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllTransfersForClient(@PathVariable String clientId)
    {
        return ResponseEntity.ok(
                transferService.getAllTransfersForClient(clientId)
        );
    }

    @GetMapping("/TransfersForBatch")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Transfer>> getAllTransfersForBatch()
    {
        return ResponseEntity.ok(
                transferService.getAllTransfersForBatch()
        );
    }

    @PostMapping("/saveAllTransfers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> saveAllTransfers(@RequestBody List<Transfer> transfers)
    {
        return ResponseEntity.ok(
                transferService.saveAllTransfers(transfers)
        );
    }

    @DeleteMapping("/deleteAllTransfers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteAllTransfers(@RequestBody List<Transfer> transfers)
    {
        transferService.deleteAllTransfers(transfers);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reference}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTransferByRef(@PathVariable String reference)
    {
        return ResponseEntity.ok(
                transferService.getTransferByReference(reference)
        );
    }

    @PostMapping("/clientSiron")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> checkClientSIRON(@RequestBody ClientResponse client)
    {
        return ResponseEntity.ok(
                sendTransferService.checkSIRON(client.getWalletId())
        );
    }

    @PostMapping("/transferAmount")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> calculateTransferAmount(@RequestBody TransferAmountRequest amountRequest)
    {
        return ResponseEntity.ok(
                sendTransferService.calculateTransferAmount(amountRequest)
        );
    }

    @PostMapping("/issueTransfer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> issueTransfer(@RequestBody TransferRequest transferRequest)
    {
        return ResponseEntity.ok(
                sendTransferService.issueTransfer(transferRequest)
        );
    }
    @PostMapping("/validateTransferByWallet")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> validateTransferByWallet(@RequestBody ValidateTransferRequest request)
    {
        return ResponseEntity.ok(
                sendTransferService.validateTransferByWallet(request)
        );
    }
    @PostMapping("/recipientSiron")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> checkRecipientSIRON(@RequestBody RecipientResponse recipient)
    {
        return ResponseEntity.ok(
                sendTransferService.checkSIRON(recipient.getWalletId())
        );
    }

    @GetMapping("/checkTransferToServe/{reference}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> checkTransferToServe(@PathVariable String reference)
    {
        return ResponseEntity.ok(
                receiveTransferService.checkTransferToServe(reference)
        );
    }

    @PostMapping("/serveTransferCash")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> serveTransferCash(@RequestBody ServeTransferRequest request)
    {
        return ResponseEntity.ok(
                receiveTransferService.serveTransferCash(request)
        );
    }

    @PostMapping("/serveTransferToWallet")
    public ResponseEntity<?> serveTransferToWallet(@RequestBody ServeTransferRequest request)
    {
        return ResponseEntity.ok(
                receiveTransferService.serveTransferToWallet(request)
        );
    }

    @PostMapping("/validateTransferToWallet")
    public ResponseEntity<Transfer> validateToServeTransfer(@RequestBody ValidateTransferRequest validateTransferRequest){
        return ResponseEntity.ok(
                receiveTransferService.validateTransferToWallet(validateTransferRequest)
        );
    }

    @PutMapping ("/extourner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> extournerTransfer(@RequestBody OperationRequest request)
    {
        return ResponseEntity.ok(
                transferOperationService.extournerTransfer(request)
        );
    }

    @PutMapping ("/restituerByAgent")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> restituerTransferByAgent(@RequestBody OperationRequest request)
    {
        return ResponseEntity.ok(
                transferOperationService.restituerTransferByAgent(request)
        );
    }

    @PostMapping ("/restituerByClient")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> restituerTransferByClient(@RequestBody OperationRequest request)
    {
        return ResponseEntity.ok(
                transferOperationService.restituerTransferByClient(request)
        );
    }
    @PutMapping ("/finalizeRestitutionByClient")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> finalizeRestitutionByClient(@RequestBody TransferRestitutionRequest request)
    {
        return ResponseEntity.ok(
                transferOperationService.finalizeRestitutionByClient(request)
        );
    }

    @PutMapping ("/block")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> blockTransfer(@RequestBody OperationRequest request)
    {
        return ResponseEntity.ok(
                transferOperationService.blockTransfer(request)
        );
    }
    @PutMapping ("/unblock/{reference}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> unblockTransfer(@RequestBody OperationRequest request)
    {
        return ResponseEntity.ok(
                transferOperationService.unblockTransfer(request)
        );
    }

    @GetMapping ("/dummy")
    @ResponseStatus(HttpStatus.OK)
    public void dummy()
    {
        String otp =  transferService.generateOtpForSms();

        NotificationRequest notif = NotificationRequest.builder()
                .phone("0616061968")
                .code(otp)
                .transferReference("EDP2345435")
                .transferAmount(BigDecimal.valueOf(100000))
                .transferState(TransferState.RETURNED.toString())
                .build();

                rabbitTemplate.convertAndSend("notificationExchange", "otpRoutingKey", notif);
    }


}
