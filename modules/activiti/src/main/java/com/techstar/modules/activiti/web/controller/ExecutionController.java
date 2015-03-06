package com.techstar.modules.activiti.web.controller;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.techstar.modules.activiti.service.ExecutionService;
import com.techstar.modules.mybatis.domain.MyRowBounds;
import com.techstar.modules.mybatis.domain.Page;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.utils.Identities;

/**
 * 己部署流程定义Controller类
 * 
 * @author sundoctor
 * 
 */
@Controller
@RequestMapping(value = "/workflow/execution")
public class ExecutionController {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private ExecutionService executionService;

	/**
	 * 流程定义列表
	 * 
	 * @return 调转到流程定义列表页面
	 */
	@RequestMapping("")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("workflow/excutionList");
		mav.addObject("uuid", Identities.uuid2());
		return mav;
	}

	/**
	 * 查询流程模型
	 * 
	 * @param rowBounds
	 * @return Results
	 */
	@RequestMapping("search")
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) MyRowBounds rowBounds) {
		Page<Execution> page = executionService.findPageBy(rowBounds);
		return new PageResponse<Execution>(page);
	}

	/**
	 * 挂起、激活流程实例
	 * 
	 * @param state
	 *            流程定义状态
	 * @param processInstanceId
	 *            流程实例ID
	 */
	@RequestMapping(value = "/update/{state}")
	public @ResponseBody
	Results updateState(@PathVariable("state") int state, @RequestParam("processInstanceId") String processInstanceId) {
		if (state == 2) {
			runtimeService.activateProcessInstanceById(processInstanceId);
		} else {
			runtimeService.suspendProcessInstanceById(processInstanceId);
		}
		return new Results(true, state == 2 ? 1 : 2);
	}

}
