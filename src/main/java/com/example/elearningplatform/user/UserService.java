package com.example.elearningplatform.user;

import com.example.elearningplatform.address.Address;
import com.example.elearningplatform.address.AddressRepository;
import com.example.elearningplatform.role.Role;
import com.example.elearningplatform.role.RoleRepository;
import com.example.elearningplatform.signup.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service

public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AddressRepository addressRepository;

    public User saveUser(SignUpRequest request) throws SQLException, IOException {

        User user = User.builder().email(request.getEmail()).password(request.getPassword()).age(request.getAge())
                .bio(request.getBio()).firstName(request.getFirstName()).lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber()).registrationDate(LocalDateTime.now()).build();

        Role role = roleRepository.findByName("ROLE_USER");

        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));

        if (request.getProfilePicture() == null) {
            user.setProfilePicture(null);
        } else {
            byte[] bytes = request.getProfilePicture().getBytes();
            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
            user.setProfilePicture(blob);
        }

        userRepository.save(user);

        Address address = Address.builder().user(user).city(request.getCity()).country(request.getCountry())
                .street(request.getStreet()).state(request.getState()).zipCode(request.getZipCode()).build();
        addressRepository.save(address);

        return user;
    }

    public Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

}
