package com.ensa.transferservice.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipientRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private String walletId;
    private String cin;

}
