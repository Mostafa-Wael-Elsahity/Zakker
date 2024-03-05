package com.example.elearningplatform.user;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.address.Address;
import com.example.elearningplatform.address.AddressRepository;
import com.example.elearningplatform.base.BaseRepository;
import com.example.elearningplatform.role.Role;
import com.example.elearningplatform.role.RoleService;
import com.example.elearningplatform.signup.SignUpRequest;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserService extends BaseRepository {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RoleService roleService;

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
    public User findByEmail(String email) {
        String sql = "SELECT * from  users where email =? ";
        List<User> users = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper(User.class), email);
        if (users.isEmpty()) {
            return null;
        }
        List<Role> roles = roleService.getRolesByUserId(users.get(0).getId());
        users.get(0).setRoles(roles);

        return users.get(0);
    }

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
    public List<User> findCourseInstructors(Integer courseId) {
        String sql = "SELECT * FROM users " +
                " inner JOIN instructed_courses ON users.id=instructed_courses.user_id" +
                " inner Join course ON course.id=instructed_courses.course_id WHERE course.id = ?";

        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class), courseId);
        return users;
    }

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
    @Transactional
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
        save(user);

        Role role = roleService.getByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist();
        }

        user.setRoles(List.of(role));

        save(user);
        Address address = Address.builder().user(user).city(request.getCity()).country(request.getCountry())
                .street(request.getStreet()).state(request.getState()).zipCode(request.getZipCode()).build();
        addressRepository.save(address);

        return user;
    }

    /********************************************************************************************************* */
    @Transactional
    public Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleService.saveRole(role);
    }

}
