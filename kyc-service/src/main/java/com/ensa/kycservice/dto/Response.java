package com.ensa.kycservice.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@Builder

public class Response {

    private int statusCode;
    private HttpStatus status;
    private String message;
    private Map<?,?> data;

}
