package com.techstar.modules.activiti.engine;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.UserEntity;

/**
 * 
 * @author sundoctor
 *
 */
public interface CustomIdentityService {

	UserEntity findUserById(String userId);
	
	List<Group> findGroupsByUser(String userId);
}
