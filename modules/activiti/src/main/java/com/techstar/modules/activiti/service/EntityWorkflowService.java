package com.techstar.modules.activiti.service;

import java.io.Serializable;
import java.util.Map;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

import com.techstar.modules.activiti.domain.ActivitiEntity;
import com.techstar.modules.springframework.data.jpa.service.MyJpaService;

/**
 * activiti 业务实体工作流服务接口
 * 
 * @author sundoctor
 * 
 * @param <T>
 * @param <ID>
 */
public interface EntityWorkflowService<T extends ActivitiEntity, ID extends Serializable> extends MyJpaService<T, ID> {

	/**
	 * 工作流服务接口
	 * 
	 * @return
	 */
	WorkflowService getWorkflowService();

	/**
	 * 启动动态表单流程
	 * 
	 * @param processDefinitionId
	 *            流程定义ID,不能为空.
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param properties
	 * @return ProcessInstance
	 * @see org.activiti.engine.FormService#submitStartFormData(String,String,
	 *      Map)
	 */
	ProcessInstance submitStartFormData(final String processDefinitionId, final String businessKey,
			final Map<String, String> properties);

	/**
	 * 启动动态表单流程
	 * 
	 * @param processDefinitionId
	 *            流程定义ID,不能为空.
	 * @param entity
	 *            业务实体,不能为空.
	 * @param properties
	 * @return ProcessInstance
	 * @see org.activiti.engine.FormService#submitStartFormData(String,String,
	 *      Map)
	 */
	public ProcessInstance submitStartFormData(final String processDefinitionId, final ActivitiEntity entity,
			final Map<String, String> properties);

	/**
	 * 启动动态表单流程
	 * 
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param properties
	 * @return ProcessInstance
	 * @see org.activiti.engine.FormService#submitStartFormData(String,String,
	 *      Map)
	 */
	ProcessInstance submitStartFormDataByKey(final String processDefinitionKey, final String businessKey,
			final Map<String, String> properties);

	/**
	 * 启动动态表单流程
	 * 
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @param entity
	 *            业务实体,不能为空.
	 * @param properties
	 * @return ProcessInstance
	 * @see org.activiti.engine.FormService#submitStartFormData(String,String,
	 *      Map)
	 */
	ProcessInstance submitStartFormDataByKey(final String processDefinitionKey, final ActivitiEntity entity,
			final Map<String, String> properties);

	/**
	 * 启动流程
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @return
	 * @see org.activiti.engine.RuntimeService#startProcessInstanceByKey(String,
	 *      String)
	 */
	ProcessInstance startProcessInstanceByKey(ActivitiEntity entity, String processDefinitionKey);

	/**
	 * 启动流程
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param processDefinitionKey
	 *            　流程定义KEY,不能为空.
	 * @param variables
	 *            流程参数 ，可以为null或者空
	 * @return
	 * @see org.activiti.engine.RuntimeService#startProcessInstanceByKey(String,
	 *      String,Map)
	 */
	ProcessInstance startProcessInstanceByKey(ActivitiEntity entity, String processDefinitionKey,
			Map<String, Object> variables);

	/**
	 * 启动流程
	 * 
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * 
	 * @return
	 * @see org.activiti.engine.RuntimeService#startProcessInstanceByKey(String,
	 *      String)
	 */
	ProcessInstance startProcessInstanceByKey(String businessKey, String processDefinitionKey);

	/**
	 * 启动流程
	 * 
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @param variables
	 *            流程参数 ，可以为null或者空
	 * @return
	 * @see org.activiti.engine.RuntimeService#startProcessInstanceByKey(String,
	 *      String)
	 */
	ProcessInstance startProcessInstanceByKey(String businessKey, String processDefinitionKey,
			Map<String, Object> variables);

	/**
	 * 保存业务并且启动流程
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param processDefinitionKey
	 *            流程定义KEY,不能为空.
	 * @return
	 * @see org.activiti.engine.RuntimeService#startProcessInstanceByKey(String,
	 *      String)
	 */
	ProcessInstance saveAndStartProcessInstanceByKey(ActivitiEntity entity, String processDefinitionKey);

