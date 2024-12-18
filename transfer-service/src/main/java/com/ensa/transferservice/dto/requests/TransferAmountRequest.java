package com.ensa.transferservice.dto.requests;

import com.ensa.transferservice.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferAmountRequest {

   private BigDecimal amount;
   private String CommissionType;
   private String clientId;
   private String agentId;
   private Boolean transferNotification;
   private TransferType transferType;
}
