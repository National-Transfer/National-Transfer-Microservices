package com.ensa.transferservice.controllers;

import com.ensa.transferservice.dto.requests.*;
import com.ensa.transferservice.dto.responses.ClientResponse;
import com.ensa.transferservice.dto.responses.RecipientResponse;
import com.ensa.transferservice.services.ReceiveTransferService;
import com.ensa.transferservice.services.SendTransferService;
import com.ensa.transferservice.services.TransferOperationService;
import com.ensa.transferservice.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;
    private final SendTransferService sendTransferService;
    private final ReceiveTransferService receiveTransferService;

    private final TransferOperationService transferOperationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllTransfers()
    {
        return ResponseEntity.ok(
                transferService.getAllTransfers()
        );
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
    @PostMapping("/validateTransfer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> validateTransfer(@RequestBody ValidateTransferRequest request)
    {
        return ResponseEntity.ok(
                sendTransferService.validateTransfer(request)
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

    @GetMapping("/{reference}")
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

}
