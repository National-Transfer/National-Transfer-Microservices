package com.ensa.kycservice.services;



import com.ensa.kycservice.dto.AccountRequestDto;
import com.ensa.kycservice.dto.AccountResponseDto;
import com.ensa.kycservice.dto.ClientRequest;
import com.ensa.kycservice.dto.ClientResponse;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.repositories.ClientRepository;
import com.ensa.kycservice.utils.AccountUtil;
import com.ensa.kycservice.utils.CustomerProfileUtil;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountUtil accountUtil;
    private final com.okta.sdk.client.Client oktaClient;

    public ClientResponse getClient(String identityNumber) {
        Client client =  clientRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with identity number: " + identityNumber));
        AccountResponseDto accountDto = accountUtil.getAccountForClient(client.getId());
        return new ClientResponse(client, accountDto);
    }

    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll().stream()
                .map(client -> {
                    AccountResponseDto accountDto = accountUtil.getAccountForClient(client.getId());
                    return new ClientResponse(client, accountDto);
                }).collect(Collectors.toList());
    }

    public ClientResponse saveClient(ClientRequest clientRequest) {
        if (clientRepository.existsByIdentityNumber(clientRequest.getIdentityNumber())) {
            throw new IllegalStateException("Client already exists");
        } else {
            Client clientSaved = clientRepository.save(clientRequest.mapToClient());
            AccountResponseDto accountDto = accountUtil.createAccountForClient(AccountRequestDto.builder().ownerId(String.valueOf(clientSaved.getId())).accountType("CLIENT").build());
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
            return new ClientResponse(clientSaved, accountDto);
        }

    }


    public boolean clientExists(String identityNumber) {
        return clientRepository.existsByIdentityNumber(identityNumber);
    }

    public ClientResponse updateClient(Long id, ClientRequest clientRequest) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));

        User oktaClientUser = oktaClient.getUser(client.getEmail());
        CustomerProfileUtil.updateCustomerProfile(client, clientRequest);
        CustomerProfileUtil.updateOktaProfile(oktaClientUser.getProfile(),clientRequest);
        Client savedClient = clientRepository.save(client);
        AccountResponseDto accountDto = accountUtil.getAccountForClient(savedClient.getId());
        // update okta user
        oktaClientUser.update();
        return new ClientResponse(savedClient, accountDto);
    }
    public String deleteClient(Long id){
        Client client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("client not found"));

        User oktaClientUser =  oktaClient.getUser(client.getEmail());
        oktaClientUser.deactivate();
        oktaClientUser.delete(true);
        clientRepository.deleteById(id);
        return "client with id {" + id + "} deleted successfully";
    }

}
