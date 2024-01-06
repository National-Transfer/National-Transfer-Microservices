package com.ensa.chargeservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferAmountResponse {

    private BigDecimal transferAmount;
    private BigDecimal commissionAmount;
    private BigDecimal totalAmount;
}
