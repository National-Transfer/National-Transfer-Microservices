package com.ensa.kycservice.controllers;


import com.ensa.kycservice.dto.BeneficiaryRequest;
import com.ensa.kycservice.dto.BeneficiaryResponse;
import com.ensa.kycservice.dto.Response;
import com.ensa.kycservice.entities.Beneficiary;
import com.ensa.kycservice.services.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;


    @PostMapping("/prospect/{prospectId}")
    public ResponseEntity<Beneficiary> saveBeneficiaryForProspect(@PathVariable Long prospectId, @RequestBody BeneficiaryRequest request){
        return ResponseEntity.ok(
                beneficiaryService.saveBeneficiaryForProspect(prospectId, request)
        );
    }

    @PostMapping("/client/{clientId}")
    public ResponseEntity<Beneficiary> saveBeneficiaryForClient(@PathVariable Long clientId, @RequestBody BeneficiaryRequest request){
        return ResponseEntity.ok(
                beneficiaryService.saveBeneficiaryForClient(clientId, request)
        );
    }

    @GetMapping("/prospect/{id}")
    public ResponseEntity<List<Beneficiary>> getAllBeneficiariesForProspect(@PathVariable Long id){
        return ResponseEntity.ok(
                beneficiaryService.getBeneficiariesForProspect(id)
        );
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<Beneficiary>> getAllBeneficiariesForClient(@PathVariable Long id){
        return ResponseEntity.ok(
                beneficiaryService.getBeneficiariesForClient(id)
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaryResponse> getBeneficiary(@PathVariable Long id){
        return ResponseEntity.ok(
                beneficiaryService.getBeneficiary(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Beneficiary> updateBeneficiary(@PathVariable Long id, @RequestBody BeneficiaryRequest request) {
        return ResponseEntity.ok(
                beneficiaryService.updateBeneficiary(id,request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBeneficiary(@PathVariable Long id) {
        return ResponseEntity.ok(
                beneficiaryService.deleteBeneficiary(id)
        );
    }
}
