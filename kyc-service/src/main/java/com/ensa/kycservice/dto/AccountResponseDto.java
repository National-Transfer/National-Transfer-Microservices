package com.ensa.kycservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountResponseDto {
    private BigDecimal balance ;
    private String accountCode;
    private String ownerId;

}