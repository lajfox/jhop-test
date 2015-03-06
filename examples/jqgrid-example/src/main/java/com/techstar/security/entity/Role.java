package com.techstar.security.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.techstar.modules.springframework.data.jpa.domain.IdEntity;
import com.techstar.modules.utils.Collections3;

/**
 * 角色.
 * 
 * @author calvin
 */
@Entity
@Table(name = "SS_ROLE")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdEntity {

	private String name;
	private String note;

	private Set<Permission> permissions = Sets.<Permission> newHashSet();
	private Set<User> users = Sets.<User> newHashSet();

	@NotBlank
	@Length(max=500)
	@Column(unique=true,nullable=false,length=500)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(max=2000)
	@Column(length=2000)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "SS_ROLE_PERMISSION",
			joinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "id") })
	@OrderBy("name ASC")
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@JsonIgnore
	// 多对多定义
	@ManyToMany
	@JoinTable(name = "SS_USER_ROLE",
			joinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "id") })
	// 集合按name排序
	@OrderBy("name ASC")
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)	
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	//@JsonIgnore
	@Transient
	public String getPermissionList(){
		return Collections3.extractToString(permissions, "displayName", ", ");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
