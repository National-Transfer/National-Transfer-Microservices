package com.ensa.kycservice.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;


@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@Entity
public class Prospect extends CustomerProfile{

    @OneToMany(mappedBy = "prospect")
    private Set<Beneficiary> beneficiaries;
}
