package com.techstar.modules.activiti.web.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.activiti.domain.ActivitiEntity;
import com.techstar.modules.activiti.domain.ClaimInfo;
import com.techstar.modules.activiti.domain.StartInfo;
import com.techstar.modules.activiti.domain.Variable;
import com.techstar.modules.activiti.service.EntityWorkflowService;
import com.techstar.modules.activiti.util.VariableUtils;
import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.data.jpa.service.MyJpaService;
import com.techstar.modules.springframework.web.Controller.GenericController;
import com.techstar.modules.springframework.web.bind.annotation.PrePathVariable;
import com.techstar.modules.springframework.web.util.WebUtils;

/**
 * 流程处理Controller类
 * 
 * @author sundoctor
 * 
 *         <p>
 *         1:generic/start/{businessKey}/{processDefinitionKey} 启动流程
 *         </p>
 *         <p>
 *         子类可以复写 {@link WorkflowController#startBefore(ActivitiEntity, Map)}
 *         对流程启动前进行处理
 *         </p>
 *         <p>
 *         2:generic/saveAndStart/{businessKey}/{processDefinitionKey}
 *         保存业务并且启动流程
 *         </p>
 *         <p>
 *         子类可以复写
 *         {@link WorkflowController#saveAndStartBefore(ActivitiEntity, Map)}
 *         对流程启动前进行处理
 *         </p>
 *         <p>
 *         3:generic/task/claim/{businessKey}/{taskId} 签收任务
 *         </p>
 *         <p>
 *         子类可以复写 {@link WorkflowController#claimBefore(ActivitiEntity, Map)}
 *         对流程签收前进行处理
 *         </p>
 *         <p>
 *         4:generic/task/unclaim/{businessKey}/{taskId} 取消签收任务
 *         </p>
 *         <p>
 *         子类可以复写 {@link WorkflowController#unclaimBefore(ActivitiEntity, Map)}
 *         对流程取消签收前进行处理
 *         </p>
 *         <p>
 *         5:generic/task/vars/{businessKey}/{taskId} 读取任务变量数据
 *         </p>
 *         <p>
 *         6：generic/task/complete/{businessKey}/{taskId}
 *         </p>
 *         <p>
 *         子类可以复写 {@link WorkflowController#completeBefore(ActivitiEntity, Map)}
 *         对流程办理完成前进行处理
 *         </p>
 *         <p>
 *         7:generic/task/delegate/{businessKey}/{taskId}/{userId}
 *         委派任务，将任务委派给别一用户办理
 *         </p>
 *         <p>
 *         子类可以复写 {@link WorkflowController#delegateBefore(ActivitiEntity, Map)}
 *         对流程委派前进行处理
 *         </p>
 *         <p>
 *         8:generic/task/resolve/{businessKey}/{taskId} 取消委派任务
 *         </p>
 *         <p>
 *         子类可以复写 {@link WorkflowController#resolveBefore(ActivitiEntity, Map)}
 *         对流程取消委派前进行处理
 *         </p>
 *         <p>
 *         9:generic/submit/start/form/{businessKey}/{processDefinitionKey}
 *         启动表单流程
 *         </p>
 *         <p>
 *         子类可以复写
 *         {@link WorkflowController#submitStartFormBefore(ActivitiEntity, Map)}
 *         对流程启动进行前处理
 *         </p>
 *         <p>
 *         10:generic/submit/task/form/{businessKey}/{taskId} 提交流程任务表单
 *         </p>
 *         <p>
 *         子类可以复写
 *         {@link WorkflowController#submitTaskFormBefore(ActivitiEntity, Map)}
 *         对任务办理进行前处理
 *         </p>
 * 
 * @param <T>
 * @param <ID>
 */

