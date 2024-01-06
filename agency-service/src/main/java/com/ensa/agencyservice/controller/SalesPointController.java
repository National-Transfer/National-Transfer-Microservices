package com.ensa.agencyservice.controller;

import com.ensa.agencyservice.dto.request.SalesPointRequestDto;
import com.ensa.agencyservice.dto.response.AgentResponseDto;
import com.ensa.agencyservice.dto.response.SalesPointResponseDto;
import com.ensa.agencyservice.service.SalesPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/salesPoints")
public class SalesPointController {
    private final SalesPointService salesPointService;

    @PostMapping
    public ResponseEntity<SalesPointResponseDto> createSalesPoint(@RequestBody @Validated SalesPointRequestDto salesPointRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(salesPointService.createSalesPoint(salesPointRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<SalesPointResponseDto>> getAllSalesPoints(){
        return ResponseEntity.ok(salesPointService.getAllSalesPoints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPointResponseDto> getSalesPointById(@PathVariable String id){
        return ResponseEntity.ok(salesPointService.getSalesPointById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesPointResponseDto> updateSalesPoint(@PathVariable String id, @RequestBody @Validated SalesPointRequestDto salesPointRequestDto){
        return ResponseEntity.ok(salesPointService.updateSalesPoint(id, salesPointRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesPointById(@PathVariable String id){
        salesPointService.deleteSalesPointById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/agents")
    public ResponseEntity<List<AgentResponseDto>> getSalesPointAgents(@PathVariable String id){
        return ResponseEntity.ok(salesPointService.getSalesPointAgents(id));
    }
}
