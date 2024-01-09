package com.ensa.kycservice.repositories;

import com.ensa.kycservice.entities.Beneficiary;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.entities.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    public Optional<Client> findByIdentityNumber(String identityNumber);
    public Boolean existsByIdentityNumber(String identityNumber);
    @Query("SELECT c FROM Client c JOIN c.beneficiaries b WHERE b = :beneficiary")
    Client findByBeneficiary(@Param("beneficiary") Beneficiary beneficiary);
}