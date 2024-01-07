package com.ensa.batchservice.feign;



import com.ensa.batchservice.dto.TransferDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@FeignClient(name = "transfer-service")
public interface TransferFeignClient {

    @GetMapping("/api/v1/transfer/TransfersForBatch")
    ResponseEntity<List<TransferDto>> getAllTransfersForBatch();

    @PostMapping("/api/v1/transfer/saveAllTransfers")
    ResponseEntity<List<TransferDto>> saveAllTransfers(@RequestBody List<TransferDto> transfers);

    @DeleteMapping("/api/v1/transfer/deleteAllTransfers")
    ResponseEntity<Void> deleteAllTransfers(@RequestBody List<TransferDto> transfers);

}
