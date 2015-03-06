package com.techstar.security.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.techstar.modules.jasperreports.util.JasperModelAndViewUtils;
import com.techstar.modules.jasperreports.web.servlet.support.RequestContext;
import com.techstar.modules.shiro.web.filter.authc.FormAuthenticationWithLockFilter;
import com.techstar.modules.shiro.web.util.PasswordUtils;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;
import com.techstar.modules.springframework.web.util.WebUtils;
import com.techstar.modules.utils.Identities;
import com.techstar.security.entity.Organization;
import com.techstar.security.entity.Permission;
import com.techstar.security.entity.Role;
import com.techstar.security.entity.User;
import com.techstar.security.service.ShiroDbRealm.ShiroUser;
import com.techstar.security.service.UserService;

@Controller
@RequestMapping("/security/user")
public class UserController {

	private static final Map<String, String> allStatus = Maps.newHashMap();
	private static final FormAuthenticationWithLockFilter FILTER = new FormAuthenticationWithLockFilter();

	static {
		allStatus.put("enabled", "有效");
		allStatus.put("disabled", "无效");
	}

	@Autowired
	private UserService userService;

	// @RequiresPermissions("user:view")
	@RequestMapping("")
	public String list(Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		model.addAttribute("allStatus", WebUtils.toValue(allStatus));
		return "security/userList";
	}

	@RequestMapping("/search")
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<User> spc) {
		Page<User> users = userService.findAll(spc, pageable);
		return new PageResponse<User>(users);
	}

	@RequestMapping("/export/{format}")
	public ModelAndView export(@PathVariable(RequestContext.FORMATKEY) String formatKey,
			@RequestParam(RequestContext.URLKEY) String jr_url, Specification<User> spc) {
		List<User> users = userService.findAll(spc);
		return JasperModelAndViewUtils.forword(formatKey, jr_url, new JRBeanCollectionDataSource(users));
	}

	/**
	 * 保存用户
	 */
	@RequiresPermissions(value = { "admin", "user:create", "user:update" }, logical = Logical.OR)
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public @ResponseBody
	Results edit(@Valid @PreModelAttribute(disallowedFields = { "roles", "permissions", "organizations", "roleList[]",
			"permissionList[]", "organizationList[]" }) User user,
			@RequestParam(value = "roleList[]", required = false) List<String> roles,
			@RequestParam(value = "permissionList[]", required = false) List<String> permissions,
			@RequestParam(value = "organizationList[]", required = false) List<String> organizations,
			@RequestParam(value = "oper", required = false) String oper) {

		String message = "修改成功";
		if (StringUtils.equals("add", oper)) {
			user.setId(null);
			message = "保存成功";
		} else {
			if (userService.isSupervisor(user)) {
				return new Results(false, "不允许修改超级管理员用户", user);
			}
		}

		// bind roles
		user.getRoles().clear();
		if (CollectionUtils.isNotEmpty(roles)) {
			user.getRoles().addAll(userService.findBy(Role.class, "idIn", roles));
		}

		// bind permissions
		user.getPermissions().clear();
		if (CollectionUtils.isNotEmpty(permissions)) {
			user.getPermissions().addAll(userService.findBy(Permission.class, "idIn", permissions));
		}
		// bind organizations
		user.getOrganizations().clear();
		if (CollectionUtils.isNotEmpty(organizations)) {
			user.getOrganizations().addAll(userService.findBy(Organization.class, "idIn", organizations));

		}
		userService.saveUser(user);
		return new Results(true, message, user);

	}

	@RequiresPermissions(value = { "admin", "user:delete" }, logical = Logical.OR)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody
	Results delete(@RequestParam("id") String id) {
		User user = this.userService.findOne(id);
		if (userService.isSupervisor(user)) {
			return new Results(false, "不允许删除超级管理员用户");
		} else {
			this.userService.delete(user);
			return new Results(true);
		}
	}

	@RequestMapping("/selectRole")
	public String selectRole(@RequestParam(value = "edit_id", required = false) String id, Model model) {
		List<Map<String, Object>> roles = StringUtils.isEmpty(id) ? userService.findAllRoles() : userService
				.findRolesByUser(id);
		model.addAttribute("list", roles);
		return "security/selectPermission";
	}

	@RequestMapping("/selectPermission")
	public @ResponseBody
	List<Map<String, Object>> selectPermission(@RequestParam(value = "edit_id", required = false) String id, Model model) {
		List<Map<String, Object>> permissions = StringUtils.isEmpty(id) ? userService.findAllPermissions()
				: userService.findPermissionsByUser(id);

		return permissions;
	}

	/**
	 * 根据用户id,构造组织机构 CaiFangfang 2013-10-31
	 */
	@RequestMapping("/selectOrganization")
	public @ResponseBody
	List<Map<String, Object>> selectOrganization(@RequestParam(value = "edit_id", required = false) String id,
			Model model) {
		List<Map<String, Object>> organizations = StringUtils.isEmpty(id) ? userService.findAllOrganization()
				: userService.findOrganizationByUser(id);
		return organizations;
	}

	@RequestMapping("unlock/{username}")
	public @ResponseBody
	Results unlock(@PathVariable("username") String username) {
		FILTER.resetAccountLock(username);
		return new Results(true);
	}

	@RequestMapping("checkLoginName")
	public @ResponseBody
	Results checkLoginName(@RequestParam(value = "id", required = false) String id, @RequestParam("name") String name) {
		boolean exists = StringUtils.isEmpty(id) ? userService.exists("loginName", name) : userService.exists(
				"loginNameAndIdNot", name, id);
		return new Results(!exists);
	}

	/**
	 * 修改密码
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("userpwdform")
	public String userpwdform(Model model) {
		ShiroUser user = SubjectUtils.getPrincipal();

		model.addAttribute("id", user.getId());// 用户名
		return "security/userpwdform";
	}

	/**
	 * 保存修改后的密码 CaiFangfang 2013-5-8
	 */
	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	public @ResponseBody
	Results changePassword(@Valid @PreModelAttribute User user) {

		String password = SubjectUtils.getPrincipal(ShiroUser.class).getPassword();

		String password2 = PasswordUtils.entryptPassword(user.getPassword(), user.getSalt());
		if (!StringUtils.equals(password, password2)) {
			return new Results(false, "旧密码输入错误");
		}

		userService.changePassword(user);
		return new Results(true, "密码修改成功");
	}
}
