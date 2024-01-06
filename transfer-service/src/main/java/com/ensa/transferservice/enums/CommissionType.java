package com.ensa.transferservice.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)

@Getter
@RequiredArgsConstructor
public enum CommissionType {

    SHARED_CHARGE("shared charges"),
    ON_BENEFICIARY( "Client beneficiary"),
    ON_SENDER( "Giver client ");

    private final String type;


}
