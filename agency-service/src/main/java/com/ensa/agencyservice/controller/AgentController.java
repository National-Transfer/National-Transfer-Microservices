package com.ensa.agencyservice.controller;

import com.ensa.agencyservice.dto.request.AgentRequestDto;
import com.ensa.agencyservice.dto.response.AgentResponseDto;
import com.ensa.agencyservice.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @PostMapping
    public ResponseEntity<AgentResponseDto> createAgent(@RequestBody @Validated AgentRequestDto agentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agentService.createAgent(agentRequestDto));
    }


    @GetMapping
    public ResponseEntity<List<AgentResponseDto>> getAllAgents() {
        return ResponseEntity.ok(agentService.getAllAgents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentResponseDto> getAgentById(@PathVariable String id) {
        return ResponseEntity.ok(agentService.getAgentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgentById(@PathVariable String id) {
        agentService.deleteAgentById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentResponseDto> updateAgentById(@PathVariable String id, @RequestBody AgentRequestDto agentRequestDto){
        return ResponseEntity.ok(agentService.updateAgentById(id, agentRequestDto));
    }


}
