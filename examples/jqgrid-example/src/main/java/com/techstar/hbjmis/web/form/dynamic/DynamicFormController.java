package com.techstar.hbjmis.web.form.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.activiti.domain.ProcessDefinitionItem;
import com.techstar.modules.activiti.domain.ProcessInstanceItem;
import com.techstar.modules.activiti.service.WorkflowService;
import com.techstar.modules.dozer.mapper.BeanMapper;
import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.util.WebUtils;
import com.techstar.modules.utils.Identities;

/**
 * 动态表单Controller
 * 
 * @author ZengWenfeng
 */
@Controller
@RequestMapping(value = "/form/dynamic")
public class DynamicFormController {

	private static final Logger logger = LoggerFactory.getLogger(DynamicFormController.class);
	private static final JsonMapper JSONMAPPER = JsonMapper.nonEmptyMapper();
	
	@Autowired
	private WorkflowService workflowService;

	

	/**
	 * 动态form流程列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "process-list", "" })
	public String processDefinitionList(Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		return "/form/dynamic/dynamic-form-process-list";
	}

	/**
	 * 动态form流程列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "process-list/search", "search" })
	public @ResponseBody
	Results processDefinitionSearch() {

		ProcessDefinitionQuery query1 = workflowService.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey("leave-dynamic-from").active().latestVersion().orderByDeploymentId().desc();
		List<ProcessDefinition> list = query1.list();

		ProcessDefinitionQuery query2 = workflowService.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey("dispatch").active().latestVersion().orderByDeploymentId().desc();
		List<ProcessDefinition> dispatchList = query2.list();

		list.addAll(dispatchList);

		return new Response<ProcessDefinitionItem>(BeanMapper.mapList(list, ProcessDefinitionItem.class));
	}

	/**
	 * 读取启动流程的表单字段
	 */
	@RequestMapping(value = "get-form/start/{processDefinitionKey}")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public Results findStartForm(@PathVariable("processDefinitionKey") String processDefinitionKey) {
		Map<String, Object> result = new HashMap<String, Object>();

		FormData startFormData = workflowService.getStartFormDataByKey(processDefinitionKey);

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

		result.put("form", startFormData);

		return new Results(result);
	}

	/**
	 * 读取Task的表单
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "get-form/task/{taskId}")
	@ResponseBody
	public Results findTaskForm(@PathVariable("taskId") String taskId) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		FormData taskFormData = workflowService.getTaskFormData(taskId);

		result.put("taskFormData", taskFormData);
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

	/**
	 * 提交task的并保存form
	 */
	@RequestMapping(value = "task/complete/{taskId}")
	@SuppressWarnings("unchecked")
	public @ResponseBody
	Results completeTask(@PathVariable("taskId") String taskId, HttpServletRequest request) {

		Map<String, Object> jsonMap = WebUtils.getParametersStartingWith(request, "json_");

		Map<String, String> formProperties = new HashMap<String, String>();

		// 从request中读取参数然后转换
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			// fp_的意思是form paremeter
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				key = key.split("_")[1];
				String[] value = entry.getValue();
				formProperties.put(key,  StringUtils.join(value,","));

				if (MapUtils.isNotEmpty(jsonMap)) {
					String json = MapUtils.getString(jsonMap, key);
					if (StringUtils.isNotEmpty(json)) {
						Map<String, String> values = JSONMAPPER.fromJson(json, Map.class);
						int i = 0;
						String[] cns = new String[value.length];
						for(String v:value){
							cns[i] = values.get(v);
							i++;
						}
						formProperties.put(key + "_text", StringUtils.join(cns,","));
					}
				}
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		workflowService.submitTaskFormData(taskId, formProperties);

		return new Results(true, "任务完成：taskId=" + taskId);
	}

