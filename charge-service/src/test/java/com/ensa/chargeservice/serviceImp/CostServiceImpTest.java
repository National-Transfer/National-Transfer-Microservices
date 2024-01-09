package com.ensa.chargeservice.serviceImp;

import com.ensa.chargeservice.dtos.CommissionType;
import com.ensa.chargeservice.dtos.TransferAmountRequest;
import com.ensa.chargeservice.dtos.TransferAmountResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CostServiceImpTest {



    @InjectMocks
    private CostServiceImp costService;

    @Test
    void calculateTransferAmount () {

        BigDecimal testAmount = new BigDecimal("500");
        BigDecimal expectedCommission = new BigDecimal("50.0"); // Example value
        TransferAmountRequest request = new TransferAmountRequest();
        request.setAmount(testAmount);
        request.setCommissionType(CommissionType.ON_BENEFICIARY);

        CostServiceImpTest service;
        TransferAmountResponse response = costService.calculateTransferAmount(request);

        assertEquals(expectedCommission, response.getCommissionAmount());
        assertEquals(testAmount.subtract(expectedCommission), response.getTotalAmount());

    }
}