package com.ensa.transferservice.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipientResponse {

    private String firstName;
    private String lastName;
    private String phone;
    private String walletId;
    private String cin;

}
