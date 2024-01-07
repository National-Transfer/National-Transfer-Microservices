package com.ensa.chargeservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)

@AllArgsConstructor
@Getter
public enum TransferType {

    BY_WALLET("By wallet"),
    IN_CASH("In cash");

    private final String transferType;

}
