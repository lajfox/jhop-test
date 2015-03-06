package com.techstar.security.repository.jpa;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.security.entity.User;

public interface UserDao extends MyJpaRepository<User, String> {

}
