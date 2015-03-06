package com.techstar.modules.activiti.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.activiti.domain.ActivitiEntity;
import com.techstar.modules.activiti.domain.TaskItem;
import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.service.MyJpaServiceImpl;
import com.techstar.modules.utils.Collections3;

@Transactional(readOnly = true)
public abstract class EntityWorkflowServiceImpl<T extends ActivitiEntity, ID extends Serializable> extends
		MyJpaServiceImpl<T, ID> implements EntityWorkflowService<T, ID> {

	private static final JsonMapper JSONMAPPER = JsonMapper.nonEmptyMapper();

	@Autowired
	@Qualifier("workflowService")
	private WorkflowService workflowService;

	@Override
	public WorkflowService getWorkflowService() {
		return workflowService;
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance submitStartFormData(final String processDefinitionId, final String businessKey,
			final Map<String, String> properties) {

		ActivitiEntity entity = this.findOne(this.getMyJpaRepository().getDomainClass(), businessKey);
		return submitStartFormData(processDefinitionId, entity, properties);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance submitStartFormData(final String processDefinitionId, final ActivitiEntity entity,
			final Map<String, String> properties) {

		ShiroUser user = SubjectUtils.getPrincipal();
		properties.put(WorkflowService.USERID, user.getLoginName());
		properties.put(WorkflowService.USERNAME, user.getName());

		properties.put(WorkflowService.ENTITYID, entity.getId());
		properties.put(WorkflowService.ENTITYNAME, entity.getClass().getName());

		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		workflowService.getIdentityService().setAuthenticatedUserId(user.getLoginName());

		ProcessInstance processInstance = workflowService.getFormService().submitStartFormData(processDefinitionId,
				entity.getId(), properties);

		afterStart(entity, processInstance);

		return processInstance;

	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance submitStartFormDataByKey(final String processDefinitionKey, final String businessKey,
			final Map<String, String> properties) {
		ProcessDefinition processDefinition = workflowService.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).latestVersion().active().singleResult();
		if (processDefinition == null) {
			throw new ActivitiObjectNotFoundException("No processDefinition found for processDefinitionKey '"
					+ processDefinitionKey + "'", ProcessDefinition.class);
		}

		return submitStartFormData(processDefinition.getId(), businessKey, properties);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance submitStartFormDataByKey(final String processDefinitionKey, final ActivitiEntity entity,
			final Map<String, String> properties) {
		ProcessDefinition processDefinition = workflowService.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).latestVersion().active().singleResult();
		if (processDefinition == null) {
			throw new ActivitiObjectNotFoundException("No processDefinition found for processDefinitionKey '"
					+ processDefinitionKey + "'", ProcessDefinition.class);
		}

		return submitStartFormData(processDefinition.getId(), entity, properties);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance saveAndStartProcessInstanceByKey(ActivitiEntity entity, String processDefinitionKey) {
		return saveAndStartProcessInstanceByKey(entity, processDefinitionKey, null);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance saveAndStartProcessInstanceByKey(ActivitiEntity entity, String processDefinitionKey,
			Map<String, Object> variables) {

		this.save(ActivitiEntity.class, entity);

		if (StringUtils.isEmpty(entity.getProcessInstanceId())) {

			String businessKey = entity.getId();
			ProcessInstance processInstance = startProcessInstanceByKey(businessKey, processDefinitionKey, variables);

			afterStart(entity, processInstance);

			logger.info("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[] {
					processDefinitionKey, businessKey, processInstance.getId(), variables });

			return processInstance;
		}

		return null;

	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance startProcessInstanceByKey(String businessKey, String processDefinitionKey) {
		return startProcessInstanceByKey(businessKey, processDefinitionKey, null);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance startProcessInstanceByKey(String businessKey, String processDefinitionKey,
			Map<String, Object> variables) {
		ActivitiEntity entity = this.findOne(this.getMyJpaRepository().getDomainClass(), businessKey);
		return startProcessInstanceByKey(entity, processDefinitionKey, variables);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance startProcessInstanceByKey(ActivitiEntity entity, String processDefinitionKey) {
		return startProcessInstanceByKey(entity, processDefinitionKey, null);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance startProcessInstanceByKey(ActivitiEntity entity, String processDefinitionKey,
			Map<String, Object> variables) {

		if (variables == null) {
			variables = new HashMap<String, Object>();
		}

		String businessKey = entity.getId();
		variables.put(WorkflowService.ENTITYID, businessKey);
		variables.put(WorkflowService.ENTITYNAME, entity.getClass().getName());

		ShiroUser user = SubjectUtils.getPrincipal();
		variables.put(WorkflowService.USERID, user.getLoginName());
		variables.put(WorkflowService.USERNAME, user.getName());

		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		this.getWorkflowService().getIdentityService().setAuthenticatedUserId(user.getLoginName());

		ProcessInstance processInstance = this.getWorkflowService().getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);

		afterStart(entity, processInstance);

		logger.info("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[] { processDefinitionKey,
				businessKey, processInstance.getId(), variables });

		return processInstance;

	}

	@Override
	@Transactional(readOnly = false)
	public void afterStart(final ActivitiEntity entity, final ProcessInstance processInstance) {

		String processInstanceId = processInstance.getId();
		entity.setProcessInstanceId(processInstanceId);
		entity.setProcessDefinitionId(processInstance.getProcessDefinitionId());
		entity.setProcessInstanceStartTime(new Date());

		ProcessDefinition processDefinition = workflowService.getRepositoryService().createProcessDefinitionQuery()
				.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		entity.setProcessDefinitionKey(processDefinition.getKey());
		entity.setProcessDefinitionName(processDefinition.getName());

		updateActivitiEntity(entity);

	}

	@Override
	@Transactional(readOnly = false)
	public void complete(String taskId) {
		complete(taskId, (Map<String, Object>) null);
	}

	@Override
	@Transactional(readOnly = false)
	public void complete(String taskId, Map<String, Object> variables) {

		Task task = workflowService.getTaskService().createTaskQuery().taskId(taskId).singleResult();

		ProcessInstance processInstance = workflowService.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		String businessKey = processInstance.getBusinessKey();

		complete(businessKey, taskId, variables);
	}

	@Override
	@Transactional(readOnly = false)
	public void complete(String businessKey, String taskId) {
		complete(businessKey, taskId, null);
	}

	@Override
	@Transactional(readOnly = false)
	public void complete(String businessKey, String taskId, Map<String, Object> variables) {
		workflowService.complete(taskId, variables);
		afterComplete(businessKey);
	}

	@Override
	@Transactional(readOnly = false)
	public void complete(final ActivitiEntity entity, String taskId) {
		complete(entity, taskId, null);
	}

	@Override
	@Transactional(readOnly = false)
	public void complete(final ActivitiEntity entity, String taskId, Map<String, Object> variables) {
		workflowService.complete(taskId, variables);
		afterComplete(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void afterComplete(final ActivitiEntity entity) {

		long count = workflowService.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(entity.getProcessInstanceId()).count();

		if (count <= 0) {
			entity.setTasks(null);
			entity.setProcessInstanceEndTime(new Date());
		} else {
			updateActivitiEntity(entity);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void afterComplete(String businessKey) {
		ActivitiEntity entity = findOne(this.getMyJpaRepository().getDomainClass(), businessKey);
		afterComplete(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void claim(final String taskId, final String userId) {

		Task task = workflowService.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance processInstance = workflowService.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		String businessKey = processInstance.getBusinessKey();

		claim(businessKey, taskId, userId);
	}

	@Override
	@Transactional(readOnly = false)
	public void claim(final String businessKey, final String taskId, final String userId) {
		ActivitiEntity entity = findOne(this.getMyJpaRepository().getDomainClass(), businessKey);
		claim(entity, taskId, userId);
	}

	@Override
	@Transactional(readOnly = false)
	public void claim(final ActivitiEntity entity, final String taskId, final String userId) {
		workflowService.getTaskService().claim(taskId, userId);
		updateActivitiEntity(entity, true);
	}

	@Override
	@Transactional(readOnly = false)
	public void unclaim(final String taskId) {

		Task task = workflowService.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance processInstance = workflowService.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		String businessKey = processInstance.getBusinessKey();

		unclaim(businessKey, taskId);
	}

	@Override
	@Transactional(readOnly = false)
	public void unclaim(final String businessKey, final String taskId) {
		ActivitiEntity entity = findOne(this.getMyJpaRepository().getDomainClass(), businessKey);
		unclaim(entity, taskId);
	}

	@Override
	@Transactional(readOnly = false)
	public void unclaim(final ActivitiEntity entity, final String taskId) {
		workflowService.getTaskService().unclaim(taskId);
		updateActivitiEntity(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void delegateTask(final String taskId, final String userId) {

		Task task = workflowService.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance processInstance = workflowService.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		String businessKey = processInstance.getBusinessKey();

		delegateTask(businessKey, taskId, userId);
	}

	@Override
	@Transactional(readOnly = false)
	public void delegateTask(final String businessKey, final String taskId, final String userId) {
		ActivitiEntity entity = findOne(this.getMyJpaRepository().getDomainClass(), businessKey);
		delegateTask(entity, taskId, userId);
	}

	@Override
	@Transactional(readOnly = false)
	public void delegateTask(final ActivitiEntity entity, final String taskId, final String userId) {
		workflowService.getTaskService().delegateTask(taskId, userId);
		updateActivitiEntity(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void resolveTask(final String taskId) {
		resolveTask(taskId, (Map<String, Object>) null);
	}

	@Override
	@Transactional(readOnly = false)
	public void resolveTask(String taskId, Map<String, Object> variables) {

		Task task = workflowService.getTaskService().createTaskQuery().taskId(taskId).singleResult();

		ProcessInstance processInstance = workflowService.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		String businessKey = processInstance.getBusinessKey();

		resolveTask(businessKey, taskId, variables);
	}

	@Override
	@Transactional(readOnly = false)
	public void resolveTask(final String businessKey, final String taskId) {
		resolveTask(businessKey, taskId, null);
	}

	@Override
	@Transactional(readOnly = false)
	public void resolveTask(String businessKey, String taskId, Map<String, Object> variables) {
		ActivitiEntity entity = this.findOne(this.getMyJpaRepository().getDomainClass(), businessKey);
		resolveTask(entity, taskId, variables);
	}

	@Override
	@Transactional(readOnly = false)
	public void resolveTask(final ActivitiEntity entity, final String taskId) {
		resolveTask(entity, taskId, null);
	}

	@Override
	@Transactional(readOnly = false)
	public void resolveTask(final ActivitiEntity entity, String taskId, Map<String, Object> variables) {
		workflowService.resolveTask(taskId, variables);
		updateActivitiEntity(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void submitTaskFormData(final String taskId, final Map<String, String> properties) {

		Task task = workflowService.getTaskService().createTaskQuery().taskId(taskId).singleResult();

		ProcessInstance processInstance = workflowService.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		String businessKey = processInstance.getBusinessKey();

		submitTaskFormData(businessKey, taskId, properties);
	}

	@Override
	@Transactional(readOnly = false)
	public void submitTaskFormData(final String businessKey, final String taskId, final Map<String, String> properties) {
		workflowService.submitTaskFormData(taskId, properties);
		afterComplete(businessKey);
	}

	@Override
	@Transactional(readOnly = false)
	public void submitTaskFormData(final ActivitiEntity entity, final String taskId,
			final Map<String, String> properties) {
		workflowService.submitTaskFormData(taskId, properties);
		afterComplete(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void signal(String businessKey, String activityId) {
		signal(businessKey, activityId, null);
	}

	@Override
	@Transactional(readOnly = false)
	public void signal(String businessKey, String activityId, Map<String, Object> variables) {
		ActivitiEntity entity = this.findOne(this.getMyJpaRepository().getDomainClass(), businessKey);
		signal(entity, activityId, variables);
	}

	@Override
	@Transactional(readOnly = false)
	public void signal(final ActivitiEntity entity, String activityId) {
		signal(entity, activityId, null);
	}

	@Override
	@Transactional(readOnly = false)
	public void signal(final ActivitiEntity entity, String activityId, Map<String, Object> variables) {
		workflowService.signal(entity.getProcessInstanceId(), activityId, variables);
		afterComplete(entity);
	}

	/**
	 * 删除业务数据时同时删除流程实例
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(T entity) {
		// 删除业务数据
		super.delete(entity);

		// 删除流程实例
		Collection<T> entities = new ArrayList<T>();
		entities.add(entity);
		deleteAllProcessInstance(entities);

	}

	/**
	 * 删除业务数据时同时删除流程实例
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(ID id) {
		T entity = (T) this.findOne(id);
		delete(entity);
	}

	/**
	 * 删除业务数据时同时删除流程实例
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Iterable<? extends T> entities) {
		// 删除业务数据
		super.delete(entities);
		// 删除流程实例
		deleteAllProcessInstance(entities);
	}

	/**
	 * 删除业务数据时同时删除流程实例
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Collection<ID> ids) {
		Iterable<T> entities = this.findAll(ids);
		delete(entities);
	}

	/**
	 * 删除业务数据时同时删除流程实例
	 */
	@Override
	@Transactional(readOnly = false)
	public int deleteInBatch(Collection<ID> ids) {

		Iterable<T> entities = this.findAll(ids);
		// 删除业务数据
		int i = super.deleteInBatch(ids);
		// 删除流程实例
		deleteAllProcessInstance(entities);

		return i;
	}

	/**
	 * 删除业务数据时同时删除流程实例
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteInBatch(Iterable<T> entities) {
		// 删除业务数据
		super.deleteInBatch(entities);
		// 删除流程实例
		deleteAllProcessInstance(entities);
	}

	/**
	 * 删除业务数据时同时删除流程实例
	 */
	@Override
	@Transactional(readOnly = false)
	public void deleteAllInBatch() {
		List<T> entities = this.findAll();
		deleteInBatch(entities);
	}

	/**
	 * 删除业务流程实例
	 * 
	 * @param entities
	 *            业务数据集合
	 */
	private void deleteAllProcessInstance(Iterable<? extends T> entities) {
		for (T entity : entities) {
			// 删除流程实例
			if (StringUtils.isNotEmpty(entity.getProcessInstanceId())) {
				long count = workflowService.getRuntimeService().createProcessInstanceQuery()
						.processInstanceId(entity.getProcessInstanceId()).count();

				if (count > 0) {
					// 实例运行中
					workflowService.getRuntimeService().deleteProcessInstance(entity.getProcessInstanceId(), "业务数据己删除");
					workflowService.getHistoryService().deleteHistoricProcessInstance(entity.getProcessInstanceId());
				} else {
					// 实例己结束，从历史表中删除
					count = workflowService.getHistoryService().createHistoricProcessInstanceQuery()
							.processInstanceId(entity.getProcessInstanceId()).count();
					if (count > 0) {
						workflowService.getHistoryService()
								.deleteHistoricProcessInstance(entity.getProcessInstanceId());
					}
				}
			}
		}
	}

	private void updateActivitiEntity(final ActivitiEntity entity) {
		updateActivitiEntity(entity, false);
	}

	/**
	 * 更新业务实体
	 * 
	 * @param entity
	 *            　业务实体，不能为空
	 * @param processInstanceId
	 *            　流程实例ID，不能为空
	 */
	private void updateActivitiEntity(final ActivitiEntity entity, final boolean claim) {

		List<Task> tasks = workflowService.getTaskService().createTaskQuery()
				.processInstanceId(entity.getProcessInstanceId()).active().orderByTaskCreateTime().desc().list();

		if (CollectionUtils.isNotEmpty(tasks)) {

			UserEntity user = null;
			List<IdentityLink> links = null;
			List<String> candidateGroups = null, candidateUsers = null;
			TaskItem taskItem = null;
			List<TaskItem> taskItems = new ArrayList<TaskItem>(tasks.size());
			for (Task task : tasks) {

				taskItem = new TaskItem();
				taskItem.setId(task.getId());
				taskItem.setAssignee(task.getAssignee());
				taskItem.setCreateTime(task.getCreateTime());
				taskItem.setDelegationState(task.getDelegationState());
				taskItem.setDescription(task.getDescription());
				taskItem.setDueDate(task.getDueDate());
				taskItem.setExecutionId(task.getExecutionId());
				taskItem.setName(task.getName());
				taskItem.setOwner(task.getOwner());
				taskItem.setParentTaskId(task.getParentTaskId());
				taskItem.setPriority(task.getPriority());
				taskItem.setProcessDefinitionId(task.getProcessDefinitionId());
				taskItem.setProcessInstanceId(task.getProcessInstanceId());
				taskItem.setSuspended(task.isSuspended());
				taskItem.setTaskDefinitionKey(task.getTaskDefinitionKey());
				taskItem.setClaim(claim);

				if (StringUtils.isEmpty(task.getAssignee())) {

					links = workflowService.getTaskService().getIdentityLinksForTask(task.getId());
					if (CollectionUtils.isNotEmpty(links)) {
						candidateGroups = Collections3.extractNotEmptyToList(links, "groupId");
						taskItem.setCandidateGroups(CollectionUtils.isEmpty(candidateGroups) ? null : candidateGroups);

						candidateUsers = Collections3.extractNotEmptyToList(links, "userId");
						taskItem.setCandidateUsers(CollectionUtils.isEmpty(candidateUsers) ? null : candidateUsers);
					}
				} else {
					// 将处理人登录名改为姓名显示
					user = this.getWorkflowService().getProcessEngineConfiguration().getCustomIdentityService()
							.findUserById(task.getAssignee());
					taskItem.setDisplayAssignee(user != null && StringUtils.isNotEmpty(user.getLastName()) ? user
							.getLastName() : task.getAssignee());
				}

				taskItems.add(taskItem);
			}

			entity.setTasks(JSONMAPPER.toJson(taskItems));
		}
	}

}
