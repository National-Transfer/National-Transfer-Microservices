package com.ensa.fraudservice.service;

import com.ensa.fraudservice.dto.ClientDto;
import com.ensa.fraudservice.entities.FraudDetection;
import com.ensa.fraudservice.repository.FraudDetectionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FraudDetectionService {
    private final FraudDetectionRepo fraudDetectionRepo;

    public Boolean checkClientBlacklisted(Long clientId){
        return fraudDetectionRepo.existsAccountByClientId(clientId);

    }

    public FraudDetection createBlacklistedClient(ClientDto clientDto) {
        if(fraudDetectionRepo.existsAccountByClientId(clientDto.getClientId())) throw new IllegalStateException("Client already blacklisted");
        FraudDetection newFraudDetection = FraudDetection.builder()
                .clientId(clientDto.getClientId())
                .clientCin(clientDto.getCin())
                .clientPhone(clientDto.getPhone())
                .build();
        return fraudDetectionRepo.save(newFraudDetection);
    }

    public Boolean deletedBlacklistedClient(Long clientId) {
        FraudDetection fraudDetection = fraudDetectionRepo.findFraudDetectionHistoryByClientId(clientId).orElseThrow( () -> new IllegalStateException("Client is not blacklisted"));
        fraudDetectionRepo.delete(fraudDetection);
        return Boolean.TRUE;
    }
}
