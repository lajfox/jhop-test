package com.techstar.modules.activiti.domain;

import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

public class ProcessInstanceItem implements ProcessInstance {

	private String id;
	private boolean ended;
	private String activityId;
	private String processInstanceId;
	private String parentId;
	private String processDefinitionId;
	private String businessKey;
	private boolean suspended;
	private Map<String, Object> processVariables;

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean isEnded() {
		return this.ended;
	}

	@Override
	public String getActivityId() {
		return this.activityId;
	}

	@Override
	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	@Override
	public String getParentId() {
		return this.parentId;
	}

	@Override
	public String getProcessDefinitionId() {
		return this.processDefinitionId;
	}

	@Override
	public String getBusinessKey() {
		return this.businessKey;
	}

	@Override
	public boolean isSuspended() {
		return this.suspended;
	}

	@Override
	public Map<String, Object> getProcessVariables() {
		return this.processVariables;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public void setProcessVariables(Map<String, Object> processVariables) {
		this.processVariables = processVariables;
	}

}
