package com.techstar.modules.activiti.domain;

import java.util.List;

import com.techstar.modules.activiti.util.TaskItemUtils;

public class StartInfo extends ActivitiEntity {

	private String activityNames;
	private String assignees;

	public StartInfo(ActivitiEntity entity) {

		this.setId(entity.getId());
		this.setProcessDefinitionId(entity.getProcessDefinitionId());
		this.setProcessDefinitionKey(entity.getProcessDefinitionKey());
		this.setProcessDefinitionName(entity.getProcessDefinitionName());
		this.setProcessInstanceId(entity.getProcessInstanceId());
		this.setProcessInstanceStartTime(entity.getProcessInstanceStartTime());
		this.setProcessInstanceEndTime(entity.getProcessInstanceEndTime());
		this.setTasks(entity.getTasks());

		List<TaskItem> list = TaskItemUtils.fromJson(entity.getTasks());
		this.setActivityNames(TaskItemUtils.getActivityNames(list));
		this.setAssignees(TaskItemUtils.getAssignees(list));
	}

	public String getActivityNames() {
		return activityNames;
	}

	public void setActivityNames(String activityNames) {
		this.activityNames = activityNames;
	}

	public String getAssignees() {
		return assignees;
	}

	public void setAssignees(String assignees) {
		this.assignees = assignees;
	}

}
