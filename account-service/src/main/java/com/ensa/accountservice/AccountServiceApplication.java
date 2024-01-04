package com.ensa.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountServiceApplication {

	private static List<AccountResponseDto> accounts = new ArrayList<>();
	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

	@PostMapping
	public AccountResponseDto createAccount(@RequestBody AccountRequestDto accountRequestDto){
		AccountResponseDto account = AccountResponseDto.builder()
				.ownerId(accountRequestDto.getOwnerId())
				.balance(BigDecimal.valueOf(0))
				.id("123456789")
				.build();
		accounts.add(account);
		return account;
	}
	@GetMapping("/{userId}")
	public AccountResponseDto getAccountByUserId(@PathVariable String userId){
		 AccountResponseDto account = AccountResponseDto.builder()
				 .id(UUID.randomUUID().toString())
				 .ownerId(userId)
				 .balance(BigDecimal.valueOf(0))
				 .build();
		return account;
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteAccountByUserId(@PathVariable String userId){
		// delete account
		return ResponseEntity.ok().build();
	}

}
