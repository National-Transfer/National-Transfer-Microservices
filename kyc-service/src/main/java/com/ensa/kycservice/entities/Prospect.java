package com.ensa.kycservice.entities;


<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonManagedReference;
=======
>>>>>>> f59c017d22b4369fb6245b239806423bfe5f3d0b
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
<<<<<<< HEAD
@EqualsAndHashCode(exclude = {"beneficiaries"}, callSuper = true)
=======
@EqualsAndHashCode(callSuper = true)
>>>>>>> f59c017d22b4369fb6245b239806423bfe5f3d0b
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@Entity
public class Prospect extends CustomerProfile {

<<<<<<< HEAD
    @OneToMany(mappedBy = "prospect", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
=======
    @OneToMany(mappedBy = "prospect", cascade = CascadeType.PERSIST)
>>>>>>> f59c017d22b4369fb6245b239806423bfe5f3d0b
    private Set<Beneficiary> beneficiaries;
}
