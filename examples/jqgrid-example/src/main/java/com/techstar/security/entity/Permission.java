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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.techstar.modules.springframework.data.jpa.domain.JsonTree;

@Entity
@Table(name = "SS_PERMISSION")
public class Permission extends JsonTree<Permission>{

	private String name;
	private String displayName;
	private String url;
	private Boolean menu = false;
	
	private Set<Role> roles = Sets.<Role> newHashSet();
	private Set<User> users = Sets.<User> newHashSet();
	
	private Boolean checked = false;
	private Boolean open = false;

	@NotBlank
	@Length(max=300)
	@Column(unique = true, nullable = false, length = 300)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	@Length(max=500)
	@Column(unique = true, nullable = false, length = 500)
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Length(max=500)
	@Column(length = 500)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getMenu() {
		return menu;
	}

	public void setMenu(Boolean menu) {
		this.menu = menu;
	}

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "SS_ROLE_PERMISSION", joinColumns = { @JoinColumn(name = "PERMISSION_ID",
			referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID",
			referencedColumnName = "id") })
	@OrderBy("name ASC")
	// Fecth策略定义
	@Fetch(FetchMode.SUBSELECT)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "SS_USER_PERMISSION",
			joinColumns = { @JoinColumn(name = "PERMISSION_ID") },
			inverseJoinColumns = { @JoinColumn(name = "USER_ID") })
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
	
	@Transient
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	@Transient
	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}
}
