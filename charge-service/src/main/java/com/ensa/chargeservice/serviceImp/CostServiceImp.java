package com.ensa.chargeservice.serviceImp;

import com.ensa.chargeservice.dtos.CommissionType;
import com.ensa.chargeservice.dtos.TransferAmountRequest;
import com.ensa.chargeservice.dtos.TransferAmountResponse;
import com.ensa.chargeservice.services.CostService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CostServiceImp implements CostService {

    @Override
    public TransferAmountResponse calculateTransferAmount(TransferAmountRequest request) {
        CommissionType commissionType = request.getCommissionType();
        BigDecimal montantSaisie = request.getAmount();

        BigDecimal commissionAmount = calculateTransferFees(request.getAmount());

        TransferAmountResponse response = TransferAmountResponse.builder()
                .transferAmount(montantSaisie)
                .build();

        if (commissionType.equals(CommissionType.ON_BENEFICIARY)) {
            response.setCommissionAmount(commissionAmount);
            response.setTotalAmount(montantSaisie.subtract(commissionAmount));

        } else if (commissionType.equals(CommissionType.ON_SENDER)) {
            response.setCommissionAmount(commissionAmount);
            response.setTotalAmount(montantSaisie.add(commissionAmount));

        } else if (commissionType.equals(CommissionType.SHARED_CHARGE)) {
            BigDecimal fraisPartages = commissionAmount.divide(BigDecimal.valueOf(2));
            response.setCommissionAmount(fraisPartages);

            response.setTotalAmount(montantSaisie.subtract(fraisPartages));
        }

        return response;

    }

    private BigDecimal calculateTransferFees(BigDecimal amount) {

        // Exemple  10% du montant de l'opération
        return amount.multiply(BigDecimal.valueOf(0.10));
    }
}
