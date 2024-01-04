package com.ensa.kycservice.controllers;


import com.ensa.kycservice.dto.ClientRequest;
import com.ensa.kycservice.dto.Response;
import com.ensa.kycservice.services.BeneficiaryService;
import com.ensa.kycservice.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final BeneficiaryService beneficiaryService;

    @GetMapping("/{identityNumber}")
    public ResponseEntity<Response> getClient(@PathVariable String identityNumber){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Client retrieved with identity number : " + identityNumber)
                        .data(Map.of("client", clientService.getClient(identityNumber)))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<Response> getAllClients(){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("All clients retrieved ")
                        .data(Map.of("clients", clientService.getAllClients()))
                        .build()
        );
    }

    @GetMapping("/exists/{identityNumber}")
    public ResponseEntity<Response> clientExists(@PathVariable String identityNumber) {
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Client existence check completed")
                        .data(Map.of("exists", clientService.clientExists(identityNumber)))
                        .build()
        );
    }

    @GetMapping("/{id}/beneficiaries")
    public ResponseEntity<Response> getAllBeneficiaries(@PathVariable Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("All beneficiaries retrieved for client with id : " + id)
                        .data(Map.of("beneficiaries", beneficiaryService.getBeneficiariesForClient(id)))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<Response> saveClient(@RequestBody ClientRequest clientRequest){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(clientService.saveClient(clientRequest))
                        .build()
        );

    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(clientService.updateClient(id,clientRequest))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteClient(@PathVariable Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(clientService.deleteClient(id))
                        .build()
        );
    }

}
