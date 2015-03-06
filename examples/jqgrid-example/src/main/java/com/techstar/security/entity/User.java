package com.techstar.security.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Sets;
import com.techstar.modules.jackson.serializer.JsonDateTimeSerializer;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.utils.Collections3;

/**
 * 用户.
 * 
 * @author calvin
 */
@Entity
@Table(name = "SS_USER")
// 默认的缓存策略.
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// @Audited(withModifiedFlag=false)
public class User extends ShiroUser {

	private String email;
	private String status;
	private Date createtime = new Date();

	private Set<Role> roles = Sets.<Role> newHashSet();
	private Set<Permission> permissions = Sets.<Permission> newHashSet();
	private Set<Organization> organizations = Sets.<Organization> newHashSet();

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

	@JsonSerialize(using = JsonDateTimeSerializer.class)	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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
	
	
	// @NotAudited  cff
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "SS_USER_ORGANIZATION", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ORGANIZATION_ID") })
	// 集合按name排序
	@OrderBy("name ASC")
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	public Set<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<Organization> organizations) {
		this.organizations = organizations;
	}
	
 

	@Transient
	public String getRoleList() {
		return Collections3.extractToString(roles, "name", ", ");
	}

	@Transient
	public String getPermissionList() {
		return Collections3.extractToString(permissions, "displayName", ", ");
	}
	
	@Transient
	public String getOrganizationList() {
		return Collections3.extractToString(organizations, "name", ", ");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}