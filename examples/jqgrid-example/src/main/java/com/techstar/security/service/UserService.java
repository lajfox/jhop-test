package com.techstar.security.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.hibernate.Hibernates;
import com.techstar.modules.shiro.web.util.PasswordUtils;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.MyJpaServiceImpl;
import com.techstar.security.entity.User;
import com.techstar.security.repository.jpa.OrganizationDao;
import com.techstar.security.repository.jpa.PermissionDao;
import com.techstar.security.repository.jpa.RoleDao;
import com.techstar.security.repository.jpa.UserDao;
import com.techstar.security.service.ShiroDbRealm.ShiroUser;

/**
 * 用户管理业务类.
 * 
 * @author zengWenfeng
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class UserService extends MyJpaServiceImpl<User, String> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PermissionDao permissionDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Override
	protected MyJpaRepository<User, String> getMyJpaRepository() {
		return userDao;
	}

	public List<Map<String, Object>> findAllRoles() {
		return roleDao.getAll();
	}

	public List<Map<String, Object>> findRolesByUser(final String id) {
		return roleDao.findByUser(id);
	}

	public List<Map<String, Object>> findAllPermissions() {
		List<Map<String, Object>> permissions = permissionDao.getAll();
		if (CollectionUtils.isNotEmpty(permissions)) {
			for (Map<String, Object> map : permissions) {
				map.put("checked", false);
				// map.put("open", MapUtils.getIntValue(map, "level_") == 0 ?
				// true: false);
				map.put("open", false);
			}
		}
		return permissions;
	}

	public List<Map<String, Object>> findPermissionsByUser(final String id) {
		List<Map<String, Object>> permissions = permissionDao.findByUser(id);

		if (CollectionUtils.isNotEmpty(permissions)) {
			for (Map<String, Object> map : permissions) {
				map.put("checked", MapUtils.getIntValue(map, "cnt") == 0 ? false : true);
				// map.put("open", MapUtils.getIntValue(map, "cnt") == 0 ?
				// false: true);
				map.put("open", false);
			}
		}
		return permissions;
	}

	/**
	 * 查询全部组织机构信息 CaiFangfang 2013-10-31
	 */
	public List<Map<String, Object>> findAllOrganization() {
		List<Map<String, Object>> organizations = organizationDao.getAll();
		if (CollectionUtils.isNotEmpty(organizations)) {
			for (Map<String, Object> map : organizations) {
				map.put("checked", false);
				// map.put("open", MapUtils.getIntValue(map, "level_") == 0 ?
				// true : false);
				map.put("open", false);
			}
		}
		return organizations;
	}

	/**
	 * 根据用户id查找组织机构 CaiFangfang 2013-10-31
	 */
	public List<Map<String, Object>> findOrganizationByUser(final String id) {
		List<Map<String, Object>> organizations = organizationDao.findByUsers(id);

		if (CollectionUtils.isNotEmpty(organizations)) {
			for (Map<String, Object> map : organizations) {
				map.put("checked", MapUtils.getIntValue(map, "cnt") == 0 ? false : true);
				map.put("open", false);
			}
		}
		return organizations;
	}

	/**
	 * 
	 * 如果企图修改超级用户,取出当前操作员用户,打印其信息然后抛出异常.
	 * 
	 */
	@Transactional(readOnly = false)
	public User saveUser(User user) {

		if (isSupervisor(user)) {
			logger.warn("操作员{}尝试修改超级管理员用户", getCurrentUserName());
			throw new ServiceException("不能修改超级管理员用户");
		}

		// 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
		if (StringUtils.isNotBlank(user.getPlainPassword())) {
			PasswordUtils.entryptPassword(user);
		}

		return save(user);

	}

	public List<User> getAllUserInitialized() {
		List<User> result = findAll();
		for (User user : result) {
			Hibernates.initLazyProperty(user.getRoles());
		}
		return result;
	}

	/**
	 * 判断是否超级管理员.
	 */
	public boolean isSupervisor(User user) {
		return (user.getId() != null && user.getId().equals("1"));
	}

	/**
	 * 按名称查询用户, 并对用户的延迟加载关联进行初始化.
	 */
	public User findUserByNameInitialized(String name) {
		User user = findOne("name", name);
		if (user != null) {
			Hibernates.initLazyProperty(user.getRoles());
		}
		return user;
	}

	/**
	 * 取出Shiro中的当前用户LoginName.
	 */
	private String getCurrentUserName() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.getLoginName();
	}

	/**
	 * 
	 * CaiFangfang 2013-5-8
	 */
	@Transactional(readOnly = false)
	public void changePassword(User user) {
		PasswordUtils.entryptPassword(user);
		save(user);
	}

}
