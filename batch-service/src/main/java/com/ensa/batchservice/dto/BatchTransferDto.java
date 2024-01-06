package com.ensa.batchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchTransferDto {

    List<TransferDto> transfersToValidate;
    List<TransferDto> transfersToServe;

}
