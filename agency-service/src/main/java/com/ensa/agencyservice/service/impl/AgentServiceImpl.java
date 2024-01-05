package com.ensa.agencyservice.service.impl;

import com.ensa.agencyservice.dto.request.AccountRequestDto;
import com.ensa.agencyservice.dto.request.AgentRequestDto;
import com.ensa.agencyservice.dto.response.AccountResponseDto;
import com.ensa.agencyservice.dto.response.AgentResponseDto;
import com.ensa.agencyservice.entity.AgentEntity;
import com.ensa.agencyservice.entity.SalesPointEntity;
import com.ensa.agencyservice.exception.ResourceNotCreatedException;
import com.ensa.agencyservice.exception.ResourceNotFoundException;
import com.ensa.agencyservice.mapper.AgentMapper;
import com.ensa.agencyservice.repository.AgentRepository;
import com.ensa.agencyservice.repository.SalesPointRepository;
import com.ensa.agencyservice.service.AgentService;
import com.ensa.agencyservice.service.http.AccountFeignClient;
import com.okta.sdk.client.Client;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;
    private final SalesPointRepository salesPointRepository;
    private final AccountFeignClient accountFeignClient;
    private final Client client;

    @Override
    public AgentResponseDto createAgent(AgentRequestDto agentRequestDto) {
        SalesPointEntity salesPointEntity = salesPointRepository.findById(UUID.fromString(agentRequestDto.getSalesPointId())).orElseThrow(() -> new ResourceNotFoundException("sales point with id : " + agentRequestDto.getSalesPointId() + " not found"));

        AgentEntity agentEntity = AgentMapper.INSTANCE.toEntity(agentRequestDto);
        agentEntity.setSalesPoint(salesPointEntity);
        agentEntity.setId(UUID.randomUUID());

        AccountResponseDto accountResponseDto = accountFeignClient.createAccount(
                AccountRequestDto.builder()
                        .ownerId(agentEntity.getId().toString())
                        .build()
        ).getBody();

        if (accountResponseDto == null) {
            throw new ResourceNotCreatedException("agent not created ! try again later .");
        }
        
        User agentOkta = UserBuilder.instance()
                .setGroups("00ge6s3a1wYF8RD3g5d7") // AGENT GROUP ID
                .setFirstName(agentEntity.getFirstName())
                .setLastName(agentEntity.getLastName())
                .setEmail(agentEntity.getEmail())
                .setActive(true)
                .setMobilePhone(agentEntity.getPhoneNumber())
                .buildAndCreate(client);

        return AgentMapper.INSTANCE.toResponseDto(agentRepository.save(agentEntity), accountResponseDto);
    }

    @Override
    public List<AgentResponseDto> getAllAgents() {
        return agentRepository.findAll().stream().map(agentEntity -> AgentMapper.INSTANCE.toResponseDto(agentEntity, null)).toList();
    }

    @Override
    public AgentResponseDto getAgentById(String id) {
        return agentRepository.findById(UUID.fromString(id)).map(agentEntity -> {
            AccountResponseDto account = accountFeignClient.getAccountByOwnerId(agentEntity.getId().toString()).getBody();
            return AgentMapper.INSTANCE.toResponseDto(agentEntity, account);
        }).orElseThrow(() -> new ResourceNotFoundException("agent with id: "+ id + " not found"));
    }

    @Override
    public void deleteAgentById(String id) {
        AgentEntity agentEntity = agentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("agent with id: " + id + "not found"));
        accountFeignClient.deleteAccountByOwnerId(agentEntity.getId().toString());
        agentRepository.deleteById(UUID.fromString(id));
    }
}
