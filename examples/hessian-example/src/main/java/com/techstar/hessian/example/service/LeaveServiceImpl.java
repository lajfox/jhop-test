package com.techstar.hessian.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hessian.example.entity.Leave;
import com.techstar.hessian.example.repository.jpa.LeaveDao;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.MyJpaServiceImpl;

@Service("leaveService")
@Transactional(readOnly = true)
public class LeaveServiceImpl extends MyJpaServiceImpl<Leave, String> implements LeaveService {

	@Autowired
	private LeaveDao leaveDao;

	@Override
	protected MyJpaRepository<Leave, String> getMyJpaRepository() {
		return leaveDao;
	}

	@Transactional(readOnly = false)
	public void saveLeave(Leave leave) {
		this.save(leave);
	}

}
