package com.ensa.transferservice.feignClients;

import com.ensa.transferservice.dto.responses.SironCheckResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "fraud-service")
public interface FraudFeignClient {

    @GetMapping("/api/fraudCheck/{clientId}")
    ResponseEntity<SironCheckResponse> checkSIRON(@PathVariable String clientId);
}
