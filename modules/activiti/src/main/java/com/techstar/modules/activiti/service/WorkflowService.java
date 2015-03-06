package com.techstar.modules.activiti.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.techstar.modules.activiti.spring.SpringProcessEngineConfiguration;

/**
 * activiti 工作流服务接口
 * 
 * @author sundoctor
 * 
 */
public interface WorkflowService {

	String USERID = "act_user_id";// 任务处理人ＩＤ
	String USERNAME = "act_user_name";// 任务处理人名称
	String ENTITYID = "act_entity_id";// 业务实体主键
	String ENTITYNAME = "act_entity_name";// 业务实体全路径名称

	/**
	 * 获取任务节点外置表单
	 * 
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @return 任务节点外置表单
	 * @see org.activiti.engine.FormService#getRenderedTaskForm(String)
	 */
	Object getRenderedStartFormByKey(final String processDefinitionKey);

	/**
	 * 获取任务节点表单数据
	 * 
	 * @param taskId
	 *            任务ＩＤ，不能为空
	 * @return 任务节点表单数据
	 * @see org.activiti.engine.FormService#getTaskFormData(String)
	 */
	FormData getTaskFormData(final String taskId);

	/**
	 * 办理动态表单任务节点
	 * 
	 * @param taskId
	 *            任务ＩＤ，不能为空
	 * @param properties
	 */
	void submitTaskFormData(final String taskId, final Map<String, String> properties);

	/**
	 * 获取启动节点表单数据
	 * 
	 * @param processDefinitionId
	 *            流程定义ID,不能为空.
	 * @return 启动节点表单数据
	 * @see org.activiti.engine.FormService#getStartFormData(String)
	 */
	FormData getStartFormData(final String processDefinitionId);

	/**
	 * 获取启动节点表单数据
	 * 
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @return 启动节点表单数据
	 */
	FormData getStartFormDataByKey(final String processDefinitionKey);

	/**
	 * 启动动态表单流程
	 * 
	 * @param processDefinitionId
	 *            流程定义ID,不能为空.
	 * @param properties
	 * @return ProcessInstance
	 * @see org.activiti.engine.FormService#submitStartFormData(String, Map)
	 */
	ProcessInstance submitStartFormData(final String processDefinitionId, final Map<String, String> properties);

	/**
	 * 启动动态表单流程
	 * 
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @param properties
	 * @return ProcessInstance
	 * @see #submitStartFormData(String, Map)
	 */
	ProcessInstance submitStartFormDataByKey(final String processDefinitionKey, final Map<String, String> properties);

	/**
	 * 启动流程
	 * 
	 * @param processDefinitionKey
	 *            　流程定义KEY,不能为空.
	 * @return ProcessInstance
	 * @see org.activiti.engine.RuntimeService#startProcessInstanceByKey(String,
	 *      String,Map)
	 */
	ProcessInstance startProcessInstanceByKey(String processDefinitionKey);

