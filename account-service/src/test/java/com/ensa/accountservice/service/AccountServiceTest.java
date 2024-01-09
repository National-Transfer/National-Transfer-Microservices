package com.ensa.accountservice.service;

import com.ensa.accountservice.entity.Account;
import com.ensa.accountservice.enums.AccountType;
import com.ensa.accountservice.repository.AccountRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepo accountRepo;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getAllAccounts () {
        Mockito.when(accountRepo.findAll()).thenReturn(List.of(new Account(), new Account()));
        assertEquals(2, accountService.getAllAccounts().size());
    }

    @Test
    void getAccountByOwnerId () {
        String existingOwnerId = "cfbce531-c365-4e98-a91a-e3e383e4c5d3";
        Account expectedAccount = new Account();
        Mockito.when(accountRepo.findAccountByOwnerId(existingOwnerId)).thenReturn(Optional.of(expectedAccount));

        Optional<Account> result1 = accountService.getAccountByOwnerId(existingOwnerId);

        String nonExistingOwnerId = "non-existing-id";
        Mockito.when(accountRepo.findAccountByOwnerId(nonExistingOwnerId)).thenReturn(Optional.empty());

        Optional<Account> result2 = accountService.getAccountByOwnerId(nonExistingOwnerId);



        assertTrue(result1.isPresent());
        assertEquals(expectedAccount, result1.get());
        assertFalse(result2.isPresent());

    }

    @Test
    void createAccount(){
        Account account = new Account(); // Initialize with proper values
        account.setAccountType(AccountType.AGENT);
        Mockito.when(accountRepo.existsAccountByAccountCode(account.getAccountCode())).thenReturn(false);
        accountService.createAccount(account);

        verify(accountRepo, times(1)).save(any(Account.class));
    }

    @Test
    void deleteAccountByAccountCode () {
        UUID accountCode = UUID.randomUUID();
        Account account = new Account();
        Mockito.when(accountRepo.findAccountByAccountCode(accountCode)).thenReturn(Optional.of(account));
        doNothing().when(accountRepo).delete(account);

        Boolean result = accountService.deleteAccountByAccountCode(accountCode);

        assertTrue(result);
        verify(accountRepo).delete(account);
    }

    @Test
    void deleteAccountByOwnerId () {
        String ownerId = "test";
        Account account = new Account();
        Mockito.when(accountRepo.findAccountByOwnerId(ownerId)).thenReturn(Optional.of(account));
        doNothing().when(accountRepo).delete(account);

        Boolean result = accountService.deleteAccountByOwnerId(ownerId);

        assertTrue(result);
        verify(accountRepo).delete(account);
    }
}