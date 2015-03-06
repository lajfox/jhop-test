package com.techstar.hessian.example.repository.jpa;

import com.techstar.hessian.example.entity.Leave;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface LeaveDao extends MyJpaRepository<Leave,String> {

}
