package com.ensa.chargeservice.entites;

import com.ensa.chargeservice.enums.CoastType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class Operation {

    private  Long transferId;

    private BigDecimal montant;
    private CoastType typeFrais;

    public Operation() {
    }
}
