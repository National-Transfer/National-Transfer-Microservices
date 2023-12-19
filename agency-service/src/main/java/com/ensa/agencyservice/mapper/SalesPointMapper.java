package com.ensa.agencyservice.mapper;

import com.ensa.agencyservice.dto.request.SalesPointRequestDto;
import com.ensa.agencyservice.dto.response.SalesPointResponseDto;
import com.ensa.agencyservice.entity.SalesPointEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SalesPointMapper {
    SalesPointMapper INSTANCE = Mappers.getMapper(SalesPointMapper.class);

    SalesPointEntity toEntity(SalesPointRequestDto salesPointRequestDto);

    SalesPointResponseDto toResponseDto(SalesPointEntity salesPointEntity);

    void updateEntity(SalesPointRequestDto salesPointRequestDto, @MappingTarget SalesPointEntity salesPointEntity);

}
