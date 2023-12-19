package com.ensa.agencyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AgentResponseDto {

    private String id;
    private String firstName;
    private String lastName;

    private String phoneNumber;
    private String email;
    private SalesPointResponseDto salesPoint;

    private AccountResponseDto account;

}
