package com.ensa.chargeservice.serviceImp;

import com.ensa.chargeservice.dtos.TransferAmountRequest;
import com.ensa.chargeservice.dtos.TransferAmountResponse;
import com.ensa.chargeservice.reps.OperationRepository;
import com.ensa.chargeservice.services.CostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CostServiceImp implements CostService {
    private final OperationRepository operationRepository;

    @Override
    public TransferAmountResponse calculateTransferAmount(TransferAmountRequest request) {
        String commissionType = request.getCommissionType();
        BigDecimal montantSaisie = request.getAmount();

        BigDecimal commissionAmount = calculateTransferFees(request.getAmount());

        TransferAmountResponse response = TransferAmountResponse.builder()
                .transferAmount(montantSaisie)
                .build();

        if (commissionType.equals("ON_BENEFICIARY")) {
            response.setCommissionAmount(commissionAmount);
            response.setTotalAmount(montantSaisie.subtract(commissionAmount));

        } else if (commissionType.equals("ON_SENDER")) {
            response.setCommissionAmount(commissionAmount);
            response.setTotalAmount(montantSaisie.add(commissionAmount));

        } else if (commissionType.equals("SHARED_CHARGE")) {
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
