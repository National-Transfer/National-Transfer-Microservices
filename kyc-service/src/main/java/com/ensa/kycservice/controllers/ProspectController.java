package com.ensa.kycservice.controllers;


import com.ensa.kycservice.dto.ProspectRequest;
import com.ensa.kycservice.entities.Prospect;
import com.ensa.kycservice.services.ProspectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/prospects")
public class ProspectController {

    private final ProspectService prospectService;

    @GetMapping("/{identityNumber}")
    public ResponseEntity<Prospect> getProspect(@PathVariable String identityNumber){
        return ResponseEntity.ok(
                prospectService.getProspect(identityNumber)
        );
    }

    @GetMapping
    public ResponseEntity<List<Prospect>> getAllProspects(){
        return ResponseEntity.ok(
                prospectService.getAllProspects()
        );
    }

    @GetMapping("/exists/{identityNumber}")
    public ResponseEntity<Boolean> prospectExists(@PathVariable String identityNumber) {
        return ResponseEntity.ok(
                prospectService.prospectExists(identityNumber)
        );
    }

    @PostMapping
    public ResponseEntity<Prospect> saveProspect(@RequestBody ProspectRequest prospectRequest){
        return ResponseEntity.ok(
                prospectService.saveProspect(prospectRequest)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prospect> updateProspect(@PathVariable Long id, @RequestBody ProspectRequest prospectRequest){
        return ResponseEntity.ok(
                prospectService.updateProspect(id,prospectRequest)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProspect(@PathVariable Long id){
        return ResponseEntity.ok(
                prospectService.deleteProspect(id)
        );
    }

    @PostMapping("/convert/{id}")
    public ResponseEntity<String> convertToClient(@PathVariable Long id){
        return ResponseEntity.ok(
                prospectService.convertToClient(id)
        );
    }
}
