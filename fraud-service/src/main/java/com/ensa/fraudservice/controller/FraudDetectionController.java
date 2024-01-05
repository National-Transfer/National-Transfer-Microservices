package com.ensa.fraudservice.controller;

import com.ensa.fraudservice.dto.ClientDto;
import com.ensa.fraudservice.entities.FraudDetection;
import com.ensa.fraudservice.service.FraudDetectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/blacklistedClients")
@RestController
@AllArgsConstructor
public class FraudDetectionController {
    private final FraudDetectionService fraudDetectionService;

    @GetMapping("{clientId}")
    public ResponseEntity<Boolean> checkClientBlacklisted(@PathVariable Long clientId) {
        return ResponseEntity.ok(fraudDetectionService.checkClientBlacklisted(clientId));
    }

    @PostMapping
    public ResponseEntity<FraudDetection> addBlacklistedClient(@RequestBody ClientDto clientdto) {
        return ResponseEntity.ok(fraudDetectionService.createBlacklistedClient(clientdto));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Boolean> deleteAccountByOwnerId(@PathVariable Long clientId) {
        return ResponseEntity.ok(fraudDetectionService.deletedBlacklistedClient(clientId));
    }
}
