package com.ensa.agencyservice.repository;

import com.ensa.agencyservice.entity.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity,UUID> {
    List<AgentEntity> findAgentEntitiesBySalesPointId(UUID id);
}
