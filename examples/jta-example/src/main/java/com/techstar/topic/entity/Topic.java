package com.techstar.topic.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;

import com.techstar.modules.springframework.data.jpa.domain.IdEntity;

@Entity
@Table(name = "XA_TOPIC")
public class Topic extends IdEntity {

	private String forumId;
	private String topicTitle;
	private String userId;
	private Date topicTime;
	private Integer topicViews;
	private Integer topicReplies;

	@NotBlank
	@Column(length = 100)
	public String getForumId() {
		return forumId;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}

	@NotBlank
	@Column(length = 200)
	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	@NotBlank
	@Column(length = 50)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTopicTime() {
		return topicTime;
	}

	public void setTopicTime(Date topicTime) {
		this.topicTime = topicTime;
	}

	public Integer getTopicViews() {
		return topicViews;
	}

	public void setTopicViews(Integer topicViews) {
		this.topicViews = topicViews;
	}

	public Integer getTopicReplies() {
		return topicReplies;
	}

	public void setTopicReplies(Integer topicReplies) {
		this.topicReplies = topicReplies;
	}

}
