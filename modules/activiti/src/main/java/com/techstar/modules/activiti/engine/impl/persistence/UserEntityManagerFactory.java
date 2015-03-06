package com.techstar.modules.activiti.engine.impl.persistence;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;

import com.techstar.modules.activiti.engine.impl.persistence.entity.CustomUserEntityManger;

public class UserEntityManagerFactory implements SessionFactory {

	private CustomUserEntityManger userEntityManger;

	public UserEntityManagerFactory(CustomUserEntityManger userEntityManger) {
		this.userEntityManger = userEntityManger;
	}

	@Override
	public Class<?> getSessionType() {
		return UserIdentityManager.class;
	}

	@Override
	public Session openSession() {
		return userEntityManger;
	}

}
