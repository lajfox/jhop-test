package com.techstar.hbjmis.entity.oa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.techstar.modules.activiti.domain.ActivitiEntity;
import com.techstar.modules.jackson.serializer.JsonDateTimeSerializer;

/**
 * Entity: Leave
 * 
 * @author ZengWenfeng
 */
@Entity
@Table(name = "OA_LEAVE")
public class Leave extends ActivitiEntity {

	private String userId;

	private Date startTime;

	private Date endTime;

	private Date realityStartTime;

	private Date realityEndTime;
	private Date applyTime;
	private String leaveType;
	private String reason;

	

	@Column
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	@Column
	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	@Column
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REALITY_START_TIME")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	public Date getRealityStartTime() {
		return realityStartTime;
	}

	public void setRealityStartTime(Date realityStartTime) {
		this.realityStartTime = realityStartTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REALITY_END_TIME")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	public Date getRealityEndTime() {
		return realityEndTime;
	}

	public void setRealityEndTime(Date realityEndTime) {
		this.realityEndTime = realityEndTime;
	}



	@Override
	public String toString() {
		return new StringBuilder().append(this.leaveType).append(",开始时间：")
				.append(DateFormatUtils.format(this.startTime, "yyyy-MM-dd HH:mm:ss")).append(",结束时间：")
				.append(DateFormatUtils.format(this.endTime, "yyyy-MM-dd HH:mm:ss")).toString();
	}

}
