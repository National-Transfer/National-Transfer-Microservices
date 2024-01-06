package com.ensa.accountservice.repository;


import com.ensa.accountservice.entity.Account;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepo extends JpaRepositoryImplementation<Account, UUID> {
    public Optional<Account> findAccountByAccountCode(UUID accountCode);

    public Optional<Account> findAccountByOwnerId(String ownerId);

    public Boolean existsAccountByAccountCode(UUID accountCode);
    public Boolean existsAccountByOwnerId(String ownerId);

}
