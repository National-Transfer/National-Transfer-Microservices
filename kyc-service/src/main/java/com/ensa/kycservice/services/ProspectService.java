package com.ensa.kycservice.services;


import com.ensa.kycservice.dto.ProspectRequest;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.entities.Prospect;
import com.ensa.kycservice.repositories.BeneficiaryRepository;
import com.ensa.kycservice.repositories.ClientRepository;
import com.ensa.kycservice.repositories.ProspectRepository;
import com.ensa.kycservice.utils.CustomerProfileUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor

@Service
public class ProspectService {

    private final ProspectRepository prospectRepository;
    private final ClientRepository clientRepository;
    private final BeneficiaryRepository beneficiaryRepository;


    public Prospect getProspect(String identityNumber){
        return prospectRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new EntityNotFoundException("Prospect not found with identity number: " + identityNumber));
    }

    public List<Prospect> getAllProspects(){
        return prospectRepository.findAll();
    }

    public String saveProspect(ProspectRequest prospectRequest){
        if (prospectRepository.existsByIdentityNumber(prospectRequest.getIdentityNumber())){
            return "prospect already exists";
        } else{
            prospectRepository.save(prospectRequest.mapToProspect());
            return "prospect saved successfully";
        }
    }

    public boolean prospectExists(String identityNumber) {
        return prospectRepository.existsByIdentityNumber(identityNumber);
    }

    public String updateProspect(Long id, ProspectRequest prospectRequest){ // here
        Prospect prospect = prospectRepository.findById(id).orElseThrow();

        CustomerProfileUtil.updateCustomerProfile(prospect, prospectRequest);
        prospectRepository.save(prospect);
        return "prospect of id {" + id + "} updated successfully";
    }

    public String deleteProspect(Long id){
        prospectRepository.deleteById(id);
        return "prospect with id {" + id + "} deleted successfully";
    }

    @Transactional
    public String convertToClient(Long id){
        Prospect prospect = prospectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Prospect not found with id: " + id));
        Client client = new Client(prospect);
        clientRepository.save(client);
        if (prospect.getBeneficiaries() != null) {
            client.setBeneficiaries(new HashSet<>(prospect.getBeneficiaries()));

            client.getBeneficiaries().forEach(beneficiary -> {
                beneficiary.setProspect(null);
                beneficiary.setClient(client);

            });
            beneficiaryRepository.saveAll(client.getBeneficiaries());
        }

        prospectRepository.delete(prospect);

        // Call Account Microservice to create Account

        return "prospect converted to client successfully";
    }
}
