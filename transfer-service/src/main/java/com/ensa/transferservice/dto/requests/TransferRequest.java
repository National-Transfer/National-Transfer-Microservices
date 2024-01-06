package com.ensa.transferservice.dto.requests;

import com.ensa.transferservice.enums.CommissionType;
import com.ensa.transferservice.enums.TransferReason;
import com.ensa.transferservice.enums.TransferType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    private String agentId;
    @NotNull
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String phone;
    private String clientId;
    private BigDecimal amount;
    private TransferReason reason;
    private Boolean transferNotification;
    private String recipientId;
    private TransferType transferType;
    private CommissionType commissionType;


}
