package com.ensa.kycservice.dto;

import com.ensa.kycservice.entities.Client;
import com.ensa.kycservice.entities.Prospect;
import com.ensa.kycservice.entities.Title;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClientRequest extends CustomerProfileRequest{

    public Client mapToClient(){
        return Client.builder()
                .title(Title.valueOf(this.getTitle().toUpperCase()))
                .firstName(this.getFirstName())
                .identityType(this.getIdentityType())
                .countryOfIssue(this.getCountryOfIssue())
                .identityNumber(this.getIdentityNumber())
                .identityValidity(this.getIdentityValidity())
                .dateOfBirth(this.getDateOfBirth())
                .profession(this.getProfession())
                .nationality(this.getNationality())
                .countryOfAddress(this.getCountryOfAddress())
                .address(this.getAddress())
                .city(this.getCity())
                .phoneNumber(this.getPhoneNumber())
                .email(this.getEmail())
                .build();

    }
}
