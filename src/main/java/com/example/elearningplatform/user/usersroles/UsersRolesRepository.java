package com.example.elearningplatform.user.usersroles;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.example.elearningplatform.user.User;

public interface UsersRolesRepository extends JpaRepository<UsersRoles, Integer> {

    public List<UsersRoles> findByUser(User user);

}
