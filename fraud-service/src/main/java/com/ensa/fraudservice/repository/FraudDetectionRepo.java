package com.ensa.fraudservice.repository;

import com.ensa.fraudservice.entities.FraudDetection;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;
import java.util.UUID;

public interface FraudDetectionRepo extends JpaRepositoryImplementation<FraudDetection, UUID> {
    public Boolean existsAccountByClientId(Long ownerId);
    public Optional<FraudDetection> findFraudDetectionHistoryByClientId(Long clientId);
}
