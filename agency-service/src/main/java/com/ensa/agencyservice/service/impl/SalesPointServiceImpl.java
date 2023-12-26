package com.ensa.agencyservice.service.impl;

import com.ensa.agencyservice.dto.request.SalesPointRequestDto;
import com.ensa.agencyservice.dto.response.AgentResponseDto;
import com.ensa.agencyservice.dto.response.SalesPointResponseDto;
import com.ensa.agencyservice.entity.SalesPointEntity;
import com.ensa.agencyservice.exception.ResourceNotFoundException;
import com.ensa.agencyservice.mapper.AgentMapper;
import com.ensa.agencyservice.mapper.SalesPointMapper;
import com.ensa.agencyservice.repository.SalesPointRepository;
import com.ensa.agencyservice.service.SalesPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesPointServiceImpl implements SalesPointService {
    private final SalesPointRepository salesPointRepository;

    @Override
    public SalesPointResponseDto createSalesPoint(SalesPointRequestDto salesPointRequestDto) {
        SalesPointEntity salesPointEntity = SalesPointMapper.INSTANCE.toEntity(salesPointRequestDto);
        return SalesPointMapper.INSTANCE.toResponseDto(salesPointRepository.save(salesPointEntity));
    }

    @Override
    public List<SalesPointResponseDto> getAllSalesPoints() {
        return salesPointRepository.findAll().stream().map(SalesPointMapper.INSTANCE::toResponseDto).toList();
    }

    @Override
    public SalesPointResponseDto getSalesPointById(String id) {
        return salesPointRepository.findById(UUID.fromString(id)).map(SalesPointMapper.INSTANCE::toResponseDto).orElseThrow(() -> new ResourceNotFoundException("salesPoint with id: " + id + "not found"));
    }

    @Override
    public SalesPointResponseDto updateSalesPoint(String id, SalesPointRequestDto salesPointRequestDto) {
        SalesPointEntity salesPointEntity = salesPointRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("SalesPoint with id: " + id + "not found"));

        SalesPointMapper.INSTANCE.updateEntity(salesPointRequestDto, salesPointEntity);
        return SalesPointMapper.INSTANCE.toResponseDto(salesPointRepository.save(salesPointEntity));
    }

    @Override
    public void deleteSalesPointById(String id) {
        salesPointRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public List<AgentResponseDto> getSalesPointAgents(String id) {
        SalesPointEntity salesPointEntity = salesPointRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ResourceNotFoundException("SalesPoint with id: " + id + "not found"));

        return salesPointEntity.getAgents().stream().map(agentEntity -> AgentMapper.INSTANCE.toResponseDto(agentEntity, null)).toList();
    }
}