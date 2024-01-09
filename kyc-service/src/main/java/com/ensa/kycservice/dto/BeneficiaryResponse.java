package com.ensa.kycservice.dto;

import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.entities.Prospect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeneficiaryResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Prospect prospect;

    private Client client;
}
