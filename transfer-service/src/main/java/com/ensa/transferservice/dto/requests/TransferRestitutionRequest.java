package com.ensa.transferservice.dto.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRestitutionRequest {

    private String reference;
    private String otp;
    private String clientPhone;
}
