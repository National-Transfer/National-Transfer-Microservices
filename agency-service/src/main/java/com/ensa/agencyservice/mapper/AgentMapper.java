package com.ensa.agencyservice.mapper;

import com.ensa.agencyservice.dto.request.AgentRequestDto;
import com.ensa.agencyservice.dto.response.AccountResponseDto;
import com.ensa.agencyservice.dto.response.AgentResponseDto;
import com.ensa.agencyservice.entity.AgentEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {SalesPointMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AgentMapper {
    AgentMapper INSTANCE = Mappers.getMapper(AgentMapper.class);


    AgentEntity toEntity(AgentRequestDto agentRequestDto);


    @Mapping(source = "account", target = "account")
    @Mapping(source = "agentEntity.salesPoint", target = "salesPoint")
    @Mapping(source = "agentEntity.id", target = "id")
    AgentResponseDto toResponseDto(AgentEntity agentEntity, AccountResponseDto account);

    void updateEntity(AgentRequestDto agentRequestDto, @MappingTarget AgentEntity agentEntity);
}
