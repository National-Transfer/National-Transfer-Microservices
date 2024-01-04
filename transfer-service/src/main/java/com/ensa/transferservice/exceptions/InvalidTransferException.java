package com.ensa.transferservice.exceptions;

public class InvalidTransferException extends IllegalArgumentException {
    public InvalidTransferException(String message) {
        super(message);
    }
}

