package com.techstar.modules.activiti.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.modules.jackson.serializer.JsonDateDeserializer;
import com.techstar.modules.jackson.serializer.JsonDateTimeSerializer;
import com.techstar.modules.jaxb.serializer.JaxbDateTimeSerializer;

public class TaskItem implements Task, Comparable<TaskItem> {

	private String id;
	private String name;
	private String description;
	private int priority;
	private String owner;// 任务拥有者
	private String assignee;// 任务办理人
	private DelegationState delegationState;// 任务委派状态
	private String executionId;
	private String processInstanceId;
	private String processDefinitionId;
	private Date createTime;
	private String taskDefinitionKey;
	private Date dueDate;
	private String parentTaskId;
	private boolean suspended;
	private Map<String, Object> taskLocalVariables;
	private Map<String, Object> processVariables;

	private ProcessDefinitionItem processDefinition;
	private ProcessInstanceItem processInstance;

	private List<String> candidateGroups;// 当前任务处理角色
	private List<String> candidateUsers;// 当前任务处理用户
	private String displayAssignee;// assignee显示名称
	private boolean claim = false;// 是否是签收任务

	private String businessDesc;// 业务扩展描述
	

	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int getPriority() {
		return this.priority;
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String getOwner() {
		return this.owner;
	}

	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String getAssignee() {
		return this.assignee;
	}

	@Override
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	@Override
	public DelegationState getDelegationState() {
		return this.delegationState;
	}

	@Override
	public void setDelegationState(DelegationState delegationState) {
		this.delegationState = delegationState;
	}

	@Override
	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Override
	public String getExecutionId() {
		return this.executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	@Override
	public String getProcessDefinitionId() {
		return this.processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	@Override
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@XmlJavaTypeAdapter(JaxbDateTimeSerializer.class)
	public Date getCreateTime() {
		return this.createTime;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String getTaskDefinitionKey() {
		return this.taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	@Override
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@XmlJavaTypeAdapter(JaxbDateTimeSerializer.class)
	public Date getDueDate() {
		return this.dueDate;
	}

	@Override
	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public void delegate(String userId) {

	}

	@Override
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	@Override
	public String getParentTaskId() {
		return this.parentTaskId;
	}

	@Override
	public boolean isSuspended() {
		return this.suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	@Override
	public Map<String, Object> getTaskLocalVariables() {
		return this.taskLocalVariables;
	}

	public void setTaskLocalVariables(Map<String, Object> taskLocalVariables) {
		this.taskLocalVariables = taskLocalVariables;
	}

	@Override
	public Map<String, Object> getProcessVariables() {
		return this.processVariables;
	}

	public void setProcessVariables(Map<String, Object> processVariables) {
		this.processVariables = processVariables;
	}

	public ProcessDefinitionItem getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinitionItem processDefinition) {
		this.processDefinition = processDefinition;
	}

	public ProcessInstanceItem getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstanceItem processInstance) {
		this.processInstance = processInstance;
	}

	public List<String> getCandidateGroups() {
		return candidateGroups;
	}

	public void setCandidateGroups(List<String> candidateGroups) {
		this.candidateGroups = candidateGroups;
	}

	public List<String> getCandidateUsers() {
		return candidateUsers;
	}

	public void setCandidateUsers(List<String> candidateUsers) {
		this.candidateUsers = candidateUsers;
	}

	public String getDisplayAssignee() {
		return displayAssignee;
	}

	public void setDisplayAssignee(String displayAssignee) {
		this.displayAssignee = displayAssignee;
	}

	public boolean isClaim() {
		return claim;
	}

	public void setClaim(boolean claim) {
		this.claim = claim;
	}

	public String getBusinessDesc() {
		return businessDesc;
	}

	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
	}

	@Override
	public int compareTo(TaskItem item) {
		return NumberUtils.toInt(item.getId()) - NumberUtils.toInt(id);
	}

}
