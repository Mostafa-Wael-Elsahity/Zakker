package com.example.elearningplatform.user;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.address.Address;
import com.example.elearningplatform.address.AddressRepository;
import com.example.elearningplatform.signup.SignUpRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final PasswordEncoder passwordEncoder;

    /********************************************************************************************************* */
    // public List<User> getCourseInstructors(String searchKey) {
    // String sql = "SELECT * FROM users "
    // +
    // " JOIN instructed_courses ON users.id=instructed_courses.user_id" +
    // " Join course ON course.id=instructed_courses.course_id WHERE course.id = 1";
    // @SuppressWarnings({ "rawtypes", "unchecked" })
    // List<User> users = jdbcTemplate.queryForList(sql, new Object[] { id }, new
    // BeanPropertyRowMapper(User.class));
    // return users;
    // }

    /********************************************************************************************************* */
    // public List<User> getAllUsers() {
    // String sql = "SELECT * from users";
    // List<User> users = jdbcTemplate.query(
    // sql,
    // new BeanPropertyRowMapper(User.class));

    // return users;
    // }

    /********************************************************************************************************* */

    /********************************************************************************************************* */
    // public void saveUser(User user) {
    // String sql = "INSERT INTO users (email, password, age, bio, first_name,
    // last_name, phone_number, registration_date) "
    // + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    // jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getAge(),
    // user.getBio(),
    // user.getFirstName(), user.getLastName(), user.getPhoneNumber(),
    // user.getRegistrationDate());
    // }
    // /*********************************************************************************************************
    // */
    // @Transactional
    // public void saveUser(User user) {
    // entityManager.persist(user);

    // }

    /********************************************************************************************************* */
    // @Transactional
    // public void updateUser(User user) {
    // entityManager.merge(user);
    // }

    /********************************************************************************************************* */

    /********************************************************************************************************* */

    /********************************************************************************************************* */

    // public User getUserByUserId(Integer id) {

    // String sql = "SELECT * from users WHERE id =?";

    // List<User> users = jdbcTemplate.query(
    // sql,
    // new BeanPropertyRowMapper(User.class), id);
    // if (users.isEmpty()) {
    // return null;

    // }

    // return users.get(0);
    // }

    /********************************************************************************************************* */

    public User saveUser(SignUpRequest request) throws SQLException, IOException {

        User user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .bio(request.getBio()).firstName(request.getFirstName()).lastName(request.getLastName())
                .enabled(false)
                .phoneNumber(request.getPhoneNumber()).registrationDate(LocalDateTime.now()).build();
        if (request.getProfilePicture() == null) {
            user.setProfilePicture(null);
        } else {
            byte[] bytes = request.getProfilePicture().getBytes();
            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
            user.setProfilePicture(blob);
        }
        userRepository.save(user);

        // if (role == null) {
        // role = checkRoleExist();
        // }

        user.setRoles(List.of(Role.ROLE_USER));

        userRepository.save(user);
        Address address = Address.builder().user(user).city(request.getCity()).country(request.getCountry())
                .street(request.getStreet()).state(request.getState()).zipCode(request.getZipCode()).build();
        addressRepository.save(address);

        return user;
    }

    /********************************************************************************************************* */
    // @Transactional
    // public Role checkRoleExist() {
    // Role role = new Role();
    // role.setName("ROLE_USER");
    // return roleRepository.save(role);
    // }

}
