package com.ensa.kycservice.controllers;


import com.ensa.kycservice.dto.ClientRequest;
import com.ensa.kycservice.dto.ClientResponse;
import com.ensa.kycservice.dto.Response;
import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.services.BeneficiaryService;
import com.ensa.kycservice.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final BeneficiaryService beneficiaryService;

    @GetMapping("/{identityNumber}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable String identityNumber){
        return ResponseEntity.ok(
                clientService.getClient(identityNumber)
        );
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients(){
        return ResponseEntity.ok(
                clientService.getAllClients()
        );
    }

    @GetMapping("/exists/{identityNumber}")
    public ResponseEntity<Boolean> clientExists(@PathVariable String identityNumber) {
        return ResponseEntity.ok(
                clientService.clientExists(identityNumber)
        );
    }

    @PostMapping
    public ResponseEntity<ClientResponse> saveClient(@RequestBody ClientRequest clientRequest){
        return ResponseEntity.ok(
                clientService.saveClient(clientRequest));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest){
        return ResponseEntity.ok(
                clientService.updateClient(id,clientRequest)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id){
        return ResponseEntity.ok(
                clientService.deleteClient(id)
        );
    }

}
