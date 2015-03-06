package com.techstar.example.repository.jpa.test;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

import org.hibernate.transform.RootEntityResultTransformer;
import org.hibernate.transform.Transformers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.techstar.example.entity.Role;
import com.techstar.example.repository.jpa.RoleDao;
import com.techstar.modules.hibernate.transform.MyAliasToEntityMapResultTransformer;
import com.techstar.modules.springframework.test.SpringTransactionalTestCase;
import com.techstar.modules.utils.Identities;

/**
 * org.springframework.data.repository.Repository接口测试 org.springframework.data.jpa.repository.JpaSpecificationExecutor接口测试
 * 
 * @author sundoctor
 * 
 */
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class RoleDaoTest extends SpringTransactionalTestCase {

	@Autowired
	private RoleDao roleDao;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void findOne() {

		// JPQＬ查询
		Role role1 = roleDao.findOneByName1("Admin");
		Assert.assertNotNull(role1);

		Role role21 = roleDao.findOneByName2("Admin");
		Assert.assertNotNull(role21);

		Map<String, Object> role6 = roleDao.findOneByName3("Admin");
		Assert.assertNotNull(role6);

		Map<String, Object> role62 = roleDao.findOneByName3("Admin", MyAliasToEntityMapResultTransformer.INSTANCE);
		Assert.assertNotNull(role62);

		// 返回Map new map(x.name as name,x.id as id)
		Map<String, Object> role7 = roleDao.findOneByName4("Admin");
		Assert.assertNotNull(role7);

		// SQＬ查询
		Role role5 = roleDao.findOneByName5("Admin");
		Assert.assertNotNull(role5);

		Role role8 = roleDao.findOneByName6("Admin");
		Assert.assertNotNull(role8);

		Map<String, Object> role10 = roleDao.findOneByName7("Admin");
		Assert.assertNotNull(role10);

		Map<String, Object> role102 = roleDao.findOneByName7("Admin", MyAliasToEntityMapResultTransformer.INSTANCE);
		Assert.assertNotNull(role102);

	}
	
	
	@Test
	public void findBy() {

		// JPQＬ查询
		List<Role> role1 = roleDao.findByName1("Admin");
		Assert.assertNotNull(role1);

		Sort sort = new Sort(Sort.Direction.ASC, "name");
		role1 = roleDao.findByName1("Admin", sort);
		Assert.assertNotNull(role1);

		//sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "permissions"));		
		Pageable pageable = new PageRequest(0, 5, sort);
		Page<Role> page5 = roleDao.findByName1("Admin", pageable);
		Assert.assertNotNull(page5);

		//sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "permissions"));
		pageable = new PageRequest(0, 5, sort);
		Page<Role> page52 = roleDao.findByName1("Admin", pageable, RootEntityResultTransformer.INSTANCE);
		Assert.assertNotNull(page52);

		List<Role> role21 = roleDao.findByName2("Admin");
		Assert.assertNotNull(role21);

		List<Map<String, Object>> role6 = roleDao.findByName3("Admin");
		Assert.assertNotNull(role6);

		List<Map<String, Object>> role62 = roleDao.findByName3("Admin", MyAliasToEntityMapResultTransformer.INSTANCE);
		Assert.assertNotNull(role62);

		List<Map<String, Object>> role7 = roleDao.findByName4("Admin");
		Assert.assertNotNull(role7);

		// SQＬ查询
		List<Role> role5 = roleDao.findByName5("Admin");
		Assert.assertNotNull(role5);

		sort = new Sort(Sort.Direction.ASC, "name");
		role5 = roleDao.findByName5("Admin", sort);
		Assert.assertNotNull(role5);

		//sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "permissions"));
		sort = new Sort(Sort.Direction.ASC, "a.name");
		pageable = new PageRequest(0, 5, sort);	
		Page<Map<String, Object>> page6 = roleDao.findByName5("Admin", pageable, Transformers.ALIAS_TO_ENTITY_MAP);
		Assert.assertNotNull(page6);

		List<Role> role8 = roleDao.findByName6("Admin");
		Assert.assertNotNull(role8);

		List<Map<String, Object>> role10 = roleDao.findByName7("Admin");
		Assert.assertNotNull(role10);

		List<Map<String, Object>> role102 = roleDao.findByName7("Admin", MyAliasToEntityMapResultTransformer.INSTANCE);
		Assert.assertNotNull(role102);
	}

	@Test
	public void updateRole() {
		int i = roleDao.updateRole("Admin", "1");
		Assert.assertTrue(i >= 1);

	}

	@Test
	public void insertRole() {
		int i = roleDao.insertRole(Identities.uuid2(), "经理");
		Assert.assertTrue(i >= 1);

	}	

}
