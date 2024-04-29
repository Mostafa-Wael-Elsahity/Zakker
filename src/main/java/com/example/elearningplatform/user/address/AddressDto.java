package com.example.elearningplatform.user.address;

import lombok.Data;

@Data
public class AddressDto {


    private Integer id;


    private String street;


    private String city;


    private String state;


    private String country;


    private String zipCode;
    public AddressDto(Address address) {
        if (address == null) {
            return;
        }
        this.id = address.getId();
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
        this.country = address.getCountry();
        this.zipCode = address.getZipCode();
    }
}
