package com.ensa.transferservice.feignClients;

import com.ensa.transferservice.dto.requests.TransferAmountRequest;
import com.ensa.transferservice.dto.responses.TransferAmountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "charge-service")
public interface ChargeFeignClient {

    @PostMapping("/api/charge")
    ResponseEntity<TransferAmountResponse> getCommissionTotal(@RequestBody TransferAmountRequest transferAmountRequest);

}
