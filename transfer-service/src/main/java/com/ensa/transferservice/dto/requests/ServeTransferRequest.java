package com.ensa.transferservice.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServeTransferRequest {

    private String reference;
    private String phone;
    private String clientId;
}