public abstract class WorkflowController<T extends ActivitiEntity, ID extends Serializable> extends
		GenericController<T, ID> {

	private static final JsonMapper JSONMAPPER = JsonMapper.nonEmptyMapper();

	protected MyJpaService<T, ID> getService() {
		return getWorkflowService();
	}

	protected abstract EntityWorkflowService<T, ID> getWorkflowService();

	/**
	 * 启动流程
	 * 
	 * @param businessKey
	 *            业务主键，不能为空
	 * @param processDefinitionKey
	 *            流程唯一标识，不能为空
	 * @param leave
	 */
	@RequestMapping(value = "generic/start/{businessKey}/{processDefinitionKey}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results startWorkflow(@PrePathVariable("businessKey") T entity,
			@PathVariable("processDefinitionKey") String processDefinitionKey,
			@RequestBody(required = false) List<Variable> vars) {

		Map<String, Object> variables = VariableUtils.asMap(vars);

		startBefore(entity, variables);

		ProcessInstance processInstance = getWorkflowService().startProcessInstanceByKey(entity, processDefinitionKey,
				variables);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("business", entity);
		map.put("process", new StartInfo(entity));

		// return new Results(true, "流程已启动，流程实例ID：" + processInstance.getId(),
		// map);
		return new Results(true, "流程已启动");

	}

	/**
	 * 保存业务并且启动流程
	 * 
	 * @param businessKey
	 *            业务主键，不能为空
	 * @param processDefinitionKey
	 *            流程唯一标识，不能为空
	 * @param leave
	 */
	@RequestMapping(value = "generic/saveAndStart/{businessKey}/{processDefinitionKey}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results saveAndStartWorkflow(@PrePathVariable("businessKey") T entity,
			@PathVariable("processDefinitionKey") String processDefinitionKey,
			@RequestBody(required = false) List<Variable> vars) {

		Map<String, Object> variables = VariableUtils.asMap(vars);

		saveAndStartBefore(entity, variables);

		ProcessInstance processInstance = getWorkflowService().saveAndStartProcessInstanceByKey(entity,
				processDefinitionKey, variables);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("business", entity);
		map.put("process", new StartInfo(entity));

		// return new Results(true, "流程已启动，流程实例ID：" + processInstance.getId(),
		// map);
		return new Results(true, "流程已启动");

	}

	/**
	 * 签收任务
	 * 
	 * @param businessKey
	 *            业务主键，不能为空
	 * @param taskId
	 *            任务标识，不能为空
	 */

	@RequestMapping(value = "generic/task/claim/{businessKey}/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results claim(@PrePathVariable("businessKey") T entity, @PathVariable("taskId") String taskId) {

		String userId = SubjectUtils.getPrincipal(ShiroUser.class).getLoginName();

		claimBefore(entity, taskId);

		getWorkflowService().claim(entity, taskId, userId);
		return new Results(true, "签收任务成功", new ClaimInfo(userId, entity.getTasks()));
	}

	/**
	 * 取消签收任务
	 * 
	 * @param businessKey
	 *            业务主键，不能为空
	 * @param taskId
	 *            任务标识，不能为空
	 */

	@RequestMapping(value = "generic/task/unclaim/{businessKey}/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results unclaim(@PrePathVariable("businessKey") T entity, @PathVariable("taskId") String taskId) {

		unclaimBefore(entity, taskId);

		getWorkflowService().unclaim(entity, taskId);
		return new Results(true, "取消签收任务成功", new ClaimInfo(entity.getTasks()));
	}

	/**
	 * 读取任务详细数据
	 * 
	 * @param businessKey
	 *            业务主键，不能为空
	 * @param taskId
	 *            任务标识，不能为空
	 * @return
	 */
	@RequestMapping(value = "generic/task/vars/{businessKey}/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Results getLeaveWithVars(@PathVariable("businessKey") String businessKey,
			@PathVariable("taskId") String taskId) {

		T entity = getWorkflowService().findOne(this.getEntityClass(), businessKey);
		Map<String, Object> variables = getWorkflowService().getWorkflowService().getTaskService().getVariables(taskId);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("business", entity);
		map.put("process", variables);

		return new Results(map);
	}

	/**
	 * 完成任务
	 * 
	 * @param businessKey
	 *            业务主键，不能为空
	 * @param taskId
	 *            任务标识，不能为空
	 * @return
	 */

	@RequestMapping(value = "generic/task/complete/{businessKey}/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Results complete(@PrePathVariable("businessKey") T entity, @PathVariable("taskId") String taskId,
			@RequestBody(required = false) List<Variable> vars) {

		Map<String, Object> variables = VariableUtils.asMap(vars);
		completeBefore(entity, variables);

		getWorkflowService().complete(entity, taskId, variables);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("business", entity);
		map.put("process", new StartInfo(entity));

		// return new Results(true, "办理任务成功:taskId=" + taskId, map);
		return new Results(true, "办理任务成功", map);

	}

	/**
	 * 委派任务，将任务委派给别一用户办理
	 * 
	 * @param businessKey
	 *            业务主键，不能为空
	 * @param taskId
	 *            任务标识，不能为空
	 * @param userId
	 *            被委派人标识，不能为空
	 */
	@RequestMapping(value = "generic/task/delegate/{businessKey}/{taskId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results delegate(@PrePathVariable("businessKey") T entity, @PathVariable("taskId") String taskId,
			@PathVariable("userId") String userId) {

		delegateBefore(entity, taskId);

		getWorkflowService().delegateTask(entity, taskId, userId);
		return new Results(true, "委派任务成功", new ClaimInfo(userId, entity.getTasks()));
	}

	/**
	 * 取消委派任务
	 * 
	 * @param businessKey
	 *            业务主键，不能为空
	 * @param taskId
	 *            任务标识，不能为空
	 * @return
	 */

	@RequestMapping(value = "generic/task/resolve/{businessKey}/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Results resolve(@PrePathVariable("businessKey") T entity, @PathVariable("taskId") String taskId,
			@RequestBody(required = false) List<Variable> vars) {

		Map<String, Object> variables = VariableUtils.asMap(vars);
		resolveBefore(entity, variables);

		getWorkflowService().resolveTask(entity, taskId, variables);
		return new Results(true, "取消委派任务成功", new ClaimInfo(entity.getTasks()));

	}

	/**
	 * 启动表单流程
	 */
	@RequestMapping(value = "generic/submit/start/form/{businessKey}/{processDefinitionKey}", produces = MediaType.APPLICATION_JSON_VALUE)
	@SuppressWarnings("unchecked")
	public @ResponseBody
	Results submitStartFormAndStartProcessInstance(@PrePathVariable("businessKey") T entity,
			@PathVariable("processDefinitionKey") String processDefinitionKey, HttpServletRequest request) {

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

				formProperties.put(key, StringUtils.join(value, ","));

				if (MapUtils.isNotEmpty(jsonMap)) {
					String json = MapUtils.getString(jsonMap, key);
					if (StringUtils.isNotEmpty(json)) {
						Map<String, String> values = JSONMAPPER.fromJson(json, Map.class);

						int i = 0;
						String[] cns = new String[value.length];
						for (String v : value) {
							cns[i] = values.get(v);
							i++;
						}
						formProperties.put(key + "_text", StringUtils.join(cns, ","));
					}
				}
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		submitStartFormBefore(entity, formProperties);

		ProcessInstance processInstance = getWorkflowService().submitStartFormDataByKey(processDefinitionKey, entity,
				formProperties);

		logger.debug("start a processinstance: {}", processInstance);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("business", entity);
		map.put("process", new StartInfo(entity));

		// return new Results(true, "启动流程成功，流程ID：" + processInstance.getId(),
		// map);
		return new Results(true, "启动流程成功", map);
	}

	/**
	 * 提交task的并保存form
	 */
	@RequestMapping(value = "generic/submit/task/form/{businessKey}/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@SuppressWarnings("unchecked")
	public @ResponseBody
	Results submitTaskFormData(@PrePathVariable("businessKey") T entity, @PathVariable("taskId") String taskId,
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
				formProperties.put(key, StringUtils.join(value, ","));

				if (MapUtils.isNotEmpty(jsonMap)) {
					String json = MapUtils.getString(jsonMap, key);
					if (StringUtils.isNotEmpty(json)) {
						Map<String, String> values = JSONMAPPER.fromJson(json, Map.class);
						int i = 0;
						String[] cns = new String[value.length];
						for (String v : value) {
							cns[i] = values.get(v);
							i++;
						}
						formProperties.put(key + "_text", StringUtils.join(cns, ","));
					}
				}
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		submitTaskFormBefore(entity, formProperties);

		getWorkflowService().submitTaskFormData(entity, taskId, formProperties);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("business", entity);
		map.put("process", new StartInfo(entity));

		// return new Results(true, "办理任务成功:taskId=" + taskId, map);
		return new Results(true, "办理任务成功", map);
	}

	/**
	 * 流程启动表单提交前处理
	 * 
	 * @param entity
	 *            业务实体，不能为空
	 * @param formProperties
	 *            表单字段
	 */
	protected void submitStartFormBefore(final T entity, final Map<String, String> formProperties) {

	}

	/**
	 * 任务办理表单提交前处理
	 * 
	 * @param entity
	 *            业务实体，不能为空
	 * @param formProperties
	 *            表单字段
	 */
	protected void submitTaskFormBefore(final T entity, final Map<String, String> formProperties) {

	}

	/**
	 * 流程启动前处理
	 * 
	 * @param entity
	 *            业务实体
	 * @param variables
	 *            流程变量
	 */
	protected void startBefore(final T entity, final Map<String, Object> variables) {

	}

	/**
	 * 流程启动前处理
	 * 
	 * @param entity
	 *            业务实体
	 * @param variables
	 *            流程变量
	 */
	protected void saveAndStartBefore(final T entity, final Map<String, Object> variables) {

	}

	/**
	 * 委派任务前处理
	 * 
	 * @param entity
	 *            业务实体
	 * @param variables
	 *            流程变量
	 */
	protected void delegateBefore(final T entity, final String taskId) {

	}

	/**
	 * 签收任务前处理
	 * 
	 * @param entity
	 *            业务实体
	 * @param variables
	 *            流程变量
	 */
	protected void claimBefore(final T entity, final String taskId) {

	}

	/**
	 * 取消签收任务前处理
	 * 
	 * @param entity
	 *            业务实体
	 * @param variables
	 *            流程变量
	 */
	protected void unclaimBefore(final T entity, final String taskId) {

	}

	/**
	 * 任务完成前处理
	 * 
	 * @param entity
	 *            业务实体
	 * @param variables
	 *            流程变量
	 */
	protected void completeBefore(T entity, final Map<String, Object> variables) {

	}

	/**
	 * 被委派人完成任务前处理
	 * 
	 * @param entity
	 *            业务实体
	 * @param variables
	 *            流程变量
	 */
	protected void resolveBefore(T entity, final Map<String, Object> variables) {

	}
}
