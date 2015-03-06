package com.techstar.modules.activiti.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.impl.form.StartFormDataImpl;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.activiti.collections.HistoricActivityInstanceComparator;
import com.techstar.modules.activiti.collections.HistoricActivityPredicate;
import com.techstar.modules.activiti.collections.HistoricDetailPredicate;
import com.techstar.modules.activiti.domain.HistoricActivityInstanceItem;
import com.techstar.modules.activiti.domain.ProcessDefinitionItem;
import com.techstar.modules.activiti.domain.ProcessInstanceItem;
import com.techstar.modules.activiti.domain.TaskItem;
import com.techstar.modules.activiti.domain.Todo;
import com.techstar.modules.activiti.spring.SpringProcessEngineConfiguration;
import com.techstar.modules.activiti.util.HistoricVariableUpdateUtils;
import com.techstar.modules.dozer.mapper.BeanMapper;
import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.utils.Collections3;

@Service("workflowService")
@Transactional(readOnly = true)
public class WorkflowServiceImpl implements WorkflowService, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);
	private static final JsonMapper JSONMAPPER = JsonMapper.nonEmptyMapper();

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private FormService formService;

	@Autowired
	private SpringProcessEngineConfiguration processEngineConfiguration;

	private Repositories repositories;

	public void setApplicationContext(ApplicationContext context) {
		this.repositories = new Repositories(context);
	}

	@Override
	public Object getRenderedStartFormByKey(final String processDefinitionKey) {

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).latestVersion().active().singleResult();
		if (processDefinition == null) {
			throw new ActivitiObjectNotFoundException("No processDefinition found for processDefinitionKey '"
					+ processDefinitionKey + "'", ProcessDefinition.class);
		}

		return formService.getRenderedStartForm(processDefinition.getId());
	}

	@Override
	public FormData getTaskFormData(final String taskId) {
		TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);
		taskFormData.setTask(null);
		return taskFormData;
	}

	@Override
	@Transactional(readOnly = false)
	public void submitTaskFormData(final String taskId, final Map<String, String> properties) {

		ShiroUser user = SubjectUtils.getPrincipal();
		properties.put(USERID, user.getLoginName());
		properties.put(USERNAME, user.getName());

		formService.submitTaskFormData(taskId, properties);
	}

	@Override
	public FormData getStartFormData(final String processDefinitionId) {
		StartFormDataImpl startFormData = (StartFormDataImpl) formService.getStartFormData(processDefinitionId);
		startFormData.setProcessDefinition(null);
		return startFormData;
	}

	@Override
	public FormData getStartFormDataByKey(final String processDefinitionKey) {

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).latestVersion().active().singleResult();
		if (processDefinition == null) {
			throw new ActivitiObjectNotFoundException("No processDefinition found for processDefinitionKey '"
					+ processDefinitionKey + "'", ProcessDefinition.class);
		}

		return getStartFormData(processDefinition.getId());
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance submitStartFormData(final String processDefinitionId, final Map<String, String> properties) {
		ShiroUser user = SubjectUtils.getPrincipal();
		properties.put(USERID, user.getLoginName());
		properties.put(USERNAME, user.getName());

		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(user.getLoginName());

		return formService.submitStartFormData(processDefinitionId, properties);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance submitStartFormDataByKey(final String processDefinitionKey,
			final Map<String, String> properties) {

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey).latestVersion().active().singleResult();
		if (processDefinition == null) {
			throw new ActivitiObjectNotFoundException("No processDefinition found for processDefinitionKey '"
					+ processDefinitionKey + "'", ProcessDefinition.class);
		}

		return submitStartFormData(processDefinition.getId(), properties);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance startProcessInstanceByKey(String processDefinitionKey) {
		return startProcessInstanceByKey(processDefinitionKey, (Map<String, Object>) null);
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables) {

		if (variables == null) {
			variables = new HashMap<String, Object>();
		}

		ShiroUser user = SubjectUtils.getPrincipal();
		variables.put(USERID, user.getLoginName());
		variables.put(USERNAME, user.getName());

		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(user.getLoginName());

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);

		logger.info("start process of {key={},  pid={}, variables={}}", new Object[] { processDefinitionKey,
				processInstance.getId(), variables });

		return processInstance;
	}

	@Override
	@Transactional(readOnly = false)
	public void complete(String taskId) {
		complete(taskId, null);
	}

	@Override
	@Transactional(readOnly = false)
	public void complete(String taskId, Map<String, Object> variables) {

		ShiroUser user = SubjectUtils.getPrincipal();

		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		variables.put(USERID, user.getLoginName());
		variables.put(USERNAME, user.getName());

		taskService.complete(taskId, variables);

	}

	@Override
	@Transactional(readOnly = false)
	public void resolveTask(String taskId) {
		resolveTask(taskId, null);
	}

	@Override
	@Transactional(readOnly = false)
	public void resolveTask(String taskId, Map<String, Object> variables) {

		ShiroUser user = SubjectUtils.getPrincipal();

		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		variables.put(USERID, user.getLoginName());
		variables.put(USERNAME, user.getName());

		taskService.resolveTask(taskId, variables);

	}

	@Override
	public List<HistoricActivityInstance> findHistoricActivityInstances(final String processInstanceId) {
		return findHistoricActivityInstances(processInstanceId, null, null);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<HistoricActivityInstance> findHistoricActivityInstances(final String processInstanceId,
			final List<String> actionVariableNames, final List<String> reasonVariableNames) {

		List results = null;

		// 流程历史活动轨迹
		List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc()
				.orderByHistoricActivityInstanceEndTime().asc().list();

		if (CollectionUtils.isNotEmpty(activityInstances)) {
			// 去除Gateway网关类型节点
			activityInstances = HistoricActivityPredicate.select(activityInstances);

			// 根据ID自增特点排序
			Collections.sort(activityInstances, HistoricActivityInstanceComparator.getInstanse());

			// 流程变量详细信息
			List<HistoricDetail> details = historyService.createHistoricDetailQuery()
					.processInstanceId(processInstanceId).variableUpdates().orderByTime().asc().list();

			if (CollectionUtils.isEmpty(details)) {// 流程不存在变量
				return activityInstances;
			} else {
				results = new ArrayList<HistoricActivityInstanceItem>(activityInstances.size());

				HistoricDetailPredicate predicate = null;
				List<HistoricDetail> subs = null;
				String value = null;
				HistoricActivityInstanceItem item = null;
				for (HistoricActivityInstance activityInstance : activityInstances) {

					predicate = new HistoricDetailPredicate(activityInstance.getId());
					subs = predicate.select(details, predicate);

					if (CollectionUtils.isNotEmpty(subs)) {
						item = BeanMapper.map(activityInstance, HistoricActivityInstanceItem.class);
						// 处理人
						value = HistoricVariableUpdateUtils.getString(subs, USERNAME);
						if (StringUtils.isNotEmpty(value)) {
							item.setAssignee(value);
						} else {
							setAssignee(item);
						}

						// 处理动作，如果同意，驳回等
						if (CollectionUtils.isNotEmpty(actionVariableNames)) {
							value = HistoricVariableUpdateUtils.getString(subs, actionVariableNames);
							item.setAction(value);
						}

						// 处理意见
						if (CollectionUtils.isNotEmpty(reasonVariableNames)) {
							value = HistoricVariableUpdateUtils.getString(subs, reasonVariableNames);
							item.setReason(value);
						}

						results.add(item);
					} else {
						if (activityInstance.getEndTime() == null
								|| activityInstance.getActivityType().equals("endEvent")) {
							item = BeanMapper.map(activityInstance, HistoricActivityInstanceItem.class);
							setAssignee(item);

							results.add(item);
						}
					}
				}
			}

		}

		return results;
	}

	@Override
	public List<String> getTaskCandidateGroups(final String taskId) {
		List<String> groups = null;
		List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);
		if (CollectionUtils.isNotEmpty(links)) {
			groups = new ArrayList<String>(links.size());
			for (IdentityLink link : links) {
				if (StringUtils.isNotEmpty(link.getGroupId())) {
					groups.add(link.getGroupId());
				}
			}
		}

		return groups;
	}

	@Override
	public List<String> getTaskCandidateUsers(final String taskId) {
		List<String> users = null;
		List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);
		if (CollectionUtils.isNotEmpty(links)) {
			users = new ArrayList<String>(links.size());
			for (IdentityLink link : links) {
				if (StringUtils.isNotEmpty(link.getUserId())) {
					users.add(link.getUserId());
				}
			}

		}

		return users;
	}

	@Override
	public long countTodoTasks(String userId) {
		// 根据当前人的ID查询
		long todoList = taskService.createTaskQuery().taskAssignee(userId).active().count();

		// 根据当前人未签收的任务
		long unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId).active().count();

		return todoList + unsignedTasks;
	}

	@Override
	public long countTodoTasks(String userId, String processDefinitionKey) {

		// 根据当前人的ID查询
		long todoList = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey)
				.active().count();

		// 根据当前人未签收的任务
		long unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId)
				.processDefinitionKey(processDefinitionKey).active().count();

		return todoList + unsignedTasks;
	}

	@Override
	public List<Task> findTodoTasks(String userId) {
		return findTodoTasks(userId, false);

	}

	@Override
	public List<Task> findTodoTasks(String userId, boolean businessDesc) {
		return findTodoTasks(userId, businessDesc, false, false);

	}

	@Override
	public List<Task> findTodoTasks(String userId, boolean includeProcessVariables, boolean includeTaskLocalVariables) {
		return findTodoTasks(userId, false, includeProcessVariables, includeTaskLocalVariables);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Task> findTodoTasks(String userId, boolean businessDesc, boolean includeProcessVariables,
			boolean includeTaskLocalVariables) {

		List taskItems = null;

		List<Task> todoList = null, unsignedTasks = null;

		if (!includeProcessVariables && !includeTaskLocalVariables) {
			// 根据当前人的ID查询
			todoList = taskService.createTaskQuery().taskAssignee(userId).active().orderByTaskId().desc()
					.orderByTaskCreateTime().desc().list();

			// 根据当前人未签收的任务
			unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId).active().orderByTaskId().desc()
					.orderByTaskCreateTime().desc().list();

		} else if (includeProcessVariables && !includeTaskLocalVariables) {
			// 根据当前人的ID查询
			todoList = taskService.createTaskQuery().taskAssignee(userId).active().includeProcessVariables()
					.orderByTaskId().desc().orderByTaskCreateTime().desc().list();

			unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId).active().includeProcessVariables()
					.orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		} else if (!includeProcessVariables && includeTaskLocalVariables) {
			// 根据当前人的ID查询
			todoList = taskService.createTaskQuery().taskAssignee(userId).active().includeTaskLocalVariables()
					.orderByTaskId().desc().orderByTaskCreateTime().desc().list();

			unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId).active()
					.includeTaskLocalVariables().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		} else {
			// 根据当前人的ID查询
			todoList = taskService.createTaskQuery().taskAssignee(userId).active().includeProcessVariables()
					.includeTaskLocalVariables().orderByTaskId().desc().orderByTaskCreateTime().desc().list();

			unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId).active().includeProcessVariables()
					.includeTaskLocalVariables().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		}

		if (CollectionUtils.isNotEmpty(todoList) || CollectionUtils.isNotEmpty(unsignedTasks)) {
			taskItems = new ArrayList<TaskItem>();
			tasks2TaskItems(taskItems, todoList, unsignedTasks, businessDesc);
			Collections.sort(taskItems);
		}

		return taskItems;

	}

	@Override
	public List<Task> findTodoTasks(String userId, String processDefinitionKey) {
		return findTodoTasks(userId, processDefinitionKey, false);
	}

	@Override
	public List<Task> findTodoTasks(String userId, String processDefinitionKey, boolean includeProcessVariables,
			boolean includeTaskLocalVariables) {
		return findTodoTasks(userId, processDefinitionKey, false, includeProcessVariables, includeTaskLocalVariables);
	}

	@Override
	public List<Task> findTodoTasks(String userId, String processDefinitionKey, boolean businessDesc) {
		return findTodoTasks(userId, processDefinitionKey, businessDesc, false, false);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Task> findTodoTasks(String userId, String processDefinitionKey, boolean businessDesc,
			boolean includeProcessVariables, boolean includeTaskLocalVariables) {
		List taskItems = null;

		List<Task> todoList = null, unsignedTasks = null;

		if (!includeProcessVariables && !includeTaskLocalVariables) {
			// 根据当前人的ID查询
			todoList = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey)
					.active().orderByTaskId().desc().orderByTaskCreateTime().desc().list();

			// 根据当前人未签收的任务
			unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId)
					.processDefinitionKey(processDefinitionKey).active().orderByTaskId().desc().orderByTaskCreateTime()
					.desc().list();

		} else if (includeProcessVariables && !includeTaskLocalVariables) {
			todoList = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey)
					.active().includeProcessVariables().orderByTaskId().desc().orderByTaskCreateTime().desc().list();

			// 根据当前人未签收的任务
			unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId)
					.processDefinitionKey(processDefinitionKey).active().includeProcessVariables().orderByTaskId()
					.desc().orderByTaskCreateTime().desc().list();
		} else if (!includeProcessVariables && includeTaskLocalVariables) {
			todoList = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey)
					.active().includeTaskLocalVariables().orderByTaskId().desc().orderByTaskCreateTime().desc().list();

			// 根据当前人未签收的任务
			unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId)
					.processDefinitionKey(processDefinitionKey).active().includeTaskLocalVariables().orderByTaskId()
					.desc().orderByTaskCreateTime().desc().list();
		} else {
			todoList = taskService.createTaskQuery().taskAssignee(userId).processDefinitionKey(processDefinitionKey)
					.active().includeProcessVariables().includeTaskLocalVariables().orderByTaskId().desc()
					.orderByTaskCreateTime().desc().list();

			// 根据当前人未签收的任务
			unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId)
					.processDefinitionKey(processDefinitionKey).active().includeProcessVariables()
					.includeTaskLocalVariables().orderByTaskId().desc().orderByTaskCreateTime().desc().list();
		}

		if (CollectionUtils.isNotEmpty(todoList) || CollectionUtils.isNotEmpty(unsignedTasks)) {
			taskItems = new ArrayList<TaskItem>();
			tasks2TaskItems(taskItems, todoList, unsignedTasks, businessDesc);
			Collections.sort(taskItems);
		}

		return taskItems;
	}

	@Override
	public List<String> findTodoBusinessKeys(String userId, String processDefinitionKey) {
		List<String> businessKeies = null;

		// 根据当前人的ID查询
		List<Task> todoList = taskService.createTaskQuery().taskAssignee(userId)
				.processDefinitionKey(processDefinitionKey).active().orderByTaskId().desc().orderByTaskCreateTime()
				.desc().list();

		// 根据当前人未签收的任务
		List<Task> unsignedTasks = taskService.createTaskQuery().taskCandidateUser(userId)
				.processDefinitionKey(processDefinitionKey).active().orderByTaskId().desc().orderByTaskCreateTime()
				.desc().list();

		if (CollectionUtils.isNotEmpty(todoList) || CollectionUtils.isNotEmpty(unsignedTasks)) {
			businessKeies = new ArrayList<String>();

			String businessKey = null;
			if (CollectionUtils.isNotEmpty(todoList)) {
				for (Task task : todoList) {
					businessKey = runtimeService.createProcessInstanceQuery()
							.processInstanceId(task.getProcessInstanceId()).active().singleResult().getBusinessKey();
					businessKeies.add(businessKey);
				}
			}

			if (CollectionUtils.isNotEmpty(unsignedTasks)) {
				for (Task task : unsignedTasks) {
					businessKey = runtimeService.createProcessInstanceQuery()
							.processInstanceId(task.getProcessInstanceId()).active().singleResult().getBusinessKey();
					businessKeies.add(businessKey);
				}
			}
		}

		return businessKeies;
	}

	@Transactional(readOnly = false)
	@Override
	public void signal(String processInstanceId, String activityId) {
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId)
				.activityId(activityId).singleResult();
		runtimeService.signal(execution.getId());
	}

	@Transactional(readOnly = false)
	@Override
	public void signal(String processInstanceId, String activityId, Map<String, Object> processVariables) {
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId)
				.activityId(activityId).singleResult();
		runtimeService.signal(execution.getId(), processVariables);
	}

	@Transactional(readOnly = false)
	@Override
	public void signalByKey(String processDefinitionKey) {
		List<Execution> executions = runtimeService.createExecutionQuery().processDefinitionKey(processDefinitionKey)
				.list();
		if (executions != null && executions.size() > 0) {
			for (Execution e : executions) {
				runtimeService.signal(e.getId());
			}
		}
	}

	@Transactional(readOnly = false)
	@Override
	public void signalByKey(String processDefinitionKey, Map<String, Object> processVariables) {
		List<Execution> executions = runtimeService.createExecutionQuery().processDefinitionKey(processDefinitionKey)
				.list();
		if (executions != null && executions.size() > 0) {
			for (Execution e : executions) {
				runtimeService.signal(e.getId(), processVariables);
			}
		}
	}

	@Override
	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	@Override
	public TaskService getTaskService() {
		return taskService;
	}

	@Override
	public HistoryService getHistoryService() {
		return historyService;
	}

	@Override
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	@Override
	public IdentityService getIdentityService() {
		return identityService;
	}

	@Override
	public FormService getFormService() {
		return formService;
	}

	@Override
	public SpringProcessEngineConfiguration getProcessEngineConfiguration() {
		return this.processEngineConfiguration;
	}

	private void tasks2TaskItems(List<TaskItem> taskItems, List<Task> todoList, List<Task> unsignedTasks,
			final boolean businessDesc) {

		if (CollectionUtils.isNotEmpty(todoList)) {
			tasks2TaskItems(taskItems, todoList, businessDesc);
		}

		if (CollectionUtils.isNotEmpty(unsignedTasks)) {
			tasks2TaskItems(taskItems, unsignedTasks, businessDesc);
		}

	}

	private void tasks2TaskItems(final List<TaskItem> taskItems, final List<Task> tasks, final boolean businessDesc) {

		if (CollectionUtils.isNotEmpty(tasks)) {

			UserEntity user = null;
			List<IdentityLink> links = null;
			List<String> candidateGroups = null, candidateUsers = null;
			Todo taskItem = null;
			ProcessDefinition processDefinition = null;
			ProcessInstance processInstance = null;
			List<Task> ts = null;
			String variable = null;

			for (Task task : tasks) {
				// taskItem = BeanMapper.map(task, TaskItem.class);
				taskItem = new Todo();
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

				// 流程定义
				processDefinition = this.repositoryService.createProcessDefinitionQuery()
						.processDefinitionId(task.getProcessDefinitionId()).singleResult();
				taskItem.setProcessDefinition(BeanMapper.map(processDefinition, ProcessDefinitionItem.class));

				// 流程实例
				processInstance = runtimeService.createProcessInstanceQuery()
						.processInstanceId(task.getProcessInstanceId()).active().singleResult();
				taskItem.setProcessInstance(BeanMapper.map(processInstance, ProcessInstanceItem.class));

				taskItem.setTaskLocalVariables(task.getTaskLocalVariables());
				taskItem.setProcessVariables(task.getProcessVariables());

				if (StringUtils.isEmpty(task.getAssignee())) {
					links = getTaskService().getIdentityLinksForTask(task.getId());
					if (CollectionUtils.isNotEmpty(links)) {
						candidateGroups = Collections3.extractNotEmptyToList(links, "groupId");
						taskItem.setCandidateGroups(CollectionUtils.isEmpty(candidateGroups) ? null : candidateGroups);

						candidateUsers = Collections3.extractNotEmptyToList(links, "userId");
						taskItem.setCandidateUsers(CollectionUtils.isEmpty(candidateUsers) ? null : candidateUsers);
					}
				} else {
					// 将处理人登录名改为姓名显示
					user = getProcessEngineConfiguration().getCustomIdentityService().findUserById(task.getAssignee());
					taskItem.setDisplayAssignee(user != null && StringUtils.isNotEmpty(user.getLastName()) ? user
							.getLastName() : task.getAssignee());
				}

				//
				if (businessDesc) {
					taskItem.setBusinessDesc(getBusinessDesc(task, processInstance.getBusinessKey()));
				}

				ts = new ArrayList<Task>();
				ts.add(taskItem);
				taskItem.setTasks(JSONMAPPER.toJson(ts));

				taskItems.add(taskItem);

			}
		}
	}

	private String getBusinessDesc(final Task task, final String businessKey) {

		String entityname = getVariable(task, ENTITYNAME);
		boolean enityPresent = org.springframework.util.ClassUtils.isPresent(entityname,
				WorkflowServiceImpl.class.getClassLoader());

		if (StringUtils.isNotEmpty(entityname) && enityPresent) {
			Class<?> domainClass = this.forName(entityname);
			if (domainClass != null) {
				CrudRepository<?, Serializable> repository = repositories.getRepositoryFor(domainClass);
				if (repository != null) {
					Object obj = repository.findOne(businessKey);
					return obj == null ? null : obj.toString();
				}
			}
		}

		return null;
	}

	private String getVariable(final Task task, final String variableName) {

		String entityname = null;
		Map<String, Object> map = task.getProcessVariables();
		if (MapUtils.isEmpty(map)) {
			map = task.getTaskLocalVariables();
		}

		if (MapUtils.isNotEmpty(map)) {
			entityname = MapUtils.getString(map, variableName);
		}

		if (StringUtils.isEmpty(entityname)) {
			entityname = (String) runtimeService.getVariable(task.getExecutionId(), variableName);
		}

		return entityname;

	}

	private Class<?> forName(final String className) {
		try {
			return ClassUtils.getClass(className);
		} catch (ClassNotFoundException e) {
			logger.warn("ClassNotFoundException", e);
			return null;
		}
	}

	/**
	 * 将流程历史的处理人登录名改为姓名显示
	 * 
	 * @param item
	 */
	private void setAssignee(HistoricActivityInstanceItem item) {

		if (StringUtils.isNotEmpty(item.getAssignee())) {
			UserEntity user = this.processEngineConfiguration.getCustomIdentityService().findUserById(
					item.getAssignee());
			if (user != null && StringUtils.isNotEmpty(user.getLastName())) {
				item.setAssignee(user.getLastName());
			}
		}
	}
}
