package com.example.elearningplatform.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
@SuppressWarnings("rawtypes")
public class RoleService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Role> getRolesByUserId(Integer userId) {
        String sql = "SELECT * from  roles " +
                " join users_roles on roles.id = users_roles.role_id " +
                " join users on users.id = users_roles.user_id where users.id =?";
        List<Role> roles = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper(Role.class), userId);
        return roles;
    }

    public Role getByName(String name) {
        String sql = "SELECT * FROM roles WHERE name =? ";
        @SuppressWarnings("unchecked")
        List<Role> roles = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper(Role.class), name);
        if (roles.isEmpty()) {
            return null;

        }
        return roles.get(0);
    }

    @Transactional
    public Role saveRole(Role role) {
        entityManager.persist(role);
        return role;
    }

}
