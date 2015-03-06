package com.techstar.modules.springframework.data.jpa.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略. 子类可重载getId()函数重定义id的列名映射和生成策略.
 * 
 * @author calvin
 */
// JPA Entity基类的标识
@MappedSuperclass
public class IdEntity {

	protected String id;

	@Id
	// @GeneratedValue //主键自动生成策略：数据库自动增长
	// @GeneratedValue(strategy = GenerationType.IDENTITY)//主键自动生成策略：数据库IDENTITY
	// @GeneratedValue(strategy = GenerationType.SEQUENCE)//主键自动生成策略：数据库SEQUENCE
	@GeneratedValue(generator = "system-uuid")// 主键自动生成策略：UUID
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length=32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
