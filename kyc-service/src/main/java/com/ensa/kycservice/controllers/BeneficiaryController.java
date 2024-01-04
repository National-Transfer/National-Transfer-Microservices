package com.ensa.kycservice.controllers;


import com.ensa.kycservice.dto.BeneficiaryRequest;
import com.ensa.kycservice.dto.Response;
import com.ensa.kycservice.services.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor

@RestController("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;


    @PostMapping("/prospect/{prospectId}")
    public ResponseEntity<Response> saveBeneficiaryForProspect(@PathVariable Long prospectId, @RequestBody BeneficiaryRequest request){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(beneficiaryService.saveBeneficiaryForProspect(prospectId, request))
                        .build()
        );
    }

    @PostMapping("/client/{clientId}")
    public ResponseEntity<Response> saveBeneficiaryForClient(@PathVariable Long clientId, @RequestBody BeneficiaryRequest request){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(beneficiaryService.saveBeneficiaryForClient(clientId, request))
                        .build()
        );
    }

    @GetMapping("/prospect/{id}")
    public ResponseEntity<Response> getAllBeneficiariesForProspect(@PathVariable Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("All beneficiaries retrieved for prospect with id : " + id)
                        .data(Map.of("beneficiaries", beneficiaryService.getBeneficiariesForProspect(id)))
                        .build()
        );
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<Response> getAllBeneficiariesForClient(@PathVariable Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("All beneficiaries retrieved for client with id : " + id)
                        .data(Map.of("beneficiaries", beneficiaryService.getBeneficiariesForClient(id)))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateBeneficiary(@PathVariable Long id, @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(beneficiaryService.updateBeneficiary(id,request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteBeneficiary(@PathVariable Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(beneficiaryService.deleteBeneficiary(id))
                        .build()
        );
    }
}
