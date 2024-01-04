package com.ensa.agencyservice.exception;


public class AgentAlreadyExistsException extends RuntimeException{
    public AgentAlreadyExistsException(String message) {
        super(message);
    }
}
