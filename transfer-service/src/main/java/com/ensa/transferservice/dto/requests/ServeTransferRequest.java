package com.ensa.transferservice.dto.requests;

import com.ensa.transferservice.dto.responses.RecipientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServeTransferRequest {

//    private String agency;
    private String reference;
//    private RecipientResponse recipient;
    private String phone;
//    private String agentId;

}
