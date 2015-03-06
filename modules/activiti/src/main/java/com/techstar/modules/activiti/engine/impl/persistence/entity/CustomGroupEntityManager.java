package com.techstar.modules.activiti.engine.impl.persistence.entity;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;

public class CustomGroupEntityManager extends GroupEntityManager {

	@Override
	@SuppressWarnings("unchecked")
	public List<Group> findGroupsByUser(String userId) {
		return getDbSqlSession().selectList("findGroupsByUser", userId);
	}
}
