package com.techstar.modules.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.apache.commons.lang3.StringUtils;

/**
 * 根据用户查询group
 * @author sundoctor
 *
 */
public class FindGroupsByUserCmd implements Command<List<Group>>, Serializable {

	private static final long serialVersionUID = -32852191784946868L;
	protected String userId;

	public FindGroupsByUserCmd(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new ActivitiIllegalArgumentException("userId is null");
		}
		this.userId = userId;
	}

	@Override
	public List<Group> execute(CommandContext commandContext) {
		return commandContext.getGroupIdentityManager().findGroupsByUser(userId);
	}

}
