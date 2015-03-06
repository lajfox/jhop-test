package com.techstar.example.repository.jpa.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.techstar.example.entity.Permission;
import com.techstar.example.entity.Role;
import com.techstar.example.entity.Role_;
import com.techstar.example.entity.Team;
import com.techstar.example.entity.User;
import com.techstar.example.entity.User_;
import com.techstar.example.repository.jpa.RoleDao;
import com.techstar.modules.springframework.test.SpringTransactionalTestCase;

/**
 * MyJpaRepository接口测试
 * 
 * @author sundoctor
 * 
 */
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class MyJpaRepositoryTest extends SpringTransactionalTestCase {

	@Autowired
	private RoleDao roleDao;

	/**
	 * 
	 * @see org.springframework.data.repository.CrudRepository#delete(java.io.Serializable)
	 * 
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#delete(Class,
	 *      java.io.Serializable)
	 */
	@Test
	public void delete1() {
		// 删除前
		Role role = roleDao.findOne("3");
		Assert.assertNotNull(role);

		// 根据ＩＤ删除角色
		roleDao.delete("3");

		// 删除后
		role = roleDao.findOne("3");
		Assert.assertNull(role);

		// 用RoleDao删除用户，只需要增加User实体类型
		roleDao.delete(User.class, "6");
	}

	/**
	 * @see org.springframework.data.repository.CrudRepository#delete(Object)
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#delete(Class,
	 *      Object)
	 */
	@Test
	public void delete2() {
		// 删除前
		Role role = roleDao.findOne("3");
		Assert.assertNotNull(role);

		// 删除角色
		roleDao.delete(role);

		// 删除后
		role = roleDao.findOne("3");
		Assert.assertNull(role);

		// 用RoleDao删除用户，只需要增加User实体类型
		User user = roleDao.findOne(User.class, "6");
		roleDao.delete(User.class, user);
	}

	/**
	 * @see org.springframework.data.repository.CrudRepository#delete(Iterable)
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#delete(Class,
	 *      Iterable)
	 */
	@Test
	public void delete3() {

		Collection<Role> entities = new ArrayList<Role>();
		entities.add(roleDao.findOne("3"));
		entities.add(roleDao.findOne("4"));

		// 删除角色
		roleDao.delete(entities);

		// 删除后
		Role role = roleDao.findOne("3");
		Assert.assertNull(role);

		// 用RoleDao删除用户，只需要增加User实体类型
		Collection<User> users = new ArrayList<User>();
		users.add(roleDao.findOne(User.class, "6"));
		users.add(roleDao.findOne(User.class, "5"));

		roleDao.delete(User.class, users);
	}

	/**
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#delete(Collection)
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#delete(Class,
	 *      Collection)
	 */
	@Test
	public void delete4() {

		Collection<String> entities = new ArrayList<String>();
		entities.add("3");
		entities.add("4");

		// 删除角色
		roleDao.delete(entities);

		// 删除后
		Role role = roleDao.findOne("3");
		Assert.assertNull(role);

		// 用RoleDao删除用户，只需要增加User实体类型
		Collection<String> users = new ArrayList<String>();
		users.add("6");
		users.add("5");

		roleDao.delete(User.class, users);

		// 删除后
		User user = roleDao.findOne(User.class, "5");
		Assert.assertNull(user);
	}

	/**
	 * @see org.springframework.data.jpa.repository.JpaRepository#deleteInBatch(java.lang.Iterable)
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#deleteInBatch(Class,
	 *      Collection)
	 */
	@Test
	public void deleteInBatch1() {

		Collection<Role> entities = new ArrayList<Role>();
		entities.add(roleDao.findOne("3"));
		entities.add(roleDao.findOne("4"));

		// 删除角色
		roleDao.deleteInBatch(entities);

		// 用RoleDao删除用户，只需要增加User实体类型
		Collection<User> users = new ArrayList<User>();
		users.add(roleDao.findOne(User.class, "6"));
		users.add(roleDao.findOne(User.class, "5"));

		roleDao.deleteInBatch(User.class, users);
	}

	/**
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#deleteInBatch(Collection)
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#deleteInBatch(Class,
	 *      Collection)
	 */
	@Test
	public void deleteInBatch2() {

		Collection<String> entities = new ArrayList<String>();
		entities.add("3");
		entities.add("4");

		// 删除角色
		roleDao.deleteInBatch(entities);

		// 用RoleDao删除用户，只需要增加User实体类型
		Collection<String> users = new ArrayList<String>();
		users.add("6");
		users.add("5");

		roleDao.deleteInBatch(User.class, users);
	}

	/**
	 * @see org.springframework.data.repository.CrudRepository#deleteAll()
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#deleteAll(Class)
	 *      ;
	 */
	@Test
	public void deleteAll() {

		// 删除角色
		roleDao.deleteAll();

		// 删除后
		Role role = roleDao.findOne("3");
		Assert.assertNull(role);

		// 用RoleDao删除用户，只需要增加User实体类型

		roleDao.deleteAll(User.class);

		// 删除后
		User user = roleDao.findOne(User.class, "5");
		Assert.assertNull(user);
	}

	/**
	 * @see org.springframework.data.jpa.repository.JpaRepository#deleteAllInBatch()
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#deleteAllInBatch(Class)
	 */
	@Test
	public void deleteAllInBatch() {

		// 删除前
		Role role = roleDao.findOne("4");
		Assert.assertNotNull(role);

		// 删除角色
		roleDao.deleteAllInBatch();

		// 删除后
		role = roleDao.findOne("3");
		Assert.assertNull(role);

		// 用RoleDao删除用户，只需要增加User实体类型
		roleDao.deleteAllInBatch(User.class);

		// 删除后
		User user = roleDao.findOne(User.class, "5");
		Assert.assertNull(user);
	}

	/**
	 * @see org.springframework.data.repository.CrudRepository#count()
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#count(Class)
	 * @see org.springframework.data.jpa.repository.JpaSpecificationExecutor#count(Specification)
	 * @see com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository#count(Class,
	 *      Specification)
	 */
	@Test
	public void count() {

		// 查询记录总数
		long count1 = roleDao.count();
		List<Role> roles = roleDao.findAll();
		Assert.assertEquals(count1, roles.size());

		// 查询记录总数
		long count2 = roleDao.count(User.class);
		List<User> users = roleDao.findAll(User.class);
		Assert.assertEquals(count2, users.size());

		// 根据spring data jpa 规范查询记录总数
		// select count(x) from Role x where x.name=?1
		long count5 = roleDao.count("name", "Admin");
		Assert.assertEquals(1, count5);

		// 根据spring data jpa 规范查询记录总数
		// select count(x) from User x where x.name=?1 and x.loginName=?2
		long count6 = roleDao.count(User.class, "nameAndLoginName", "Admin", "admin");
		Assert.assertEquals(1, count6);

		// 按条件查询记录总数 select count(x) from Role x where x.name=?1 and x.id=?2
		long count3 = roleDao.count(new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// return cb.equal(root.get(Role_.name), "Admin");
				return cb.and(cb.equal(root.get(Role_.name), "Admin"), cb.equal(root.get(Role_.id), "1"));
			}

		});
		Assert.assertEquals(1, count3);

		// 按条件查询记录总数 select count(x) from User x join x.roles b where
		// x.loginName=?1 or b.name=?2
		long count4 = roleDao.count(User.class, new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				SetJoin<User, Role> role = root.join(User_.roles);
				return cb.or(cb.equal(root.get(User_.loginName), "admin"), cb.equal(role.get(Role_.name), "Calvin"));
			}

		});
		Assert.assertEquals(2, count4);
	}

	@Test
	public void save() {

		// 保存记录
		Role role1 = new Role();
		role1.setName("总经理");

		List<String> ids = new ArrayList<String>();
		ids.add("1");
		ids.add("2");
		Set<Permission> ps = new HashSet<Permission>(roleDao.findBy(Permission.class, "idIn", ids));
		role1.setPermissions(ps);

		// roleDao.save(Permission.class,ps);
		roleDao.save(role1);

		Role role2 = roleDao.findOne("name", "总经理");
		Assert.assertNotNull(role2);

		// 更新记录
		role2.setName("董事长");
		roleDao.save(role2);

		Role role3 = roleDao.findOne("name", "总经理");
		Assert.assertNull(role3);

		// 保存多条记录
		Collection<Role> roles = new ArrayList<Role>();

		Role role = new Role();
		role.setName("name1");

		role.setPermissions(ps);
		roles.add(role);

		Role role4 = roleDao.findOne("name", "董事长");

		role4.setPermissions(ps);
		roles.add(role4);

		List<Role> rs = roleDao.save(roles);
		Assert.assertEquals(2, rs.size());

		/*------------------------------------------*/

		// 保存记录
		Team team1 = new Team();
		team1.setName("声乐队");
		roleDao.save(Team.class, team1);

		Team team2 = roleDao.findOne(Team.class, "name", "声乐队");
		Assert.assertNotNull(team2);

		// 更新记录
		team2.setName("足球队");
		roleDao.save(Team.class, team2);

		Team team3 = roleDao.findOne(Team.class, "name", "足球队");
		Assert.assertNotNull(team3);

		// 保存多条记录
		Collection<Team> teams = new ArrayList<Team>();

		Team team = new Team();
		team.setName("name1");
		teams.add(team);

		Team team4 = roleDao.findOne(Team.class, "name", "足球队");
		team4.setName("跳水队");
		teams.add(team4);

		List<Team> ts = roleDao.save(Team.class, teams);
		Assert.assertEquals(2, ts.size());
	}

	@Test
	public void saveAndFlush() {
		// 保存记录
		Role role1 = new Role();
		role1.setName("总经理2");

		List<String> ids = new ArrayList<String>();
		ids.add("1");
		ids.add("2");
		Set<Permission> ps = new HashSet<Permission>(roleDao.findBy(Permission.class, "idIn", ids));

		role1.setPermissions(ps);

		roleDao.saveAndFlush(role1);

		Team team1 = new Team();
		team1.setName("声乐队2");
		roleDao.saveAndFlush(Team.class, team1);
	}

	@Test
	public void findOne() {

		// 根据主键查询记录
		Role role = roleDao.findOne("1");
		Assert.assertNotNull(role);

		// 根据主键查询记录
		User user = roleDao.findOne(User.class, "1");
		Assert.assertNotNull(user);

		// 根据spring data jpa 规范查询记录
		// select x from Role x where x.name="Admin";
		Role role3 = roleDao.findOne("name", "Admin");
		Assert.assertNotNull(role3);
		// 根据spring data jpa 规范查询记录
		// select x from User x where x.name="Admin" and x.loginName="admin"
		User user3 = roleDao.findOne(User.class, "nameAndLoginName", "Admin", "admin");
		Assert.assertNotNull(user3);

		// 根据条件查询记录
		Role role2 = roleDao.findOne(new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Role_.name), "Admin");
			}
		});
		Assert.assertNotNull(role2);

		// 根据条件查询记录
		// select count(x) from User x join x.roles b where x.loginName=?1 and
		// b.name=?2
		User user2 = roleDao.findOne(User.class, new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				SetJoin<User, Role> role = root.join(User_.roles);
				return cb.and(cb.equal(root.get(User_.name), "Admin"), cb.equal(role.get(Role_.name), "Admin"));
			}
		});
		Assert.assertNotNull(user2);

	}

	@Test
	public void exists() {

		// 根据主键查询记录是否存在
		boolean exists1 = roleDao.exists("1");
		Assert.assertTrue(exists1);

		// 根据条件查询记录是否存在
		boolean exists5 = roleDao.exists(new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(Role_.name), "Admin");
			}
		});
		Assert.assertTrue(exists5);

		// 根据主键查询记录是否存在
		boolean exists2 = roleDao.exists(User.class, "1");
		Assert.assertTrue(exists2);

		// 根据条件查询记录是否存在
		boolean exists6 = roleDao.exists(User.class, new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(User_.name), "Calvin");
			}
		});
		Assert.assertTrue(exists6);

		// 根据spring data jpa 规范查询记录是否存在
		boolean exists3 = roleDao.exists("name", "Admin");
		Assert.assertTrue(exists3);
		// 根据spring data jpa 规范查询记录是否存在
		boolean exists4 = roleDao.exists(User.class, "nameAndLoginName", "Admin", "admin");
		Assert.assertTrue(exists4);
	}

	@Test
	public void findAll() {

		// 查找所有记录
		List<Role> roles = roleDao.findAll();
		Assert.assertTrue(roles.size() > 0);

		// 查找所有记录并排序
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		// Sort sort = new Sort(new Order(Sort.Direction.ASC, "name"), new
		// Order(Sort.Direction.DESC, "permissions"));
		List<Role> roles3 = roleDao.findAll(sort);
		Assert.assertTrue(roles3.size() > 0);

		// 查找所有记录并分页排序
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "permissions"));
		Pageable pageable = new PageRequest(0, 5, sort);
		Page<Role> page = roleDao.findAll(pageable);
		Assert.assertTrue(page.getContent().size() > 0);

		// 根据条件查询所有记录
		List<Role> roles4 = roleDao.findAll(new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaquery,
					CriteriaBuilder criteriabuilder) {
				return criteriabuilder.equal(root.get(Role_.name), "Admin");
			}
		});
		Assert.assertNotNull(roles4);

		// 根据条件查询所有记录并排序
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "permissions"));
		List<Role> roles5 = roleDao.findAll(new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaquery,
					CriteriaBuilder criteriabuilder) {
				return criteriabuilder.equal(root.get(Role_.name), "Admin");
			}
		}, sort);
		Assert.assertNotNull(roles5);

		// 根据条件查询所有记录并分页排序
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "permissions"));
		pageable = new PageRequest(0, 5, sort);
		Page<Role> page3 = roleDao.findAll(new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaquery,
					CriteriaBuilder criteriabuilder) {
				return criteriabuilder.equal(root.get(Role_.name), "Admin");
			}
		}, pageable);
		Assert.assertNotNull(page3.getContent());

		// 根据主键集合查询所有记录
		Collection<String> ids = new ArrayList<String>();
		ids.add("1");
		ids.add("2");
		List<Role> roles2 = roleDao.findAll(ids);
		Assert.assertEquals(2, roles2.size());

		/*--------------------------------------*/

		// 查找所有记录
		List<User> users = roleDao.findAll(User.class);
		Assert.assertTrue(users.size() > 0);

		// 查找所有记录并排序
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "loginName"));
		List<User> users3 = roleDao.findAll(User.class, sort);
		Assert.assertTrue(users3.size() > 0);

		// 查找所有记录并分页排序
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "loginName"));
		pageable = new PageRequest(0, 5, sort);
		Page<User> page2 = roleDao.findAll(User.class, pageable);
		Assert.assertTrue(page2.getContent().size() > 0);

		// 根据条件查询所有记录
		List<User> users4 = roleDao.findAll(User.class, new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaquery,
					CriteriaBuilder criteriabuilder) {
				return criteriabuilder.equal(root.get(User_.name), "Calvin");
			}
		});
		Assert.assertNotNull(users4);

		// 根据条件查询所有记录并排序
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "loginName"));
		List<User> users5 = roleDao.findAll(User.class, new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaquery,
					CriteriaBuilder criteriabuilder) {
				return criteriabuilder.equal(root.get(User_.name), "Calvin");
			}
		}, sort);
		Assert.assertNotNull(users5);

		// 根据条件查询所有记录并分页排序
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "loginName"));
		pageable = new PageRequest(0, 5, sort);
		Page<User> page4 = roleDao.findAll(User.class, new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaquery,
					CriteriaBuilder criteriabuilder) {
				return criteriabuilder.equal(root.get(User_.name), "Calvin");
			}
		}, pageable);
		Assert.assertNotNull(page4.getContent());

		// 根据主键集合查询所有记录
		ids = new ArrayList<String>();
		ids.add("1");
		ids.add("2");
		ids.add("3");
		List<User> users2 = roleDao.findAll(User.class, ids);
		Assert.assertEquals(3, users2.size());
	}

	/**
	 * 根据spring data jpa 规范查询记录
	 */
	@Test
	public void findBy() {
		// 根据spring data jpa 规范查询记录
		// select x from Role x where x.name=?1;
		List<Role> roles1 = roleDao.findBy("name", "Admin");
		Assert.assertNotNull(roles1);

		// 根据spring data jpa 规范查询记录并排序
		// select x from Role x where x.name=?1 order by x.name
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		List<Role> roles2 = roleDao.findBy("name", sort, "Admin");
		Assert.assertNotNull(roles2);

		// 根据spring data jpa 规范查询记录并分页排序
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "permissions"));
		Pageable pageable = new PageRequest(0, 5, sort);
		Page<Role> page = roleDao.findBy("name", pageable, "Admin");
		Assert.assertNotNull(page.getContent());

		// 根据spring data jpa 规范查询记录
		// select x from User x where x.name=?1 and x.loginName=?2
		List<User> users1 = roleDao.findBy(User.class, "nameAndLoginName", "Admin", "admin");
		Assert.assertNotNull(users1);

		// 根据spring data jpa 规范查询记录 @ManyToMany:roles
		// select x from User x join x.roles b where x.name=?1 and b.name=?2
		List<User> users11 = roleDao.findBy(User.class, "nameAndRolesName", "Admin", "Admin");
		Assert.assertNotNull(users11);

		// 根据spring data jpa 规范查询记录 @ManyToOne:team
		// select x from User x join x.team b where x.name=?1 and b.id=?2
		List<User> users12 = roleDao.findBy(User.class, "nameAndRolesId", "Admin", "1");
		Assert.assertNotNull(users12);

		// 根据spring data jpa 规范查询记录并排序
		// select x from User x where x.name="Admin" and x.loginName=?1 order by
		// x.name,x.loginName desc
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "loginName"));
		List<User> users2 = roleDao.findBy(User.class, "nameAndLoginName", sort, "Admin", "admin");
		Assert.assertNotNull(users2);

		// 根据spring data jpa 规范查询记录并分页排序
		sort = new Sort(new Order(Sort.Direction.ASC, "name"), new Order(Sort.Direction.DESC, "loginName"));
		pageable = new PageRequest(0, 5, sort);
		Page<User> page2 = roleDao.findBy(User.class, "nameAndLoginName", pageable, "Admin", "admin");
		Assert.assertNotNull(page2.getContent());

	}

}