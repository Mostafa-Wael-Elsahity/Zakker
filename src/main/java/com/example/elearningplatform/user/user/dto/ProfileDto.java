package com.example.elearningplatform.user.user.dto;

import java.time.LocalDateTime;

import com.example.elearningplatform.user.address.Address;
import com.example.elearningplatform.user.address.AddressDto;
import com.example.elearningplatform.user.user.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileDto {

    private String email;

    private String firstName;

    private String lastName;
    private String imageUrl;
    private String paypalEmail;
    private String phoneNumber;


    private Boolean enabled;

    private LocalDateTime registrationDate;

    private String about;

    private Integer age;
    private AddressDto address;

    // private AddressDto address;

    public ProfileDto(User user, Address address) {

        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.imageUrl = user.getImageUrl();
        this.phoneNumber = user.getPhoneNumber();
        this.enabled = user.isEnabled();
        this.registrationDate = user.getRegistrationDate();
        this.about = user.getAbout();
        this.age = user.getAge();
        this.paypalEmail = user.getPaypalEmail();

        this.address = new AddressDto(user.getAddress());

    }

}
