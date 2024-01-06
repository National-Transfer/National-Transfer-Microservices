package com.ensa.agencyservice.service;

import com.ensa.agencyservice.dto.request.AgentRequestDto;
import com.ensa.agencyservice.dto.response.AgentResponseDto;

import java.util.List;

public interface AgentService {

    AgentResponseDto createAgent(AgentRequestDto agentRequestDto);
    List<AgentResponseDto> getAllAgents();
    AgentResponseDto getAgentById(String id);
    void deleteAgentById(String id);
    AgentResponseDto updateAgentById(String id, AgentRequestDto agentRequestDto);
}
