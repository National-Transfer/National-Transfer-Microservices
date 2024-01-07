package com.ensa.transferservice.feignClients;

import com.ensa.transferservice.dto.responses.AccountResponse;
import com.ensa.transferservice.dto.responses.RecipientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "account-service")
public interface AccountFeignClient {

    @GetMapping("/api/v1/accounts/owner/{ownerId}")
    ResponseEntity<AccountResponse> getAccountByOwnerId(@PathVariable String ownerId);

    @PostMapping("/api/v1/accounts/recievedTransfer/{ownerId}")
    ResponseEntity<AccountResponse> updateAccountBalancePlus(@PathVariable String ownerId, @RequestBody BigDecimal amount);

    @PostMapping("/api/v1/accounts/sendTransfer/{ownerId}")
    ResponseEntity<AccountResponse> updateAccountBalanceMinus(@PathVariable String ownerId, @RequestBody BigDecimal amount);

    @PostMapping("/api/v2/accounts/createAccount")
    ResponseEntity<AccountResponse> createAccount(@RequestBody RecipientResponse recipient);

}
