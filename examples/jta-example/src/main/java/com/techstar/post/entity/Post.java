package com.techstar.post.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;

import com.techstar.modules.springframework.data.jpa.domain.IdEntity;

@Entity
@Table(name = "XA_POST")
public class Post extends IdEntity {

	private String topic;
	private String forumId;
	private String userId;
	private byte[] postAttach;
	private Date postTime;

	@NotBlank
	@Column(name = "TOPIC", length = 32)
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@NotBlank
	@Column(length = 100)
	public String getForumId() {
		return forumId;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}

	@NotBlank
	@Column(length = 50)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Lob
	public byte[] getPostAttach() {
		return postAttach;
	}

	public void setPostAttach(byte[] postAttach) {
		this.postAttach = postAttach;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

}
