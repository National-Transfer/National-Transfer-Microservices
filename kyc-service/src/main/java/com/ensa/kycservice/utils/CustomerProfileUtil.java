package com.ensa.kycservice.utils;

import com.ensa.kycservice.dto.CustomerProfileDto;
import com.ensa.kycservice.entities.CustomerProfile;
import com.okta.sdk.resource.user.UserProfile;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class CustomerProfileUtil {

    public static void updateCustomerProfile(CustomerProfile profile, CustomerProfileDto request) {
        if (request.getTitle() != null) {
            profile.setTitle(request.getTitle());
        }
        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getIdentityType() != null) {
            profile.setIdentityType(request.getIdentityType());
        }
        if (request.getCountryOfIssue() != null) {
            profile.setCountryOfIssue(request.getCountryOfIssue());
        }
        if (request.getIdentityNumber() != null) {
            profile.setIdentityNumber(request.getIdentityNumber());
        }
        if (request.getIdentityValidity() != null) {
            profile.setIdentityValidity(request.getIdentityValidity());
        }
        if (request.getDateOfBirth() != null) {
            profile.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getProfession() != null) {
            profile.setProfession(request.getProfession());
        }
        if (request.getNationality() != null) {
            profile.setNationality(request.getNationality());
        }
        if (request.getCountryOfAddress() != null) {
            profile.setCountryOfAddress(request.getCountryOfAddress());
        }
        if (request.getAddress() != null) {
            profile.setAddress(request.getAddress());
        }
        if (request.getCity() != null) {
            profile.setCity(request.getCity());
        }
        if (request.getPhoneNumber() != null) {
            profile.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getEmail() != null) {
            profile.setEmail(request.getEmail());
        }
    }

    public static void updateOktaProfile(UserProfile profile, CustomerProfileDto request) {
        if (request.getTitle() != null) {
            profile.setFirstName(request.getTitle());
        }
        if (request.getFirstName() != null) {
            profile.setLastName(request.getFirstName());
        }
        if (request.getPhoneNumber() != null) {
            profile.setMobilePhone(request.getPhoneNumber());
        }
        if (request.getEmail() != null) {
            profile.setEmail(request.getEmail());
        }
    }
}
