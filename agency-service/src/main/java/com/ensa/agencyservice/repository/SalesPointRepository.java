package com.ensa.agencyservice.repository;

import com.ensa.agencyservice.entity.SalesPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalesPointRepository extends JpaRepository<SalesPointEntity, UUID> {
}
