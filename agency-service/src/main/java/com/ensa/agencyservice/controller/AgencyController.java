package com.ensa.agencyservice.controller;

import com.ensa.agencyservice.dto.AgencyDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/agencies")
public class AgencyController {

    private List<AgencyDto> agencies = Arrays.asList(
      new AgencyDto("wafacash1"),
      new AgencyDto("wafacash2"),
      new AgencyDto("wafacash3")
    );


    @GetMapping
    public ResponseEntity<List<AgencyDto>> agencies(){
        return ResponseEntity.ok(agencies);
    }
}
