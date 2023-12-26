package com.ensa.agencyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SalesPointResponseDto {
    private UUID id;

    private String name;

    private String address;

    private Double dailyTransferLimit;

    private String phoneNumber;
}
