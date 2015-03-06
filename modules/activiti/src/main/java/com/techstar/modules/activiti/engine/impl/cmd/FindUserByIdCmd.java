package com.techstar.modules.activiti.engine.impl.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * 根据用户查询group
 * @author sundoctor
 *
 */
public class FindUserByIdCmd implements Command<UserEntity>, Serializable {

	private static final long serialVersionUID = -32852191784946868L;
	protected String userId;

	public FindUserByIdCmd(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new ActivitiIllegalArgumentException("userId is null");
		}
		this.userId = userId;
	}

	@Override
	public UserEntity execute(CommandContext commandContext) {
		return commandContext.getUserIdentityManager().findUserById(userId);
	}

}
