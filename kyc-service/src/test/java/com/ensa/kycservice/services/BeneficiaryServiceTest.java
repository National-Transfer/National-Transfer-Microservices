package com.ensa.kycservice.services;

import com.ensa.kycservice.dto.BeneficiaryRequest;
import com.ensa.kycservice.entities.Beneficiary;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.entities.Prospect;
import com.ensa.kycservice.repositories.BeneficiaryRepository;
import com.ensa.kycservice.repositories.ClientRepository;
import com.ensa.kycservice.repositories.ProspectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BeneficiaryServiceTest {

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private ProspectRepository prospectRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private BeneficiaryService beneficiaryService;

    @Test
    void saveBeneficiaryForClient () {
        Long clientId = 1L;
        Client client = new Client();
        client.setBeneficiaries(new HashSet<>(Arrays.asList(new Beneficiary())));

        BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenAnswer(i -> i.getArguments()[0]);

        Beneficiary result = beneficiaryService.saveBeneficiaryForClient(clientId, beneficiaryRequest);
        assertNotNull(result);
    }

    @Test
    void saveBeneficiaryForProspect () {
        Long prospectId = 1L;
        Prospect prospect = new Prospect();
        prospect.setBeneficiaries(new HashSet<>(Arrays.asList(new Beneficiary())));
        BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
        Mockito.when(prospectRepository.findById(prospectId)).thenReturn(Optional.of(prospect));
        Mockito.when(prospectRepository.save(Mockito.any(Prospect.class))).thenAnswer(i -> i.getArguments()[0]);

        Beneficiary result = beneficiaryService.saveBeneficiaryForProspect(prospectId, beneficiaryRequest);
        assertNotNull(result);
    }

    @Test
    void getBeneficiariesForClient () {
        Long clientId = 1L;
        Client client = new Client();
        List<Beneficiary> beneficiaries = Arrays.asList(new Beneficiary());

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        Mockito.when(beneficiaryRepository.findByClient(client)).thenReturn(Optional.of(beneficiaries));

        List<Beneficiary> result = beneficiaryService.getBeneficiariesForClient(clientId);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(beneficiaries, result);
        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            beneficiaryService.getBeneficiariesForClient(clientId);
        });
    }

    @Test
    void getBeneficiariesForProspect () {
        Long prospectId = 1L;
        Prospect prospect = new Prospect();
        List<Beneficiary> beneficiaries = Arrays.asList(new Beneficiary());

        Mockito.when(prospectRepository.findById(prospectId)).thenReturn(Optional.of(prospect));
        Mockito.when(beneficiaryRepository.findByProspect(prospect)).thenReturn(Optional.of(beneficiaries));

        List<Beneficiary> result = beneficiaryService.getBeneficiariesForProspect(prospectId);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(beneficiaries, result);

        Mockito.when(prospectRepository.findById(prospectId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            beneficiaryService.getBeneficiariesForProspect(prospectId);
        });
    }

}