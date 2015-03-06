package com.techstar.modules.activiti.domain;

import java.util.List;

import com.techstar.modules.activiti.util.TaskItemUtils;

/**
 * 
 * @author sundoctor
 * 
 */
public class ClaimInfo {

	private String assignees;
	private String tasks;

	public ClaimInfo(String tasks) {
		List<TaskItem> list = TaskItemUtils.fromJson(tasks);
		this.assignees = TaskItemUtils.getAssignees(list);
		this.tasks = tasks;
	}

	public ClaimInfo(String assignees, String tasks) {
		this.assignees = assignees;
		this.tasks = tasks;
	}

	public String getAssignees() {
		return assignees;
	}

	public void setAssignees(String assignees) {
		this.assignees = assignees;
	}

	public String getTasks() {
		return tasks;
	}

	public void setTasks(String tasks) {
		this.tasks = tasks;
	}

}
