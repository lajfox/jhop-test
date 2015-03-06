package com.techstar.modules.activiti.engine.impl.persistence.entity;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.techstar.modules.activiti.spring.SpringProcessEngineConfiguration;
import com.techstar.modules.springframework.test.SpringTransactionalTestCase;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class CustomUserEntityManagerTest extends SpringTransactionalTestCase {

	@Autowired
	private IdentityService identityService;

	@Autowired
	private SpringProcessEngineConfiguration processEngineConfiguration;

	@Test
	public void findUserById() {

		//UserEntity count = processEngineConfiguration.getCustomIdentityService().findUserById("admin");

		long count = identityService.createNativeUserQuery().sql("select count(*) from SS_USER where LOGIN_NAME = #{id}").parameter("id", "admin").count();
		Assert.assertTrue(count > 0);
		
		//User users = identityService.createNativeUserQuery().sql("select name as FIRST_ from SS_USER where LOGIN_NAME = #{id}").parameter("id", "admin").singleResult();
		
		List<Group> list = processEngineConfiguration.getCustomIdentityService().findGroupsByUser("admin");
		Assert.assertNotNull(list);

		boolean b = identityService.checkPassword("admin", "691b14d79bf0fa2215f155235df5e670b64394cc");
		Assert.assertTrue(b);
	}

}
