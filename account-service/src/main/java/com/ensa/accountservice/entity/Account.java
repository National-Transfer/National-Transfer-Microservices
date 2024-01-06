package com.ensa.accountservice.entity;

import com.ensa.accountservice.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountCode;
    private BigDecimal balance = new BigDecimal(0);
    private BigDecimal annualAmountTransfer = new BigDecimal(0);
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Long ownerId;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

}