	/**
	 * 读取启动流程的表单字段
	 */
	@RequestMapping(value = "start-process/{processDefinitionKey}")
	@SuppressWarnings("unchecked")
	public @ResponseBody
	Results submitStartFormAndStartProcessInstance(@PathVariable("processDefinitionKey") String processDefinitionKey,
			HttpServletRequest request) {

		Map<String, Object> jsonMap = WebUtils.getParametersStartingWith(request, "json_");

		Map<String, String> formProperties = new HashMap<String, String>();

		// 从request中读取参数然后转换
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			// fp_的意思是form paremeter
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				key = key.split("_")[1];
				String[] value = entry.getValue();

				formProperties.put(key, StringUtils.join(value,","));

				if (MapUtils.isNotEmpty(jsonMap)) {
					String json = MapUtils.getString(jsonMap, key);
					if (StringUtils.isNotEmpty(json)) {
						Map<String, String> values = JSONMAPPER.fromJson(json, Map.class);
						
						int i = 0;
						String[] cns = new String[value.length];
						for(String v:value){
							cns[i] = values.get(v);
							i++;
						}
						formProperties.put(key + "_text", StringUtils.join(cns,","));
					}
				}
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		ShiroUser user = SubjectUtils.getPrincipal();
		workflowService.getIdentityService().setAuthenticatedUserId(user.getLoginName());

		ProcessInstance processInstance = workflowService
				.submitStartFormDataByKey(processDefinitionKey, formProperties);
		logger.debug("start a processinstance: {}", processInstance);

		return new Results(true, "启动成功，流程ID：" + processInstance.getId());
	}

	/**
	 * task列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "task/list", method = RequestMethod.GET)
	public String taskList(Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		return "/form/dynamic/dynamic-form-task-list";
	}

	/**
	 * task列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "task/list", method = RequestMethod.POST)
	public @ResponseBody
	Results taskList() {

		ShiroUser user = SubjectUtils.getPrincipal();

		List<Task> tasks = new ArrayList<Task>();

		List<Task> list = this.workflowService.findTodoTasks(user.getLoginName(), "leave-dynamic-from");
		if (CollectionUtils.isNotEmpty(list)) {
			tasks.addAll(list);
		}

		list = this.workflowService.findTodoTasks(user.getLoginName(), "dispatch");
		if (CollectionUtils.isNotEmpty(list)) {
			tasks.addAll(list);
		}

		return new Response<Task>(tasks);
	}

	/**
	 * 签收任务
	 */
	@RequestMapping(value = "task/claim/{id}")
	public @ResponseBody
	Results claim(@PathVariable("id") String taskId) {
		ShiroUser user = SubjectUtils.getPrincipal();
		workflowService.getTaskService().claim(taskId, user.getLoginName());
		return new Results(true, "任务已签收", user.getLoginName());
	}

	/**
	 * 运行中的流程实例
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "process-instance/running/list", method = RequestMethod.GET)
	public String running(Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		return "/form/running-list";
	}

	/**
	 * 运行中的流程实例
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "process-instance/running/list", method = RequestMethod.POST)
	public @ResponseBody
	Results running() {

		ProcessInstanceQuery leaveDynamicQuery = workflowService.getRuntimeService().createProcessInstanceQuery()
				.processDefinitionKey("leave-dynamic-from").orderByProcessInstanceId().desc().active();
		List<ProcessInstance> list = leaveDynamicQuery.list();

		ProcessInstanceQuery dispatchQuery = workflowService.getRuntimeService().createProcessInstanceQuery()
				.processDefinitionKey("dispatch").active().orderByProcessInstanceId().desc();
		List<ProcessInstance> list2 = dispatchQuery.list();
		list.addAll(list2);

		return new Response<ProcessInstanceItem>(BeanMapper.mapList(list, ProcessInstanceItem.class));
	}

	/**
	 * 已结束的流程实例
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "process-instance/finished/list", method = RequestMethod.GET)
	public String finished(Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		return "/form/finished-list";
	}

	/**
	 * 已结束的流程实例
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "process-instance/finished/list", method = RequestMethod.POST)
	public @ResponseBody
	Results finished() {

		HistoricProcessInstanceQuery leaveDynamicQuery = workflowService.getHistoryService()
				.createHistoricProcessInstanceQuery().processDefinitionKey("leave-dynamic-from").finished()
				.orderByProcessInstanceEndTime().desc();
		List<HistoricProcessInstance> list = leaveDynamicQuery.list();

		HistoricProcessInstanceQuery dispatchQuery = workflowService.getHistoryService()
				.createHistoricProcessInstanceQuery().processDefinitionKey("dispatch").finished()
				.orderByProcessInstanceEndTime().desc();
		List<HistoricProcessInstance> list2 = dispatchQuery.list();
		list.addAll(list2);

		return new Response<HistoricProcessInstance>(list);
	}

}