	/**
	 * 启动流程
	 * 
	 * @param processDefinitionKey
	 *            　流程定义KEY,不能为空.
	 * @param variables
	 *            流程参数 ，可以为null或者空
	 * @return
	 * @see org.activiti.engine.RuntimeService#startProcessInstanceByKey(String,
	 *      String,Map)
	 */
	ProcessInstance startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables);

	/**
	 * 办理任务
	 * 
	 * 
	 * @param taskId
	 *            任务标识,不能为空.
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * 
	 * @see org.activiti.engine.TaskService#complete(String)
	 */
	void complete(String taskId);

	/**
	 * 办理任务
	 * 
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param variables
	 *            任务参数 ，可以为null或者空
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * @see org.activiti.engine.TaskService#complete(String, Map)
	 */
	void complete(String taskId, Map<String, Object> variables);

	/**
	 * 取消委派任务，将任务退回给原主人
	 * 
	 * 
	 * @param taskId
	 *            任务标识,不能为空.
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * 
	 * @see org.activiti.engine.TaskService#complete(String)
	 */
	void resolveTask(String taskId);

	/**
	 * 取消委派任务，将任务退回给原主人
	 * 
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param variables
	 *            任务参数 ，可以为null或者空
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * @see org.activiti.engine.TaskService#complete(String, Map)
	 */
	void resolveTask(String taskId, Map<String, Object> variables);

	/**
	 * 查找流程历史活动轨迹
	 * 
	 * @param processInstanceId
	 *            流程实例ID,不能为空.
	 * @return 流程历史活动轨迹
	 */
	List<HistoricActivityInstance> findHistoricActivityInstances(final String processInstanceId);

	/**
	 * 查找流程历史活动轨迹
	 * 
	 * @param processInstanceId
	 *            流程实例ID,不能为空.
	 * @param actionVariableNames
	 *            处理动作变量名称集合.
	 * @param reasonVariableNames
	 *            处理意见变量名称集合.
	 * @return 流程历史活动轨迹
	 */
	List<HistoricActivityInstance> findHistoricActivityInstances(final String processInstanceId,
			final List<String> actionVariableNames, final List<String> reasonVariableNames);

	/**
	 * 查找当前任务处理角色
	 * 
	 * @param taskId
	 *            任务标识，不能为空
	 * @return 当前任务处理角色集合
	 */
	List<String> getTaskCandidateGroups(final String taskId);

	/**
	 * 查找当前任务处理用户
	 * 
	 * @param taskId
	 *            任务标识，不能为空
	 * @return 当前任务处理用户集合
	 */
	List<String> getTaskCandidateUsers(final String taskId);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @return 用户待办任务
	 */
	long countTodoTasks(String userId);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @param processDefinitionKey
	 *            　流程定义KEY,不能为空.
	 * @return 用户待办任务
	 */
	long countTodoTasks(String userId, String processDefinitionKey);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @return 用户待办任务
	 */
	List<Task> findTodoTasks(String userId);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @param includeProcessVariables
	 * @param includeTaskLocalVariables
	 * @return 用户待办任务
	 */
	List<Task> findTodoTasks(String userId, boolean includeProcessVariables, boolean includeTaskLocalVariables);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @param businessDesc
	 *            是否查询业务描述，true：查询，false：不查
	 * @return 用户待办任务
	 */
	List<Task> findTodoTasks(String userId, boolean businessDesc);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @param businessDesc
	 *            是否查询业务描述，true：查询，false：不查
	 * @param includeProcessVariables
	 * @param includeTaskLocalVariables
	 * @return 用户待办任务
	 */
	List<Task> findTodoTasks(String userId, boolean businessDesc, boolean includeProcessVariables,
			boolean includeTaskLocalVariables);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @param processDefinitionKey
	 *            　流程定义KEY,不能为空.
	 * @return 用户待办任务
	 */
	List<Task> findTodoTasks(String userId, String processDefinitionKey);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @param includeProcessVariables
	 * @param includeTaskLocalVariables
	 * @return 用户待办任务
	 */

	List<Task> findTodoTasks(String userId, String processDefinitionKey, boolean includeProcessVariables,
			boolean includeTaskLocalVariables);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @param processDefinitionKey
	 *            　流程定义KEY,不能为空.
	 * @param businessDesc
	 *            是否查询业务描述，true：查询，false：不查
	 * @return 用户待办任务
	 */
	List<Task> findTodoTasks(String userId, String processDefinitionKey, boolean businessDesc);

	/**
	 * 查询用户待办任务
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @param businessDesc
	 *            是否查询业务描述，true：查询，false：不查
	 * @param includeProcessVariables
	 * @param includeTaskLocalVariables
	 * @return 用户待办任务
	 */
	List<Task> findTodoTasks(String userId, String processDefinitionKey, boolean businessDesc,
			boolean includeProcessVariables, boolean includeTaskLocalVariables);

	/**
	 * 查询待办任务的业务主键
	 * 
	 * @param userId
	 *            用户标识，不能为空
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @return 待办任务的业务主键集合
	 */
	List<String> findTodoBusinessKeys(String userId, String processDefinitionKey);

	/**
	 * 等待任务节点处理
	 * 
	 * @param processInstanceId
	 *            流程定义ID,不能为空.
	 * @param activityId
	 *            等待任务节点ID,不能为空.
	 * @see RuntimeService#signal(String)
	 */
	void signal(String processInstanceId, String activityId);

	/**
	 * 等待任务节点处理
	 * 
	 * @param processInstanceId
	 *            流程定义ID,不能为空.
	 * @param activityId
	 *            等待任务节点ID,不能为空.
	 * @param processVariables
	 *            流程变量
	 * @see RuntimeService#signal(String, Map)
	 */
	void signal(String processInstanceId, String activityId, Map<String, Object> processVariables);

	/**
	 * 等待任务节点处理
	 * 
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @see RuntimeService#signal(String)
	 */
	void signalByKey(String processDefinitionKey);

	/**
	 * 等待任务节点处理
	 * 
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @param processVariables
	 *            流程变量
	 * @see RuntimeService#signal(String, Map)
	 */
	void signalByKey(String processDefinitionKey, Map<String, Object> processVariables);

	/* 工作流服务类 begin */
	RuntimeService getRuntimeService();

	TaskService getTaskService();

	HistoryService getHistoryService();

	RepositoryService getRepositoryService();

	IdentityService getIdentityService();

	FormService getFormService();

	SpringProcessEngineConfiguration getProcessEngineConfiguration();
	/* 工作流服务类 end */
}
