package com.techstar.modules.activiti.engine.impl;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import com.techstar.modules.activiti.engine.CustomIdentityService;
import com.techstar.modules.activiti.engine.impl.cmd.FindGroupsByUserCmd;
import com.techstar.modules.activiti.engine.impl.cmd.FindUserByIdCmd;

public class CustomIdentityServiceImpl extends ServiceImpl implements CustomIdentityService{

	@Override	
	public UserEntity findUserById(String userId)  {
		return commandExecutor.execute(new FindUserByIdCmd(userId));
	}
	
	@Override	
	public List<Group> findGroupsByUser(String userId) {
		return commandExecutor.execute(new FindGroupsByUserCmd(userId));
	}
}
