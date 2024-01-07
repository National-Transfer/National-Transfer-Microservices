package com.ensa.batchservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TransferDto {

    private UUID id;
//    private String reference;
//    private BigDecimal transferAmount;
    //private String transferType;
//    private String transferReason;
    private String transferState;
    private LocalDateTime transferDate;
    private LocalDate expirationDate;
//    private String clientId;

    @JsonIgnore
    private String action;


}
