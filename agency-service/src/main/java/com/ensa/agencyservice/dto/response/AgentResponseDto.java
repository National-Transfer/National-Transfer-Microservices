package com.ensa.agencyservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
