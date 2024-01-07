package com.ensa.transferservice.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateTransferRequest {
    private String reference;
    private String otp;
    @NotNull
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String phone;
    private String recipientId;
}
