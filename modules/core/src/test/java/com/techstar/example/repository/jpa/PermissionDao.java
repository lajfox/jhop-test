package com.techstar.example.repository.jpa;

import com.techstar.example.entity.Permission;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface PermissionDao extends MyJpaRepository<Permission, String> {

}
