package com.ensa.kycservice.controllers;


import com.ensa.kycservice.dto.ProspectRequest;
import com.ensa.kycservice.dto.Response;
import com.ensa.kycservice.services.ProspectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor

@RestController
@RequestMapping("/prospects")
public class ProspectController {

    private final ProspectService prospectService;

    @GetMapping("/{identityNumber}")
    public ResponseEntity<Response> getProspect(@PathVariable String identityNumber){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Prospect retrieved with identity number : " + identityNumber)
                        .data(Map.of("prospect", prospectService.getProspect(identityNumber)))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<Response> getAllProspects(){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("All prospects retrieved ")
                        .data(Map.of("prospects", prospectService.getAllProspects()))
                        .build()
        );
    }

    @GetMapping("/exists/{identityNumber}")
    public ResponseEntity<Response> prospectExists(@PathVariable String identityNumber) {
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Prospect existence check completed")
                        .data(Map.of("exists", prospectService.prospectExists(identityNumber)))
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<Response> saveProspect(@RequestBody ProspectRequest prospectRequest){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(prospectService.saveProspect(prospectRequest))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateProspect(@PathVariable Long id, @RequestBody ProspectRequest prospectRequest){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(prospectService.updateProspect(id,prospectRequest))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteProspect(@PathVariable Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(prospectService.deleteProspect(id))
                        .build()
        );
    }

    @PostMapping("/convert/{id}")
    public ResponseEntity<Response> convertToClient(@PathVariable Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message(prospectService.convertToClient(id))
                        .build()
        );
    }
}
