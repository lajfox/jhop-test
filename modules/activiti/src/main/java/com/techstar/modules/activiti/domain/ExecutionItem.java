package com.techstar.modules.activiti.domain;

import java.util.Map;

import org.activiti.engine.impl.db.HasRevision;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

public class ExecutionItem implements Execution, PersistentObject, HasRevision, ProcessInstance {

	private String id;
	private int revision = 1;
	private int suspensionState = SuspensionState.ACTIVE.getStateCode();
	private String processDefinitionId;
	private String activityId;
	private String processInstanceId;
	private String businessKey;
	private String parentId;
	private boolean isActive = true;
	private boolean isScope = true;
	private boolean isConcurrent = false;
	private boolean isEnded = false;
	private boolean isEventScope = false;
	private String superExecutionId;
	private int cachedEntityState;

	private ProcessDefinitionItem processDefinition;

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean isSuspended() {
		return suspensionState == SuspensionState.SUSPENDED.getStateCode();
	}

	@Override
	public boolean isEnded() {
		return this.isEnded;
	}

	public void setEnded(boolean isEnded) {
		this.isEnded = isEnded;
	}

	@Override
	public String getActivityId() {
		return this.activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	@Override
	public String getProcessDefinitionId() {
		return this.processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	@Override
	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Override
	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Override
	public int getRevision() {
		return this.revision;
	}

	@Override
	public int getRevisionNext() {
		return this.revision + 1;
	}

	@Override
	public String getBusinessKey() {

		return this.businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	@Override
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getSuspensionState() {
		return suspensionState;
	}

	public void setSuspensionState(int suspensionState) {
		this.suspensionState = suspensionState;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isScope() {
		return isScope;
	}

	public void setScope(boolean isScope) {
		this.isScope = isScope;
	}

	public boolean isConcurrent() {
		return isConcurrent;
	}

	public void setConcurrent(boolean isConcurrent) {
		this.isConcurrent = isConcurrent;
	}

	public boolean isEventScope() {
		return isEventScope;
	}

	public void setEventScope(boolean isEventScope) {
		this.isEventScope = isEventScope;
	}

	public String getSuperExecutionId() {
		return superExecutionId;
	}

	public void setSuperExecutionId(String superExecutionId) {
		this.superExecutionId = superExecutionId;
	}

	public int getCachedEntityState() {
		return cachedEntityState;
	}

	public void setCachedEntityState(int cachedEntityState) {
		this.cachedEntityState = cachedEntityState;
	}

	public ProcessDefinitionItem getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinitionItem processDefinition) {
		this.processDefinition = processDefinition;
	}

	@Override
	public Object getPersistentState() {
		return null;
	}

	@Override
	public Map<String, Object> getProcessVariables() {
		return null;
	}

}
