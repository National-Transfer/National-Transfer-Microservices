package com.ensa.kycservice.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@MappedSuperclass
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank(message = "Title is required")
    @Enumerated(EnumType.STRING)
    protected Title title;

    @NotBlank(message = "First name is required")
    protected String firstName;

    @NotBlank(message = "Identity type is required")
    protected String identityType;

    @NotBlank(message = "Country of issue is required")
    protected String countryOfIssue;

    @NotBlank(message = "Identity number is required")
    protected String identityNumber;

    @NotNull(message = "Identity validity date is required")
    @FutureOrPresent(message = "Identity validity date must be in the present or future")
    protected Date identityValidity;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    protected Date dateOfBirth;

    @NotBlank(message = "Profession is required")
    protected String profession;

    @NotBlank(message = "Nationality is required")
    protected String nationality;

    @NotBlank(message = "Country of address is required")
    protected String countryOfAddress;

    @NotBlank(message = "Address is required")
    protected String address;

    @NotBlank(message = "City is required")
    protected String city;

    @NotBlank(message = "Phone number is required")
    protected String phoneNumber;

    @Email(message = "Invalid email address")
    protected String email;

    @CreationTimestamp
    protected Date createdTime;

    @UpdateTimestamp
    protected Date lastUpdatedTime ;
}
