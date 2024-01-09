package com.ensa.agencyservice.service.impl;

import com.ensa.agencyservice.dto.request.AccountRequestDto;
import com.ensa.agencyservice.dto.request.AgentRequestDto;
import com.ensa.agencyservice.dto.response.AccountResponseDto;
import com.ensa.agencyservice.dto.response.AgentResponseDto;
import com.ensa.agencyservice.entity.AgentEntity;
import com.ensa.agencyservice.entity.SalesPointEntity;
import com.ensa.agencyservice.repository.AgentRepository;
import com.ensa.agencyservice.repository.SalesPointRepository;
import com.ensa.agencyservice.service.http.AccountFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.okta.sdk.client.Client;
import com.okta.sdk.resource.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AgentServiceImplTest {

    @Mock
    private AgentRepository agentRepository;
    @Mock
    private SalesPointRepository salesPointRepository;
    @Mock
    private AccountFeignClient accountFeignClient;
    @InjectMocks
    private AgentServiceImpl agentService;



    @Test
    void getAllAgents () {
        List<AgentEntity> agentEntities = Arrays.asList(new AgentEntity(), new AgentEntity());
        when(agentRepository.findAll()).thenReturn(agentEntities);

        List<AgentResponseDto> result = agentService.getAllAgents();

        assertNotNull(result);
        assertEquals(agentEntities.size(), result.size());
    }

    @Test
    void getAgentById () {
        AgentEntity agentEntity = new AgentEntity();
        agentEntity.setId(UUID.randomUUID());
        when(agentRepository.findById(any(UUID.class))).thenReturn(Optional.of(agentEntity));
        when(accountFeignClient.getAccountByOwnerId(anyString())).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(new AccountResponseDto()));

        AgentResponseDto result = agentService.getAgentById(UUID.randomUUID().toString());

        assertNotNull(result);
    }

}