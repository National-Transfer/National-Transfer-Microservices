package com.ensa.chargeservice.entites;

import com.ensa.chargeservice.enums.CoastType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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