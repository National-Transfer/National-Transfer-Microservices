package com.ensa.kycservice.services.http;

import com.ensa.kycservice.dto.AccountRequestDto;
import com.ensa.kycservice.dto.AccountResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("account-service")
public interface AccountsFeignClient {
    @PostMapping("/api/v1/accounts/createAccount")
    ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto accountRequestDto);

    @GetMapping("/api/v1/accounts/owner/{ownerId}")
    ResponseEntity<AccountResponseDto> getAccountByOwnerId(@PathVariable Long ownerId);

    @DeleteMapping("/api/v1/accounts/owner/{ownerId}")
    ResponseEntity<Void> deleteAccountByOwnerId(@PathVariable Long ownerId);
}
