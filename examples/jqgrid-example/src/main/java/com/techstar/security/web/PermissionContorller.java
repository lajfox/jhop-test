package com.techstar.security.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.JsonTree;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;
import com.techstar.modules.utils.Identities;
import com.techstar.modules.utils.JsonTreeUtils;
import com.techstar.security.entity.Permission;
import com.techstar.security.service.PermissionService;

@Controller
@RequestMapping("/security/permission")
public class PermissionContorller {

	@Autowired
	private PermissionService permissionService;

	@RequestMapping("")
	public String list(Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		return "security/permissionList";
	}

	@RequestMapping("/search")
	public @ResponseBody
	Results search(Specification<Permission> spec) {
		List<Permission> permissions = permissionService.findAll(spec, new Sort(Sort.Direction.ASC, "orderno"), true);
		return new Response<Permission>(permissions);
	}

	@RequiresPermissions(value = { "admin", "permission:create", "permission:update" }, logical = Logical.OR)
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public @ResponseBody
	Results edit(@Valid @PreModelAttribute(disallowedFields = { "users", "roles", "parent" }) Permission permission,
			@RequestParam(value = "parentx", required = false) String parentid,
			@RequestParam(value = "oper", required = false) String oper) {
		String message = "修改成功";
		if (StringUtils.equals("add", oper)) {
			message = "保存成功";
		}
		permissionService.savePermission(permission, oper, parentid);
		return new Results(true, message, permission);
	}

	/***
	 * 数据删除
	 * 
	 * @Date 2013-5-28 下午3:03:00
	 * @author lrm
	 */
	@RequiresPermissions(value = { "admin", "permission:delete" }, logical = Logical.OR)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody
	Results delete(@RequestParam("id") List<String> ids) {
		this.permissionService.delete(ids);
		return new Results(true);
	}

	@RequestMapping("/selectParent")
	public String selectParent(@RequestParam(value = "edit_id", required = false) String id, Model model) {
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		List<Permission> permissions = StringUtils.isEmpty(id) ? permissionService.findBy("menuTrue", sort)
				: permissionService.findBy("menuTrueAndIdNot", sort, id);
		model.addAttribute("permissions", permissions);
		return "security/selectParent";
	}

	@RequestMapping("checkName")
	public @ResponseBody
	Results checkName(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name) {
		boolean exists = StringUtils.isEmpty(id) ? permissionService.exists("name", name) : permissionService.exists(
				"nameAndIdNot", name, id);
		return new Results(!exists);
	}

	/***
	 * 根据当前用户查询 相关的菜单
	 * 
	 * @Date 2013-3-11 上午11:43:28
	 * @author lrm
	 */
	@RequestMapping(value = "/menu")
	public @ResponseBody
	Results menu() {
		String userId = SubjectUtils.getPrincipal(ShiroUser.class).getId();
		Sort sort = new Sort(Sort.Direction.ASC, "orderno");

		List<Permission> permissions1 = permissionService.findBy("menuTrueAndRoles.users.id", sort, userId);
		List<Permission> permissions2 = permissionService.findBy("menuTrueAndUsers.id", sort, userId);

		Set<Permission> permissions = new HashSet<Permission>();
		if (CollectionUtils.isNotEmpty(permissions1)) {
			permissions.addAll(permissions1);
		}
		if (CollectionUtils.isNotEmpty(permissions2)) {
			permissions.addAll(permissions2);
		}

		return new Response<JsonTree>(JsonTreeUtils.build(permissions));
	}

	/***
	 * 选择父级菜单
	 * 
	 * @Date 2013-5-23 下午6:21:38
	 * @author lrm
	 */
	@RequestMapping("/selectMenu")
	public @ResponseBody
	List<Permission> selectPermission(@RequestParam(value = "edit_id", required = false) String id,
			@RequestParam(value = "parentId", required = false) String parentId) {
		Sort sort = new Sort(Sort.Direction.ASC, "orderno");
		List<Permission> permissions = StringUtils.isEmpty(id) ? permissionService.findBy("menuTrue", sort)
				: permissionService.findBy("menuTrueAndIdNot", sort, id);
		permission(permissions, parentId, true);
		return permissions;
	}

	private void permission(final List<Permission> permissions, final String parentId, final boolean checked) {
		if (CollectionUtils.isNotEmpty(permissions) && StringUtils.isNotEmpty(parentId)) {
			for (Permission p : permissions) {
				if (StringUtils.equals(p.getId(), parentId)) {
					p.setChecked(checked);
					p.setOpen(true);
					if (StringUtils.isNotEmpty(p.getParentId())) {
						permission(permissions, p.getParentId(), false);
					}
				}
			}
		}
	}

}
