package com.ensa.transferservice.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)

@Getter
@AllArgsConstructor
public enum TransferState
{
    TO_VALIDATE("to validate"),
    TO_SERVE("to serve"),
    SERVED("Served"),
    REVERSED("Reversed"),
    RETURNED("Returned"),
    BLOCKED("Blocked"),
    UNBLOCKED("Unblocked"),
    ESCHEAT("escheat");

    private final String transferState;

}