package com.example.elearningplatform.user.usersroles;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users_roles")
@NoArgsConstructor
@AllArgsConstructor
public class UsersRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String role;

    @OneToOne
    private User user;

    public UsersRoles(String role, User user) {
        this.role = role;
        this.user = user;
    }

}
