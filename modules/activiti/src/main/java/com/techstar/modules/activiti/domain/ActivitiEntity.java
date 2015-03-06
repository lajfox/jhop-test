package com.techstar.modules.activiti.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.modules.jackson.serializer.JsonDateTimeSerializer;
import com.techstar.modules.springframework.data.jpa.domain.IdEntity;

/**
 * 工作流实体超类 工作流业务实体继承ActivitiEntity，冗余工作流相关数据，方便业务处理
 * 
 * @author sundoctor
 * 
 */
@MappedSuperclass
public class ActivitiEntity extends IdEntity {

	private String processDefinitionId;// 流程定义ID
	private String processDefinitionName;// 流程定义名称
	private String processDefinitionKey;// 流程定义ＫＥＹ
	private String processInstanceId;// 流程实例ID
	private Date processInstanceStartTime;// 流程实例开始时间
	private Date processInstanceEndTime;// 流程实例结束时间
	private String tasks;// 当前任务列表json

	@Column(name = "PROC_DEF_ID_", length = 64)
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	@Column(name = "PROC_DEF_NAME_", length = 255)
	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	@Column(name = "PROC_DEF_KEY_", length = 255)
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	@Column(name = "PROC_INST_ID_", length = 64)
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROC_INST_START_TIME_")
	public Date getProcessInstanceStartTime() {
		return this.processInstanceStartTime;
	}

	public void setProcessInstanceStartTime(Date processInstanceStartTime) {
		this.processInstanceStartTime = processInstanceStartTime;
	}

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROC_INST_END_TIME_")
	public Date getProcessInstanceEndTime() {
		return this.processInstanceEndTime;
	}

	public void setProcessInstanceEndTime(Date processInstanceEndTime) {
		this.processInstanceEndTime = processInstanceEndTime;
	}

	@Column(name = "TASKS_", length = 4000)
	public String getTasks() {
		return tasks;
	}

	public void setTasks(String tasks) {
		this.tasks = tasks;
	}

	@Transient
	public boolean isEnded() {
		return this.processInstanceEndTime != null;
	}

}
