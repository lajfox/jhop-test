package com.techstar.sys.web;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;
import com.techstar.sys.entity.SystemDicType;
import com.techstar.sys.service.SystemDicTypeService;

/**
 * 系统字典
 * 
 * @Date 2012-11-2 下午2:06:05
 * @author lrm
 */
@Controller
@RequestMapping(value = "/zd/sysdictype")
public class SystemDicTypeController {

	private static Logger logger = LoggerFactory.getLogger(SystemDicTypeController.class);

	@Autowired
	private SystemDicTypeService systemDicTypeService;

	// 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
	@RequestMapping(value = "")
	public String list(Model model) {
		return "sys/sysDicTypeItem-layout";
	}

	@RequestMapping(value = "{path1}/{path2}")
	public String list(Model model, @PathVariable("path1") String path1, @PathVariable("path2") String path2) {
		return path1 + "/" + path2;
	}

	/***
	 * TODO 查询列表
	 * 
	 * @Date 2013-1-24 上午10:54:03
	 * @author lrm
	 */
	@RequiresPermissions(value = { "admin", "systemDicType:read" }, logical = Logical.OR)
	@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<SystemDicType> spc) {
		Page<SystemDicType> systemDicType = systemDicTypeService.findAll(spc, pageable);
		return new PageResponse<SystemDicType>(systemDicType);
	}

	/***
	 * TODO 数据编辑维护
	 * 
	 * @Date 2013-1-21 上午9:38:02
	 * @author lrm
	 */
	@RequiresPermissions(value = { "admin", "systemDicType:create", "systemDicType:update", "systemDicType:delete" }, logical = Logical.OR)
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public @ResponseBody
	Results edit(@Valid @PreModelAttribute SystemDicType systemDicType,
			@RequestParam(value = "oper", required = false) String oper) {
		String message = "修改成功";
		if (StringUtils.equals("del", oper)) {
			this.systemDicTypeService.delete(systemDicType);
		} else {
			if (StringUtils.equals("add", oper)) {
				systemDicType.setId(null);
				message = "保存成功";
			}
			systemDicTypeService.save(systemDicType);
		}
		return new Results(true, message, systemDicType);
	}
}
