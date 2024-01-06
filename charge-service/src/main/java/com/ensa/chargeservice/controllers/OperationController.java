package com.ensa.chargeservice.controllers;

import com.ensa.chargeservice.dtos.TransferAmountRequest;
import com.ensa.chargeservice.dtos.TransferAmountResponse;
import com.ensa.chargeservice.serviceImp.CostServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/vi/charge")
@RequiredArgsConstructor
public class OperationController {

    private final CostServiceImp costService;

    @PostMapping
    ResponseEntity<TransferAmountResponse> getCommissionTotal(@RequestBody TransferAmountRequest transferAmountRequest){

        return ResponseEntity.ok(
                costService.calculateTransferAmount(transferAmountRequest)
        );
    }

}
