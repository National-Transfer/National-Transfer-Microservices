package com.ensa.kycservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class CustomerProfileDto {
    protected long id;

    protected String title;

    protected String firstName;

    protected String identityType;

    protected String countryOfIssue;

    protected String identityNumber;

    protected Date identityValidity;

    protected Date dateOfBirth;

    protected String profession;

    protected String nationality;

    protected String countryOfAddress;

    protected String address;

    protected String city;

    protected String phoneNumber;

    protected String email;
}
