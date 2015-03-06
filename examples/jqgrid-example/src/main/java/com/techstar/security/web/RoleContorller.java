package com.techstar.security.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;
import com.techstar.modules.utils.Identities;
import com.techstar.security.entity.Permission;
import com.techstar.security.entity.Role;
import com.techstar.security.service.RoleService;

@Controller
@RequestMapping("/security/role")
public class RoleContorller {

	@Autowired
	private RoleService roleService;

	@RequestMapping("")
	public String list(Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		return "security/roleList";
	}

	@RequestMapping("/search")
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Role> spc) {
		Page<Role> roles = roleService.findAll(spc, pageable);
		return new PageResponse<Role>(roles);
	}

	@RequiresPermissions(value = { "admin", "role:create", "role:update" }, logical = Logical.OR)
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public @ResponseBody
	Results edit(
			@Valid @PreModelAttribute(disallowedFields = { "users", "permissions", "permissionList[]" }) Role role,
			@RequestParam(value = "permissionList[]", required = false) List<String> permissions,
			@RequestParam(value = "oper", required = false) String oper) {
		String message = "修改成功";
		if (StringUtils.equals("add", oper)) {
			role.setId(null);
			message = "保存成功";
		}
		// bind permissions
		role.getPermissions().clear();
		if (CollectionUtils.isNotEmpty(permissions)) {
			role.getPermissions().addAll(roleService.findBy(Permission.class, "idIn", permissions));
		}
		roleService.save(role);
		return new Results(true, message, role);
	}

	/***
	 * 数据删除
	 * 
	 * @Date 2013-5-28 下午3:03:00
	 * @author lrm
	 */
	@RequiresPermissions(value = { "admin", "role:delete" }, logical = Logical.OR)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody
	Results delete(@RequestParam("id") List<String> ids) {
		this.roleService.delete(ids);
		return new Results(true);
	}

	@RequestMapping("checkName")
	public @ResponseBody
	Results checkName(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name) {
		boolean exists = StringUtils.isEmpty(id) ? roleService.exists("name", name) : roleService.exists(
				"nameAndIdNot", name, id);
		return new Results(!exists);
	}

	@RequestMapping("/selectPermission")
	public @ResponseBody
	List<Map<String, Object>> selectPermission(@RequestParam(value = "edit_id", required = false) String id, Model model) {
		List<Map<String, Object>> permissions = StringUtils.isEmpty(id) ? roleService.findAllPermissions()
				: roleService.findPermissionsByRole(id);

		return permissions;
	}

	/***
	 * TODO 查询权限树
	 * 
	 * @Date 2013-3-6 下午4:07:05
	 * @author lrm
	 */
	@RequestMapping("selectPermissionTree")
	public @ResponseBody
	List<Permission> selectPermissionTree(Specification<Permission> spc) {
		Sort sort = new Sort(Sort.Direction.ASC, "order");
		List<Permission> permissions = this.roleService.findAll(Permission.class, spc, sort);
		return permissions;
	}

}
