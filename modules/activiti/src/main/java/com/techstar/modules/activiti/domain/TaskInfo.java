package com.techstar.modules.activiti.domain;

import org.activiti.engine.task.DelegationState;

public class TaskInfo {

	private String id;
	private String executionId;
	private String taskDefinitionKey;
	private String name;
	private DelegationState delegationState;// 任务委派状态
	private String owner;// 任务拥有者
	private String assignee;// 任务办理人
	private boolean claim = false;// 是否是签收任务

	public TaskInfo() {

	}

	public TaskInfo(String id, String executionId, String taskDefinitionKey, String name,
			DelegationState delegationState, String owner, String assignee) {
		this.id = id;
		this.executionId = executionId;
		this.taskDefinitionKey = taskDefinitionKey;
		this.name = name;
		this.delegationState = delegationState;
		this.owner = owner;
		this.assignee = assignee;

	}

	public TaskInfo(String taskId, String executionId, String activityId, String activityName,
			DelegationState delegationState, String owner, String assignee, boolean claim) {
		this(taskId, executionId, activityId, activityName, delegationState, owner, assignee);
		this.claim = claim;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DelegationState getDelegationState() {
		return delegationState;
	}

	public void setDelegationState(DelegationState delegationState) {
		this.delegationState = delegationState;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public boolean isClaim() {
		return claim;
	}

	public void setClaim(boolean claim) {
		this.claim = claim;
	}

}
