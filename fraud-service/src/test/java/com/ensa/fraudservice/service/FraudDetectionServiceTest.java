package com.ensa.fraudservice.service;

import com.ensa.fraudservice.dto.ClientDto;
import com.ensa.fraudservice.entities.FraudDetection;
import com.ensa.fraudservice.repository.FraudDetectionRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FraudDetectionServiceTest {

    @Mock
    private FraudDetectionRepo fraudDetectionRepo;

    @InjectMocks
    private FraudDetectionService fraudDetectionService;

    @Test
    void checkClientBlacklisted () {
        Long clientId = 123L;
        when(fraudDetectionRepo.existsAccountByClientId(clientId)).thenReturn(true);
        assertTrue(fraudDetectionService.checkClientBlacklisted(clientId));
    }

    @Test
    void createBlacklistedClient () {
        ClientDto clientDto = new ClientDto();
        when(fraudDetectionRepo.existsAccountByClientId(clientDto.getClientId())).thenReturn(false);
        when(fraudDetectionRepo.save(Mockito.any(FraudDetection.class))).thenAnswer(i -> i.getArguments()[0]);

        FraudDetection result = fraudDetectionService.createBlacklistedClient(clientDto);
        assertNotNull(result);
        assertEquals(clientDto.getClientId(), result.getClientId());
    }

    @Test
    void deletedBlacklistedClient () {
        Long clientId = 123L;
        FraudDetection fraudDetection = new FraudDetection();
        when(fraudDetectionRepo.findFraudDetectionHistoryByClientId(clientId)).thenReturn(Optional.of(fraudDetection));

        assertTrue(fraudDetectionService.deletedBlacklistedClient(clientId));
        Mockito.verify(fraudDetectionRepo).delete(fraudDetection);
    }

    @Test
    void getBlackListedClients () {
        when(fraudDetectionRepo.findAll()).thenReturn(Collections.emptyList());
        List<FraudDetection> result = fraudDetectionService.getBlackListedClients();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}