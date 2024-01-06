package com.ensa.kycservice.dto;

import com.ensa.kycservice.entities.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder

public class ClientResponse extends CustomerProfileDto{

   public ClientResponse(Client client, AccountResponseDto accountResponseDto) {
      this.id = client.getId();
      this.title = client.getTitle();
      this.firstName = client.getFirstName();
      this.identityType = client.getIdentityType();
      this.countryOfIssue = client.getCountryOfIssue();
      this.identityNumber = client.getIdentityNumber();
      this.identityValidity = client.getIdentityValidity();
      this.dateOfBirth = client.getDateOfBirth();
      this.profession = client.getProfession();
      this.nationality = client.getNationality();
      this.countryOfAddress = client.getCountryOfAddress();
      this.address = client.getAddress();
      this.city = client.getCity();
      this.phoneNumber = client.getPhoneNumber();
      this.email = client.getEmail();
      this.accountResponseDto = accountResponseDto;
   }

   @JsonProperty("account")
   private AccountResponseDto accountResponseDto;
}
