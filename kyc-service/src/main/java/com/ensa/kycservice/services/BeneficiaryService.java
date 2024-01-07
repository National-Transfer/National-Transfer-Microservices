package com.ensa.kycservice.services;


import com.ensa.kycservice.dto.BeneficiaryRequest;
import com.ensa.kycservice.entities.Beneficiary;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.entities.Prospect;
import com.ensa.kycservice.repositories.BeneficiaryRepository;
import com.ensa.kycservice.repositories.ClientRepository;
import com.ensa.kycservice.repositories.ProspectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final ProspectRepository prospectRepository;
    private final ClientRepository clientRepository;

    public Beneficiary saveBeneficiaryForClient(Long clientId, BeneficiaryRequest beneficiaryRequest){
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found with id " + clientId));
        Beneficiary beneficiary = Beneficiary.builder()
                .firstName(beneficiaryRequest.getFirstName())
                .lastName(beneficiaryRequest.getLastName())
                .phoneNumber(beneficiaryRequest.getPhoneNumber())
                .client(client)
                .build();

        client.getBeneficiaries().add(beneficiary);
        clientRepository.save(client);

        return beneficiary;
    }

    public Beneficiary saveBeneficiaryForProspect(Long prospectId, BeneficiaryRequest beneficiaryRequest){
        Prospect prospect = prospectRepository.findById(prospectId).orElseThrow(() -> new EntityNotFoundException("Prospect not found with id " + prospectId));
        Beneficiary beneficiary = Beneficiary.builder()
                .firstName(beneficiaryRequest.getFirstName())
                .lastName(beneficiaryRequest.getLastName())
                .phoneNumber(beneficiaryRequest.getPhoneNumber())
                .prospect(prospect)
                .build();


        prospect.getBeneficiaries().add(beneficiary);
        prospectRepository.save(prospect);

        return beneficiary;
    }

    public List<Beneficiary> getBeneficiariesForClient(Long clientId){
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("Client not found with id " + clientId));
        return beneficiaryRepository.findByClient(client).orElseThrow();
    }

    public List<Beneficiary> getBeneficiariesForProspect(Long prospectId){
        Prospect prospect = prospectRepository.findById(prospectId).orElseThrow(() -> new EntityNotFoundException("Prospect not found with id " + prospectId));
        return beneficiaryRepository.findByProspect(prospect).orElseThrow();
    }

    public Beneficiary updateBeneficiary(Long id, BeneficiaryRequest request) {

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Beneficiary not found with id : " + id));

        if (request.getFirstName() != null){
            beneficiary.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null){
            beneficiary.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null){
            beneficiary.setPhoneNumber(request.getPhoneNumber());
        }

        return beneficiaryRepository.save(beneficiary);
    }

    public String deleteBeneficiary(Long id) {

        if (!beneficiaryRepository.existsById(id)) {
            throw new EntityNotFoundException("Beneficiary not found");
        }

        beneficiaryRepository.deleteById(id);
        return "beneficiary with id {" + id  + "} deleted successfully";
    }
}
