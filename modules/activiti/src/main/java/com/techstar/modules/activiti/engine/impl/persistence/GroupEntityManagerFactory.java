package com.techstar.modules.activiti.engine.impl.persistence;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;

import com.techstar.modules.activiti.engine.impl.persistence.entity.CustomGroupEntityManager;

public class GroupEntityManagerFactory implements SessionFactory {

	private CustomGroupEntityManager groupEntityManager;

	public GroupEntityManagerFactory(CustomGroupEntityManager groupEntityManager) {
		this.groupEntityManager = groupEntityManager;
	}

	@Override
	public Class<?> getSessionType() {
		return GroupIdentityManager.class;
	}

	@Override
	public Session openSession() {
		return groupEntityManager;
	}

}
