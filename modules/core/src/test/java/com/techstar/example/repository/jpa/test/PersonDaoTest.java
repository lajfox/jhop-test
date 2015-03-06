package com.techstar.example.repository.jpa.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.techstar.example.repository.jpa.PersonDao;
import com.techstar.modules.springframework.test.SpringTransactionalTestCase;

/**
 * org.springframework.data.repository.Repository接口测试 org.springframework.data.jpa.repository.JpaSpecificationExecutor接口测试
 * 
 * @author sundoctor
 * 
 */
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class PersonDaoTest extends SpringTransactionalTestCase {

	@Autowired
	private PersonDao personDao;
	

	@Test
	public void findByTeam() {
		personDao.findByTeam("1");
	}	

}
