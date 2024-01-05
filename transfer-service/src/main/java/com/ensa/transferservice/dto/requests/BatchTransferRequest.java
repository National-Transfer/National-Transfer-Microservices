package com.ensa.transferservice.dto.requests;

import com.ensa.transferservice.entities.Transfer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchTransferRequest {

    List<Transfer> transferToValidate;
    List<Transfer> transfersToServe;

}
