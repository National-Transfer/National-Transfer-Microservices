package com.ensa.kycservice.services;



import com.ensa.kycservice.dto.AccountRequestDto;
import com.ensa.kycservice.dto.AccountResponseDto;
import com.ensa.kycservice.dto.ClientRequest;
import com.ensa.kycservice.dto.ClientResponse;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.repositories.ClientRepository;
import com.ensa.kycservice.utils.AccountUtil;
import com.ensa.kycservice.utils.CustomerProfileUtil;
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
            return new ClientResponse(clientSaved, accountDto);
        }

    }


    public boolean clientExists(String identityNumber) {
        return clientRepository.existsByIdentityNumber(identityNumber);
    }

    public ClientResponse updateClient(Long id, ClientRequest clientRequest) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));

        CustomerProfileUtil.updateCustomerProfile(client, clientRequest);

        Client savedClient = clientRepository.save(client);
        AccountResponseDto accountDto = accountUtil.getAccountForClient(savedClient.getId());
        return new ClientResponse(savedClient, accountDto);
    }
    public String deleteClient(Long id){
        if (!clientRepository.existsById(id)){
            throw new EntityNotFoundException("client not found");
        }
        clientRepository.deleteById(id);
        return "client with id {" + id + "} deleted successfully";
    }

}
