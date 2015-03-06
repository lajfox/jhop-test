package com.techstar.modules.activiti.engine.impl.persistence.entity;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;

public class CustomUserEntityManger extends UserEntityManager {

	@Override
	public UserEntity findUserById(String userId) {
		return (UserEntity) getDbSqlSession().selectOne("findUserById", userId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Group> findGroupsByUser(String userId) {
		return getDbSqlSession().selectList("findGroupsByUser", userId);
	}

}
