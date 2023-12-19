package com.ensa.agencyservice.service;

import com.ensa.agencyservice.dto.request.SalesPointRequestDto;
import com.ensa.agencyservice.dto.response.AgentResponseDto;
import com.ensa.agencyservice.dto.response.SalesPointResponseDto;

import java.util.List;

public interface SalesPointService {
    SalesPointResponseDto createSalesPoint(SalesPointRequestDto salesPointRequestDto);

    List<SalesPointResponseDto> getAllSalesPoints();

    SalesPointResponseDto getSalesPointById(String id);

    SalesPointResponseDto updateSalesPoint(String id,SalesPointRequestDto salesPointRequestDto);

    void deleteSalesPointById(String id);

    List<AgentResponseDto> getSalesPointAgents(String id);
}
