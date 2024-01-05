package com.ensa.transferservice.repositories;
import com.ensa.transferservice.entities.Transfer;
import com.ensa.transferservice.enums.TransferState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepo extends JpaRepository<Transfer, String> {

    public Optional<Transfer> findByReference(String reference);

    public List<Transfer> findByTransferState(TransferState transferState);

}

