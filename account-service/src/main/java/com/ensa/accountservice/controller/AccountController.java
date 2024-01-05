package com.ensa.accountservice.controller;

import com.ensa.accountservice.entity.Account;
import com.ensa.accountservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("api/V1/accounts")
@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{accountCode}")
    public ResponseEntity<Optional<Account>> getAccountByAccountCode(@PathVariable UUID accountCode) {
        return ResponseEntity.ok(accountService.getAccountByAccountCode(accountCode));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Optional<Account>> getAccountByOwnerId(@PathVariable Long ownerId) {
        return ResponseEntity.ok(accountService.getAccountByOwnerId(ownerId));
    }

    @PostMapping("/recievedTransfer/{ownerId}")
    public ResponseEntity<Account> recievedTransfer(@PathVariable Long ownerId , @RequestBody BigDecimal amount){
        return ResponseEntity.ok(accountService.deposit(ownerId, amount));
    }

    @PostMapping("/sendTransfer/{ownerId}")
    public ResponseEntity<Account> emittedTransfer(@PathVariable Long ownerId , @RequestBody BigDecimal amount){
        return ResponseEntity.ok(accountService.withdraw(ownerId, amount));
    }

    @PostMapping("/createAccount")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @DeleteMapping("/{accountCode}")
    public ResponseEntity<Boolean> deleteAccountByAccountCode(@PathVariable UUID accountCode) {
        return ResponseEntity.ok(accountService.deleteAccountByAccountCode(accountCode));
    }

    @DeleteMapping("/{ownerId}")
    public ResponseEntity<Boolean> deleteAccountByOwnerId(@PathVariable Long ownerId) {
        return ResponseEntity.ok(accountService.deleteAccountByOwnerId(ownerId));
    }
}
