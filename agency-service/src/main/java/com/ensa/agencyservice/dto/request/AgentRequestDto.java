package com.ensa.agencyservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AgentRequestDto {

    private String salesPointId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;
}
