package com.ensa.kycservice.services;



import com.ensa.kycservice.dto.ClientRequest;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.repositories.ClientRepository;
import com.ensa.kycservice.utils.CustomerProfileUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public Client getClient(String identityNumber) {
        return clientRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with identity number: " + identityNumber));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public String saveClient(ClientRequest clientRequest) {
        if (clientRepository.existsByIdentityNumber(clientRequest.getIdentityNumber())) {
            return "Client already exists";
        } else {
            clientRepository.save(clientRequest.mapToClient());
            return "Client saved successfully";
        }

        // Call Account Microservice to create Account
    }

    public boolean clientExists(String identityNumber) {
        return clientRepository.existsByIdentityNumber(identityNumber);
    }

    public String updateClient(Long id, ClientRequest clientRequest) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));

        CustomerProfileUtil.updateCustomerProfile(client, clientRequest);

        clientRepository.save(client);
        return "Client of id {" + id + "} updated successfully";
    }
    public String deleteClient(Long id){
        if (!clientRepository.existsById(id)){
            throw new EntityNotFoundException("client not found");
        }
        clientRepository.deleteById(id);
        return "client with id {" + id + "} deleted successfully";
    }

}