	/**
	 * 保存业务并且启动流程
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param processDefinitionKey
	 *            　流程定义KEY,不能为空.
	 * @param variables
	 *            流程参数 ，可以为null或者空
	 * @return
	 * @see org.activiti.engine.RuntimeService#startProcessInstanceByKey(String,
	 *      String,Map)
	 */
	ProcessInstance saveAndStartProcessInstanceByKey(ActivitiEntity entity, String processDefinitionKey,
			Map<String, Object> variables);

	/**
	 * 启动流程后处理
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param processInstance
	 *            流程实例，不能为null
	 */
	void afterStart(final ActivitiEntity entity, final ProcessInstance processInstance);

	/**
	 * 办理任务
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
	 * 办理任务
	 * 
	 * 
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param variables
	 *            任务参数 ，可以为null或者空
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * @see org.activiti.engine.TaskService#complete(String, Map)
	 */
	void complete(String businessKey, String taskId, Map<String, Object> variables);

	/**
	 * 办理任务
	 * 
	 * 
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param taskId
	 *            任务标识,不能为空.
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * @see org.activiti.engine.TaskService#complete(String)
	 */
	void complete(String businessKey, String taskId);

	/**
	 * 办理任务
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param taskId
	 *            任务标识,不能为空.
	 * 
	 * @see org.activiti.engine.TaskService#complete(String)
	 */
	void complete(final ActivitiEntity entity, String taskId);

	/**
	 * 办理任务
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param variables
	 *            任务参数 ，可以为null或者空
	 * 
	 * @see org.activiti.engine.TaskService#complete(String, Map)
	 */
	void complete(final ActivitiEntity entity, String taskId, Map<String, Object> variables);

	/**
	 * 完成任务后处理
	 * 
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 */
	void afterComplete(String businessKey);

	/**
	 * 完成任务后处理
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 */
	public void afterComplete(final ActivitiEntity entity);

	/**
	 * 签收任务
	 * 
	 * 
	 * 
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID,不能为空.
	 * @see org.activiti.engine.TaskService#claim(String, String)
	 */
	void claim(final String taskId, final String userId);

	/**
	 * 签收任务
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID，不能为空
	 * @see org.activiti.engine.TaskService#claim(String, String)
	 */
	void claim(final String businessKey, final String taskId, final String userId);

	/**
	 * 签收任务
	 * 
	 * @param entity
	 *            业务实体，不能为null
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID，不能为空
	 * @see org.activiti.engine.TaskService#claim(String, String)
	 */
	void claim(final ActivitiEntity entity, final String taskId, final String userId);

	/**
	 * 取消签收任务
	 * 
	 * 
	 * 
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID,不能为空.
	 * @see org.activiti.engine.TaskService#unclaim(String)
	 */
	void unclaim(final String taskId);

	/**
	 * 取消签收任务
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID，不能为空
	 * @see org.activiti.engine.TaskService#unclaim(String)
	 */
	void unclaim(final String businessKey, final String taskId);

	/**
	 * 取消签收任务
	 * 
	 * @param entity
	 *            业务实体，不能为null
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID，不能为空
	 * @see org.activiti.engine.TaskService#unclaim(String)
	 */
	void unclaim(final ActivitiEntity entity, final String taskId);

	/**
	 * 委派任务，将任务委派给别一用户办理
	 * 
	 * 
	 * 
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID,不能为空.
	 * @see org.activiti.engine.TaskService#delegateTask(String, String)
	 */
	void delegateTask(final String taskId, final String userId);

	/**
	 * 委派任务，将任务委派给别一用户办理
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID，不能为空
	 * @see org.activiti.engine.TaskService#delegateTask(String, String)
	 */
	void delegateTask(final String businessKey, final String taskId, final String userId);

	/**
	 * 委派任务，将任务委派给别一用户办理
	 * 
	 * @param entity
	 *            业务实体，不能为null
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID，不能为空
	 * @see org.activiti.engine.TaskService#delegateTask(String, String)
	 */
	void delegateTask(final ActivitiEntity entity, final String taskId, final String userId);

