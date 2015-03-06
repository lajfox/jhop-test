package com.techstar.security.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.JsonTreeJpaServiceImpl;
import com.techstar.security.entity.Permission;
import com.techstar.security.repository.jpa.PermissionDao;

/**
 * 角色管理业务类.
 * 
 * @author ZengWenfneg
 */

@Component
@Transactional(readOnly = true)
public class PermissionService extends JsonTreeJpaServiceImpl<Permission, String> {

	@Autowired
	private PermissionDao permissionDao;

	@Override
	protected MyJpaRepository<Permission, String> getMyJpaRepository() {
		return permissionDao;
	}

	/***
	 * 权限信息维护
	 * 
	 * @Date 2013-5-24 上午11:47:19
	 * @author lrm
	 */
	@Transactional(readOnly = false)
	public Permission savePermission(Permission permission, String oper, String parentid) {
		if (StringUtils.equals("add", oper)) {
			permission.setId(null);
		}
		// 在迁移过程中，前父结点的level需要修改
		Permission parent = permission.getParent();

		if (StringUtils.isNotEmpty(parentid)) {
			permission.setParent(this.findOne(parentid));
		} else {
			permission.setParent(null);
		}
		this.savex(permission);

		// 在迁移过程中，前父结点的level需要修改
		if (parent != null && (StringUtils.isEmpty(parentid) || !parent.getId().equals(parentid))) {
			parent.getChildrens().remove(permission);
			if (!parent.hasChildren()) {
				parent.setLeaf(true);
				this.save(parent);
			}
		}

		return permission;
	}

	/***
	 * 删除组织机构前了清理用户的数据
	 * 
	 * @Date 2013-5-28 下午2:32:53
	 * @author lrm
	 */
	@Transactional(readOnly = false)
	public void delete(List<String> ids) {
		for (String id : ids) {
			Permission permission = this.findOne(id);
			this.deletex(permission);
		}
	}

}
