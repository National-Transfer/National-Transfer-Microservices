package com.ensa.agencyservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class SalesPointEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String address;

    private BigDecimal dailyTransferLimit;

    private String phoneNumber;

    @OneToMany(mappedBy = "salesPoint",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DepositEntity> depositEntityHistory = new ArrayList<>();

    @OneToMany(mappedBy = "salesPoint", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AgentEntity> agents = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
