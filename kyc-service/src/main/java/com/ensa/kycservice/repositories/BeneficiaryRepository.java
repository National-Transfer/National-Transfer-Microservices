package com.ensa.kycservice.repositories;

import com.ensa.kycservice.entities.Beneficiary;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.entities.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    public Optional<List<Beneficiary>> findByProspect(Prospect prospect);
    public Optional<List<Beneficiary>> findByClient(Client client);
}
