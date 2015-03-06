package com.techstar.security.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;
import com.techstar.modules.utils.Identities;
import com.techstar.security.entity.Organization;
import com.techstar.security.service.OrganizationService;

@Controller
@RequestMapping(value = "/security/organization")
public class OrganizationController {
    private static final Logger log = LoggerFactory.getLogger(Organization.class);

    @Autowired
    private OrganizationService organizationService;

    // 为空时，当路径值的为空时
    // 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
    @RequiresPermissions(value = { "admin", "organization:read" }, logical = Logical.OR)
    @RequestMapping("")
    public String list(Model model) {
	model.addAttribute("uuid", Identities.uuid2());
	return "security/organizationList";
    }

    // 查询
    // 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
    @RequiresPermissions(value = { "admin", "organization:read" }, logical = Logical.OR)
    @RequestMapping("/search")
    public @ResponseBody
    Results search(Specification<Organization> spec) {
	List<Organization> organizations = organizationService.findAll(spec, new Sort(Sort.Direction.ASC,"orderno"),  true);
	return new Response<Organization>(organizations);
    }

    /***
     * 数据维护
     * 
     * @Date 2013-5-3 上午11:33:33
     * @author lrm
     */
    @RequiresPermissions(value = { "admin", "organization:create", "organization:update" }, logical = Logical.OR)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public @ResponseBody
    Results edit(
	    @Valid @PreModelAttribute(disallowedFields = { "parent" }) Organization organization,
	    @RequestParam(value = "parentx", required = false) String parentid,
	    @RequestParam(value = "oper", required = false) String oper) {
	String message = "修改成功";
	if (StringUtils.equals("add", oper)) {
	    organization.setId(null);
	    message = "保存成功";
	}
	
	organizationService.saveOrganization(organization, oper, parentid);
	return new Results(true, message, organization);
    }

    /***
     * 数据删除
     * 
     * @Date 2013-5-28 下午3:03:00
     * @author lrm
     */
    @RequiresPermissions(value = { "admin", "organization:delete" }, logical = Logical.OR)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public @ResponseBody
    Results delete(@RequestParam("id") List<String> ids) {
	this.organizationService.delete(ids);
	return new Results(true);
    }

    /***
     * 选择上级
     * 
     * @Date 2013-5-3 上午11:33:59
     * @author lrm
     */
    @RequestMapping("/selectParent")
    public  @ResponseBody List<Map<String, Object>> selectParent(@RequestParam(value = "edit_id", required = false) String id, Model model) {
	List<Map<String, Object>> organizations = StringUtils.isEmpty(id) ? organizationService.findAllOrganizations()
		: organizationService.findByIdAndSonNot(id);
	return organizations;
    } 
    @RequestMapping("/selectParents")
    public  @ResponseBody List<Organization> selectParents(@RequestParam(value = "edit_id", required = false) String id, Model model,
    		@RequestParam(value = "parentId", required = false) String parentId) {
    	Sort sort = new Sort(Sort.Direction.ASC, "orderno");
	List<Organization> organizations = StringUtils.isEmpty(id) ? organizationService.findAll(sort)
		: organizationService.findBy("idNot", id);
	organization(organizations, parentId, true);
	return organizations;
    }
	private void organization(final List<Organization> organizations, final String parentId, final boolean checked) {
		if (CollectionUtils.isNotEmpty(organizations) && StringUtils.isNotEmpty(parentId)) {
			for (Organization p : organizations) {
				if (StringUtils.equals(p.getId(), parentId)) {
					p.setChecked(checked);
					p.setOpen(true);
					if (StringUtils.isNotEmpty(p.getParentId())) {
						organization(organizations, p.getParentId(), false);
					}
				}
			}
		}
	}
}
