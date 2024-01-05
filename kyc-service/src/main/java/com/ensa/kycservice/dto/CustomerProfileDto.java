package com.ensa.kycservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerProfileRequest {
    private String title;

    private String firstName;

    private String identityType;

    private String countryOfIssue;

    private String identityNumber;

    private Date identityValidity;

    private Date dateOfBirth;

    private String profession;

    private String nationality;

    private String countryOfAddress;

    private String address;

    private String city;

    private String phoneNumber;

    private String email;
}
