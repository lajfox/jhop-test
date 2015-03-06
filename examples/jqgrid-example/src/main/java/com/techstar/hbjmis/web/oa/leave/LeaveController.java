package com.techstar.hbjmis.web.oa.leave;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.techstar.hbjmis.entity.oa.Leave;
import com.techstar.hbjmis.service.oa.leave.LeaveService;
import com.techstar.modules.activiti.service.EntityWorkflowService;
import com.techstar.modules.activiti.web.controller.WorkflowController;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.util.WebUtils;
import com.techstar.security.entity.User;

/**
 * 请假控制器，包含保存、启动流程
 * 
 * @author ZengWenfeng
 */
@Controller
@RequestMapping(value = "/oa/leave")
public class LeaveController extends WorkflowController<Leave, String> {

	private static final Map<String, String> allTypes = Maps.<String, String> newHashMap();

	@Autowired
	LeaveService leaveService;

	@Override
	public EntityWorkflowService<Leave, String> getWorkflowService() {
		return this.leaveService;
	}

	static {
		allTypes.put("公休", "公休");
		allTypes.put("病假", "病假");
		allTypes.put("调休", "调休");
		allTypes.put("事假", "事假");
		allTypes.put("婚假", "婚假");
	}

	@Override
	protected void listBefore(final Model model) {
		model.addAttribute("allTypes", WebUtils.toValue(allTypes));
	}

	@RequestMapping("search")
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Leave> spec) {
		Page<Leave> page = this.leaveService.findAll(spec, pageable);
		return new PageResponse<Leave>(page);
	}

	@Override
	protected void editBefore(final Leave leave, final String oper) {
		if (StringUtils.equals("add", oper)) {
			leave.setId(null);
			ShiroUser user = SubjectUtils.getPrincipal();
			leave.setUserId(user.getLoginName());
			leave.setApplyTime(new Date());
		}
	}

	@RequestMapping("user/search")
	public @ResponseBody
	Results usersearch() {
		List<User> list = this.leaveService.findBy(User.class, "idNot", SubjectUtils.getPrincipal(ShiroUser.class)
				.getId());
		return new Response<User>(list);
	}

}
