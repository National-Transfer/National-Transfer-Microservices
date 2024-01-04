package com.ensa.kycservice.repositories;

import com.ensa.kycservice.entities.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProspectRepository extends JpaRepository<Prospect, Long> {

    public Optional<Prospect> findByIdentityNumber(String identityNumber);
    public Boolean existsByIdentityNumber(String identityNumber);
}
