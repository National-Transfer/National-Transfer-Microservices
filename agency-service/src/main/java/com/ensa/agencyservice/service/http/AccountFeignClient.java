package com.ensa.agencyservice.service.http;

import com.ensa.agencyservice.dto.request.AccountRequestDto;
import com.ensa.agencyservice.dto.response.AccountResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("account-service")
public interface AccountFeignClient {
    @PostMapping("/api/v1/accounts")
    ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto accountRequestDto);

    @GetMapping("/api/v1/accounts/{ownerId}")
    ResponseEntity<AccountResponseDto> getAccountByOwnerId(@PathVariable String ownerId);

    @DeleteMapping("/api/v1/accounts/{ownerId}")
    ResponseEntity<Void> deleteAccountByOwnerId(@PathVariable String ownerId);
}
