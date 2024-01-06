package com.ensa.chargeservice.services;

import com.ensa.chargeservice.entites.Operation;

import java.math.BigDecimal;

public interface CoastService {
    BigDecimal calculateTheTotalAmount(Operation operation);
    BigDecimal calculateTransferAmount(Operation operation);
}
