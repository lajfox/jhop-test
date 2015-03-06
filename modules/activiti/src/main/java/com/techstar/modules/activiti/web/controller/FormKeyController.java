package com.techstar.modules.activiti.web.controller;

import java.io.Serializable;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.activiti.domain.ActivitiEntity;

/**
 * 流程－外置表单hanlder类
 * 
 * <p>
 * 1:generic/formkey/get/start/form/{processDefinitionKey}读取启动流程的表单内容
 * </p>
 * <p>
 * 2:generic/formkey/get/task/form/{taskId}读取Task的表单内容
 * </p>
 * 
 * @author sundoctor
 * 
 * @param <T>
 * @param <ID>
 * @see com.techstar.modules.activiti.web.controller.WorkflowController
 */
public abstract class FormKeyController<T extends ActivitiEntity, ID extends Serializable> extends
		WorkflowController<T, ID> {

	/**
	 * 读取启动流程的表单内容
	 */
	@RequestMapping(value = "generic/formkey/get/start/form/{processDefinitionKey}")
	@ResponseBody
	public Object findStartForm(@PathVariable("processDefinitionKey") String processDefinitionKey) {
		// 根据流程定义ID读取外置表单
		Object startForm = getWorkflowService().getWorkflowService().getRenderedStartFormByKey(processDefinitionKey);
		return startForm;
	}

	/**
	 * 读取Task的表单内容
	 */
	@RequestMapping(value = "generic/formkey/get/task/form/{taskId}")
	@ResponseBody
	public Object findTaskForm(@PathVariable("taskId") String taskId) {
		Object renderedTaskForm = getWorkflowService().getWorkflowService().getFormService()
				.getRenderedTaskForm(taskId);
		return renderedTaskForm;
	}
}
