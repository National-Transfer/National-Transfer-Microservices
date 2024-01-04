package com.ensa.transferservice.exceptions;

public class TransferAmountException extends IllegalStateException {
    public TransferAmountException(String message) {
        super(message);
    }
}