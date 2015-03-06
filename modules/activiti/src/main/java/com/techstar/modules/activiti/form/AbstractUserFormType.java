package com.techstar.modules.activiti.form;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.identity.User;

public abstract class AbstractUserFormType extends AbstractFormType {

	// Check if user exists
	protected void checkUsers(final String... ids) {
		for (String id : ids) {
			long count = ProcessEngines.getDefaultProcessEngine().getIdentityService().createNativeUserQuery()
					.sql("select count(*) from SS_USER where LOGIN_NAME = #{id}").parameter("id", id).count();

			if (count <= 0) {
				throw new ActivitiObjectNotFoundException("用户： " + id + " 不存在", User.class);
			}
		}
	}
}
