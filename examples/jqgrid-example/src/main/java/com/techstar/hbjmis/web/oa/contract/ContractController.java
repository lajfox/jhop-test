package com.techstar.hbjmis.web.oa.contract;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.hbjmis.entity.oa.Contract;
import com.techstar.hbjmis.service.oa.contract.ContractService;
import com.techstar.modules.activiti.service.EntityWorkflowService;
import com.techstar.modules.activiti.web.controller.FormKeyController;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.security.entity.User;

/**
 * 合同管理，外置表单演示
 * 
 * @author ZengWenfeng
 */
@Controller
@RequestMapping(value = "/oa/contract")
public class ContractController extends FormKeyController<Contract, String> {

	@Autowired
	ContractService contractService;

	@Override
	public EntityWorkflowService<Contract, String> getWorkflowService() {
		return this.contractService;
	}

	@RequestMapping("search")
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Contract> spec) {
		Page<Contract> page = this.contractService.findAll(spec, pageable);
		return new PageResponse<Contract>(page);
	}

	@Override
	protected void editBefore(final Contract contract, final String oper) {
		if (StringUtils.equals("add", oper)) {
			contract.setId(null);
			ShiroUser user = SubjectUtils.getPrincipal();
			contract.setUserName(user.getName());
			contract.setCreateTime(new Date());
		}
	}

	@RequestMapping("user/search")
	public @ResponseBody
	Results usersearch() {
		List<User> list = this.contractService.findBy(User.class, "idNot", SubjectUtils.getPrincipal(ShiroUser.class)
				.getId());
		return new Response<User>(list);
	}

}
