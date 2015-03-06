package com.techstar.example.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Sets;
import com.techstar.modules.jackson.serializer.JsonDateSerializer;
import com.techstar.modules.springframework.data.jpa.domain.IdEntity;
import com.techstar.modules.utils.Collections3;

/**
 * 用户.
 * 
 * @author calvin
 */
@Entity
@Table(name = "SS_USER")
public class User extends IdEntity {

	private String name;// 姓名
	private String email;// 电邮
	private String status;// 状态
	private Date createtime = new Date();
	private Double logintime;

	private String loginName;
	private String password;
	private String salt;
	private String plainPassword;

	private Set<Role> roles = Sets.<Role> newHashSet();
	private Set<Permission> permissions = Sets.<Permission> newHashSet();// 授权

	@NotBlank
	@Length(max = 100)
	@Column(name = "LOGIN_NAME", unique = true, nullable = false, length = 100)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Transient
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	@Length(max = 50)
	@Column(name = "PASSWORD", nullable = false, length = 50)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(max = 30)
	@Column(name = "SALT", nullable = false, length = 30)
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@NotBlank
	@Length(max = 300)
	@Column(nullable = false, length = 300)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email
	@Length(max = 300)
	@Column(length = 300)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(length = 10)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	// @JsonSerialize(using=CustomDateTimeSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Double getLogintime() {
		return logintime;
	}

	public void setLogintime(Double logintime) {
		this.logintime = logintime;
	}

	// @NotAudited
	@JsonIgnore
	// 多对多定义
	@ManyToMany
	@JoinTable(name = "SS_USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "id") })
	// 集合按name排序
	@OrderBy("name ASC")
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	// @NotAudited
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "SS_USER_PERMISSION", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "PERMISSION_ID") })
	// 集合按name排序
	@OrderBy("name ASC")
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Transient
	public String getRoleList() {
		return Collections3.extractToString(roles, "name", ", ");
	}

	@Transient
	public String getPermissionList() {
		return Collections3.extractToString(permissions, "displayName", ", ");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}