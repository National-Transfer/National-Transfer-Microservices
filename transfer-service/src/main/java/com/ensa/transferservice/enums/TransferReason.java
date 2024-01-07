package com.ensa.transferservice.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
@Getter
public enum TransferReason {

    FAMILY_SUPPORT("Soutien familial"),
    SAVING_INVESTMENT( "Epargne/investissement"),
    GIFT("Cadeau"),
    PAYMENT( "Paiement de biens et de services"),
    OVERAGE_FEES( "Frais de dépassement"),
    RENTAL_MORTGAGE( "Location/Hypothèque"),
    EMERGENCY_MEDICAL_AID( "Aide de secours/Médicale"),
    INCOME_FROM_WEBSITE( "Revenu d’un site internet"),
    SALARY_EXPANSES("Dépenses salariales"),
    LOTTERY_FEES_REWARDS_TAXES( "Frais de loterie ou récompense/taxes"),
    READY( "Prêt"),
    INTERNET_COMMERCE( "Commerce sur internet"),
    DONATION("Donation"),
    OTHERS("Autres (à préciser) ");

    private final String transferReason;

}
