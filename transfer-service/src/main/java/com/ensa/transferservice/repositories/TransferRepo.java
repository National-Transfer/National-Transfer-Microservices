package com.ensa.transferservice.repositories;
import com.ensa.transferservice.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferRepo extends JpaRepository<Transfer, String> {

    public Optional<Transfer> findByReference(String reference);
}
