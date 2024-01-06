package com.ensa.accountservice.service;

import com.ensa.accountservice.entity.Account;
import com.ensa.accountservice.enums.AccountType;
import com.ensa.accountservice.repository.AccountRepo;

import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {
    private AccountRepo accountRepo;

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public Optional<Account> getAccountByAccountCode(UUID accountCode) {
        return accountRepo.findAccountByAccountCode(accountCode);
    }

    public Optional<Account> getAccountByOwnerId(String ownerId) {

        return accountRepo.findAccountByOwnerId(ownerId);
    }


    public Account createAccount(Account account) {
        if (accountRepo.existsAccountByAccountCode(account.getAccountCode())) throw new IllegalStateException("Account already exists");
        Account newAccount = Account.builder()
                .accountCode(account.getAccountCode())
                .balance(account.getBalance())
                .createdAt(LocalDateTime.now())
                .ownerId(account.getOwnerId())
                .build();

        if(account.getAccountType().equals(AccountType.AGENT)) {
            newAccount.setAccountType(AccountType.AGENT);
        }else {
            newAccount.setAccountType(AccountType.CLIENT);
        }

        return accountRepo.save(newAccount);
    }

    public Boolean deleteAccountByAccountCode(UUID accountCode) {
        Account account = accountRepo.findAccountByAccountCode(accountCode).orElseThrow( () -> new ResourceNotFoundException("Account doesn't exist"));
        accountRepo.delete(account);
        return Boolean.TRUE;
    }

    public Boolean deleteAccountByOwnerId(String ownerId) {
        Account account = accountRepo.findAccountByOwnerId(ownerId).orElseThrow( () -> new ResourceNotFoundException("Account doesn't exist"));
        accountRepo.delete(account);
        return Boolean.TRUE;
    }

    public Account deposit(String ownerId, BigDecimal amount) {
        Account account = accountRepo.findAccountByOwnerId(ownerId).orElseThrow( () -> new ResourceNotFoundException("Account doesn't exist"));
        if (amount.compareTo(BigDecimal.ZERO) <= 0 ){
            throw new IllegalStateException("Deposit amount must be greater than zero");
        }
        account.setBalance(account.getBalance().add(amount));
        return accountRepo.save(account);
    }

    public Account withdraw(String ownerId, BigDecimal amount) {
        Account account = accountRepo.findAccountByOwnerId(ownerId).orElseThrow( () -> new ResourceNotFoundException("Account doesn't exists"));
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero");
        }
        if(amount.compareTo(account.getBalance()) > 0) {
            throw new IllegalArgumentException("insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return accountRepo.save(account);
    }
}
