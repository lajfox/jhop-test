package com.techstar.security.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.MyJpaServiceImpl;
import com.techstar.security.entity.Role;
import com.techstar.security.repository.jpa.PermissionDao;
import com.techstar.security.repository.jpa.RoleDao;

/**
 * 角色管理业务类.
 * 
 * @author ZengWenfneg
 */

@Component
@Transactional(readOnly = true)
public class RoleService extends MyJpaServiceImpl<Role, String> {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private PermissionDao permissionDao;

	@Override
	protected MyJpaRepository<Role, String> getMyJpaRepository() {
		return roleDao;
	}

	public List<Map<String, Object>> findAllPermissions() {
		List<Map<String, Object>> permissions = permissionDao.getAll();
		if (CollectionUtils.isNotEmpty(permissions)) {
			for (Map<String, Object> map : permissions) {
				map.put("checked", false);
//				map.put("open", MapUtils.getIntValue(map, "level_") == 0 ? true : false);
				map.put("open",false);
			}
		}
		return permissions;
	}

	public List<Map<String, Object>> findPermissionsByRole(final String id) {
		List<Map<String, Object>> permissions = permissionDao.findByRole(id);
		if (CollectionUtils.isNotEmpty(permissions)) {
			for (Map<String, Object> map : permissions) {
				map.put("checked", MapUtils.getIntValue(map, "cnt") == 0 ? false : true);
				map.put("open", MapUtils.getIntValue(map, "cnt") == 0 ? false : true);
				map.put("open",false);
			}
		}
		return permissions;
	}
}
