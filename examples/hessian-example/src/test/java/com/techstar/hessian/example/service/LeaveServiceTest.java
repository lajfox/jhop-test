package com.techstar.hessian.example.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.techstar.hessian.example.entity.Leave;
import com.techstar.modules.springframework.test.SpringContextTestCase;

@ContextConfiguration(locations = { "classpath:spring/hessian-client.xml" })
public class LeaveServiceTest extends SpringContextTestCase {

	@Autowired
	private LeaveService leaveService;

	@Test
	public void save() {

		Leave leave = new Leave();
		leave.setEndTime(new Date());
		leave.setLeaveType("公休");
		leave.setReason("请假原因");
		leave.setStartTime(new Date());

		leaveService.save(leave);
	}

	@Test
	public void delete() {

		leaveService.deleteAll();
	}

	@Test
	public void findAll() {
		List<Leave> list = leaveService.findBy("leaveType", "aaa");
	}
}
