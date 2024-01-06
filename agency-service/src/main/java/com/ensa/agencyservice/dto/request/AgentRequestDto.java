package com.ensa.agencyservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AgentRequestDto {

    @NotEmpty
    private String salesPointId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String phoneNumber;

    @NotNull
    @Email
    private String email;


    private LocalDateTime createdAt;
}

