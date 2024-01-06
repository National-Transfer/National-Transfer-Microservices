package com.ensa.chargeservice.serviceImp;

import com.ensa.chargeservice.entites.Operation;
import com.ensa.chargeservice.enums.CoastType;
import com.ensa.chargeservice.reps.OperationRepository;
import com.ensa.chargeservice.services.CoastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.ensa.chargeservice.enums.CoastType.*;

@RequiredArgsConstructor
@Service
public class CoastServiceImp implements CoastService {
    private final OperationRepository operationRepository;

    @Override
    public BigDecimal calculateTheTotalAmount(Operation operation) {
        CoastType typeFrais = operation.getTypeFrais();
        BigDecimal montantSaisie = operation.getMontant();

        if (GIVER_CLIENT.equals(typeFrais)) {
            return montantSaisie.add(calculateTransferFees(operation));
        } else if (GIVER_CLIENT.equals(typeFrais)) {
            return montantSaisie;
        } else if (SHARED_CHARGE.equals(typeFrais)) {
            BigDecimal fraisPartages = calculateTransferFees(operation).divide(BigDecimal.valueOf(2));
            return montantSaisie.add(fraisPartages);
        } else {
            throw new IllegalArgumentException("Type de frais non pris en charge : " + typeFrais);
        }
    }

    @Override
    public BigDecimal calculateTransferAmount(Operation operation) {
        CoastType typeFrais = operation.getTypeFrais();
        BigDecimal montantSaisie = operation.getMontant();

        if (GIVER_CLIENT.equals(typeFrais)) {
            return montantSaisie;
        } else if (GIVER_CLIENT.equals(typeFrais)) {
            return montantSaisie.subtract(calculateTransferFees(operation));
        } else if (SHARED_CHARGE.equals(typeFrais)) {
            BigDecimal fraisPartages = calculateTransferFees(operation).divide(BigDecimal.valueOf(2));
            return montantSaisie.subtract(fraisPartages);
        } else {
            throw new IllegalArgumentException("Type de frais non pris en charge : " + typeFrais);
        }
    }

    private BigDecimal calculateTransferFees(Operation operation) {


        // Exemple  10% du montant de l'opération
        return operation.getMontant().multiply(BigDecimal.valueOf(0.10));
    }
}
