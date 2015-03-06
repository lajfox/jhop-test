package com.techstar.modules.activiti.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.activiti.domain.ActivitiEntity;
import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.springframework.data.jpa.domain.Results;

/**
 * 流程-动态表单hanlder类
 * <p>
 * 1:generic/dynamic/get/start/form/{processDefinitionKey}读取启动流程的表单字段
 * </p>
 * <p>
 * 2:generic/dynamic/get/task/form/{taskId}读取Task的表单字段
 * </p>
 * 
 * @author sundoctor
 * 
 * @param <T>
 * @param <ID>
 * @see com.techstar.modules.activiti.web.controller.WorkflowController
 */
public abstract class DynamicFormController<T extends ActivitiEntity, ID extends Serializable> extends
		WorkflowController<T, ID> {

	private static final JsonMapper JSONMAPPER = JsonMapper.nonEmptyMapper();

	/**
	 * 读取启动流程的表单字段
	 */
	@RequestMapping(value = "generic/dynamic/get/start/form/{processDefinitionKey}", produces = MediaType.APPLICATION_JSON_VALUE)
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Results getStartForm(@PathVariable("processDefinitionKey") String processDefinitionKey) {

		Map<String, Object> result = new HashMap<String, Object>();

		FormData startFormData = getWorkflowService().getWorkflowService().getStartFormDataByKey(processDefinitionKey);

		/*
		 * 读取enum类型数据，用于下拉框
		 */
		List<FormProperty> formProperties = startFormData.getFormProperties();
		for (FormProperty formProperty : formProperties) {
			Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
			if (values != null) {
				result.put(formProperty.getId(), values);
				result.put("json_" + formProperty.getId(), JSONMAPPER.toJson(values));
			}
		}

		result.put("formData", startFormData);

		return new Results(result);
	}

	/**
	 * 读取Task的表单字段
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "generic/dynamic/get/task/form/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Results getTaskForm(@PathVariable("taskId") String taskId) {

		Map<String, Object> result = new HashMap<String, Object>();
		FormData taskFormData = getWorkflowService().getWorkflowService().getTaskFormData(taskId);

		result.put("formData", taskFormData);
		/*
		 * 读取enum类型数据，用于下拉框
		 */
		List<FormProperty> formProperties = taskFormData.getFormProperties();
		for (FormProperty formProperty : formProperties) {
			Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
			if (values != null) {
				result.put(formProperty.getId(), values);
				result.put("json_" + formProperty.getId(), JSONMAPPER.toJson(values));
			}
		}

		return new Results(result);
	}

}
