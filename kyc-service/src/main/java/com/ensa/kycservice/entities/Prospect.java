package com.ensa.kycservice.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;


@Data
@EqualsAndHashCode(exclude = {"beneficiaries"}, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@Entity
public class Prospect extends CustomerProfile {

    @OneToMany(mappedBy = "prospect", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Beneficiary> beneficiaries;
}
