package com.example.elearningplatform.user.lists;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserListRepository extends JpaRepository<UserList, Integer>{
    List<UserList> findByUserId(Integer userId);
    
}
