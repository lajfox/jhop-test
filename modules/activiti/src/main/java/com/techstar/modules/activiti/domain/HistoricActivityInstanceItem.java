package com.techstar.modules.activiti.domain;

import java.util.Date;

import org.activiti.engine.history.HistoricActivityInstance;

public class HistoricActivityInstanceItem implements HistoricActivityInstance {

	private String id;
	private String activityId;
	private String activityName;
	private String activityType;
	private String assignee;
	private String taskId;
	private String calledProcessInstanceId;
	private String processDefinitionId;
	private String processInstanceId;
	private String executionId;
	private Date startTime;
	private Date endTime;
	private Long durationInMillis;

	private String action;// 处理结果
	private String reason;// 处理意见

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getActivityId() {
		return this.activityId;
	}

	@Override
	public String getActivityName() {
		return this.activityName;
	}

	@Override
	public String getActivityType() {
		return this.activityType;
	}

	@Override
	public String getProcessDefinitionId() {
		return this.processDefinitionId;
	}

	@Override
	public String getProcessInstanceId() {
		return this.processInstanceId;
	}

	@Override
	public String getExecutionId() {
		return this.executionId;
	}

	@Override
	public String getTaskId() {
		return this.taskId;
	}

	@Override
	public String getCalledProcessInstanceId() {
		return this.calledProcessInstanceId;
	}

	@Override
	public String getAssignee() {
		return this.assignee;
	}

	@Override
	public Date getStartTime() {
		return this.startTime;
	}

	@Override
	public Date getEndTime() {
		return this.endTime;
	}

	@Override
	public Long getDurationInMillis() {
		return this.durationInMillis;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setCalledProcessInstanceId(String calledProcessInstanceId) {
		this.calledProcessInstanceId = calledProcessInstanceId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setDurationInMillis(Long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}

}
