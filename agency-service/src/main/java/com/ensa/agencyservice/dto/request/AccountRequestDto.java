package com.ensa.agencyservice.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountRequestDto {
    private String ownerId;
    // private AccountType accountType; "AGENT", "CLIENT"
}
