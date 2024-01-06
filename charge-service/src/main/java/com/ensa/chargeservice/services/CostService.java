package com.ensa.chargeservice.services;

import com.ensa.chargeservice.dtos.TransferAmountRequest;
import com.ensa.chargeservice.dtos.TransferAmountResponse;
import com.ensa.chargeservice.entites.Operation;

import java.math.BigDecimal;

public interface CostService {
    TransferAmountResponse calculateTransferAmount(TransferAmountRequest request);
}
