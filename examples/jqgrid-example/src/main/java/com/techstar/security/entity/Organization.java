package com.techstar.security.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.techstar.modules.springframework.data.jpa.domain.JsonTree;

/**
 * 组织机构
 * 
 * @author lrm
 * @date 2013-05-02
 */
@Entity
@Table(name = "SYS_ORGANIZATION")
// 默认的缓存策略.
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organization extends JsonTree<Organization> {

    private String code;// 编码
    private String name;// 名称

    private Set<User> users = Sets.<User> newHashSet();
	private Boolean checked = false;
	private Boolean open = false;
    

    @Column(length = 40)
    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    @Column(length = 100)
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "SS_USER_ORGANIZATION",
			joinColumns = { @JoinColumn(name = "ORGANIZATION_ID") },
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
