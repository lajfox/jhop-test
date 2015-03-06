package com.techstar.hessian.example.service;

import com.techstar.hessian.example.entity.Leave;
import com.techstar.modules.springframework.data.jpa.service.MyJpaService;

public interface LeaveService extends MyJpaService<Leave, String> {

	void saveLeave(Leave leave);
}