	/**
	 * 取消委派任务，将任务退回给原主人
	 * 
	 * 
	 * 
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID,不能为空.
	 * @see org.activiti.engine.TaskService#resolveTask(String)
	 */
	void resolveTask(final String taskId);

	/**
	 * 取消委派任务，将任务退回给原主人
	 * 
	 * 
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param variables
	 *            任务参数 ，可以为null或者空
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * @see org.activiti.engine.TaskService#resolveTask(String, Map)
	 */
	void resolveTask(String taskId, Map<String, Object> variables);

	/**
	 * 取消委派任务，将任务退回给原主人
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID，不能为空
	 * @see org.activiti.engine.TaskService#resolveTask(String)
	 */
	void resolveTask(final String businessKey, final String taskId);

	/**
	 * 取消委派任务，将任务退回给原主人
	 * 
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param variables
	 *            任务参数 ，可以为null或者空
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * @see org.activiti.engine.TaskService#resolveTask(String, Map)
	 */
	void resolveTask(String businessKey, String taskId, Map<String, Object> variables);

	/**
	 * 取消委派任务，将任务退回给原主人
	 * 
	 * @param entity
	 *            业务实体，不能为null
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID，不能为空
	 * @see org.activiti.engine.TaskService#resolveTask(String)
	 */
	void resolveTask(final ActivitiEntity entity, final String taskId);

	/**
	 * 取消委派任务，将任务退回给原主人
	 * 
	 * @param entity
	 *            业务实体，不能为null
	 * @param taskId
	 *            任务标识,不能为空.
	 * @param userId
	 *            用户ID，不能为空
	 * @param variables
	 *            任务参数 ，可以为null或者空
	 * @see org.activiti.engine.TaskService#resolveTask(String,Map)
	 */
	void resolveTask(final ActivitiEntity entity, String taskId, Map<String, Object> variables);

	/**
	 * 办理动态表单任务节点
	 * 
	 * @param taskId
	 *            任务ＩＤ，不能为空
	 * @param properties
	 */
	void submitTaskFormData(final String taskId, final Map<String, String> properties);

	/**
	 * 办理动态表单任务节点
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param taskId
	 *            任务ＩＤ，不能为空
	 * @param properties
	 */
	void submitTaskFormData(final String businessKey, final String taskId, final Map<String, String> properties);

	/**
	 * 办理动态表单任务节点
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param taskId
	 *            任务ＩＤ，不能为空
	 * @param properties
	 */
	void submitTaskFormData(final ActivitiEntity entity, final String taskId, final Map<String, String> properties);

	/**
	 * 等待任务节点处理
	 * 
	 * 
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param activityId
	 *            等待节点标识,不能为空.
	 * @param variables
	 *            任务参数 ，可以为null或者空
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * @see RuntimeService#signal(String, Map)
	 */
	void signal(String businessKey, String activityId, Map<String, Object> variables);

	/**
	 * 等待任务节点处理
	 * 
	 * @param businessKey
	 *            业务记录标识,不能为空.
	 * @param activityId
	 *            等待节点标识,不能为空.
	 * @throws ActivitiObjectNotFoundException
	 *             当给定任务标识的任务不存在时抛出
	 * @see RuntimeService#signal(String)
	 */
	void signal(String businessKey, String activityId);

	/**
	 * 等待任务节点处理
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param activityId
	 *            等待节点标识,不能为空.
	 * 
	 * @see RuntimeService#signal(String)
	 */
	void signal(final ActivitiEntity entity, String activityId);

	/**
	 * 等待任务节点处理
	 * 
	 * @param entity
	 *            业务实体,不能为空.
	 * @param activityId
	 *            等待节点标识,不能为空.
	 * @param variables
	 *            任务参数 ，可以为null或者空
	 * 
	 * @see RuntimeService#signal(String, Map)
	 */
	void signal(final ActivitiEntity entity, String activityId, Map<String, Object> variables);

}
