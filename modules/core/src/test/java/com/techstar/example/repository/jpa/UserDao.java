package com.techstar.example.repository.jpa;

import com.techstar.example.entity.User;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface UserDao extends MyJpaRepository<User, String> {

}
