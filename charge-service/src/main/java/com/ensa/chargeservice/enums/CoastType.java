package com.ensa.chargeservice.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)

@Getter
@RequiredArgsConstructor

public enum CoastType {
    SHARED_CHARGE("shared charges"),
    CLIENT_BENEFICIARY( "Client beneficiary"),
    GIVER_CLIENT( "Giver client ");

    private final String type;
}
