package com.techstar.sys.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techstar.modules.springframework.data.jpa.domain.JsonTree;

@Entity
@Table(name = "sys_dict_item")
public class SystemDicItem extends JsonTree<SystemDicItem> implements Serializable {

	private static final long serialVersionUID = 4094712007422880813L;

	private String ename;// 标识

	private String itemname;// 名称

	private Boolean valid;// 是否有效

	private SystemDicType systemDicType;// 对应字典类型

	@Column(length = 40)
	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	@Column(length = 100)
	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	// 在oracle映射数据库时，报此列不允许,因为oracle没有boolean
	// @Column(columnDefinition = "boolean default true")
	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "TYPEID")
	// 这里设置JoinColum设置了外键的名字，并且是关系维护端
	public SystemDicType getSystemDicType() {
		return systemDicType;
	}

	public void setSystemDicType(SystemDicType systemDicType) {
		this.systemDicType = systemDicType;
	}

}