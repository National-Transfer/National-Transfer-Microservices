package com.ensa.kycservice.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
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
public class Client extends CustomerProfile{

    public Client(Prospect prospect){
        this.title = prospect.getTitle();
        this.firstName = prospect.getFirstName();
        this.identityType = prospect.getIdentityType();
        this.countryOfIssue = prospect.getCountryOfIssue();
        this.identityNumber = prospect.getIdentityNumber();
        this.identityValidity = prospect.getIdentityValidity();
        this.dateOfBirth = prospect.getDateOfBirth();
        this.profession = prospect.getProfession();
        this.nationality = prospect.getNationality();
        this.countryOfAddress = prospect.getCountryOfAddress();
        this.address = prospect.getAddress();
        this.city = prospect.getCity();
        this.phoneNumber = prospect.getPhoneNumber();
        this.email = prospect.getEmail();
    }

    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private Set<Beneficiary> beneficiaries;
}
