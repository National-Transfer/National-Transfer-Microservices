package com.ensa.kycservice.services;


import com.ensa.kycservice.dto.AccountRequestDto;
import com.ensa.kycservice.dto.ProspectRequest;
import com.ensa.kycservice.entities.Beneficiary;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.entities.Prospect;
import com.ensa.kycservice.repositories.BeneficiaryRepository;
import com.ensa.kycservice.repositories.ClientRepository;
import com.ensa.kycservice.repositories.ProspectRepository;
import com.ensa.kycservice.utils.AccountUtil;
import com.ensa.kycservice.utils.CustomerProfileUtil;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
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
    private final AccountUtil accountUtil;
    private final com.okta.sdk.client.Client oktaClient;



    @Transactional
    public Prospect getProspect(String identityNumber){

        return prospectRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new EntityNotFoundException("Prospect not found with identity number: " + identityNumber));
    }

    public List<Prospect> getAllProspects() {
        return prospectRepository.findAll();
    }

    public Prospect saveProspect(ProspectRequest prospectRequest) {
        if (prospectRepository.existsByIdentityNumber(prospectRequest.getIdentityNumber())) {
            throw new IllegalStateException("Prospect already exists");
        } else {
            return prospectRepository.save(prospectRequest.mapToProspect());
        }
    }

    public boolean prospectExists(String identityNumber) {
        return prospectRepository.existsByIdentityNumber(identityNumber);
    }

    public Prospect updateProspect(Long id, ProspectRequest prospectRequest) { // here
        Prospect prospect = prospectRepository.findById(id).orElseThrow();

        CustomerProfileUtil.updateCustomerProfile(prospect, prospectRequest);
        return prospectRepository.save(prospect);
    }

    public String deleteProspect(Long id) {
        prospectRepository.deleteById(id);
        return "prospect with id {" + id + "} deleted successfully";
    }

    @Transactional
    public String convertToClient(Long id) {
        Prospect prospect = prospectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Prospect not found with id: " + id));
        Client client = new Client(prospect);
        if (prospect.getBeneficiaries() != null) {
            client.setBeneficiaries(new HashSet<>(prospect.getBeneficiaries()));
           client.getBeneficiaries().forEach(beneficiary -> {
                beneficiary.setProspect(null);
                beneficiary.setClient(client);
            });
        }

        Client clientSaved = clientRepository.save(client);
        prospectRepository.delete(prospect);

        // Call Account Microservice to create Account
        accountUtil.createAccountForClient(AccountRequestDto.builder().ownerId(String.valueOf(clientSaved.getId())).accountType("CLIENT").build());
        // create okta user
        User clientOkta = UserBuilder.instance()
                .setGroups("00ge70iv1oiDe46dg5d7") // AGENT GROUP ID "CLIENT"
                .setFirstName(clientSaved.getTitle())
                .setLastName(clientSaved.getFirstName())
                .setEmail(clientSaved.getEmail())
                .setActive(true)
                .setMobilePhone(clientSaved.getPhoneNumber())
                .buildAndCreate(oktaClient);
        clientOkta.setProfile(clientOkta.getProfile().setEmployeeNumber(clientSaved.getId().toString()));
        clientOkta.update();
        return "prospect converted to client successfully";
    }
}
