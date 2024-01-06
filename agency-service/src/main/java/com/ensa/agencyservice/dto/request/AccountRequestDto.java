package com.ensa.agencyservice.dto.request;


import com.ensa.agencyservice.dto.enums.AccountType;
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
    private String accountType;
}
