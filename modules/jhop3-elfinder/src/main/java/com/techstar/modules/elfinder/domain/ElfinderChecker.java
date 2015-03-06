package com.techstar.modules.elfinder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.techstar.modules.springframework.data.jpa.domain.IdEntity;

/**
 * 文档权限关系
 * 
 * 
 */
@Entity
@Table(name = "SS_ELFINDER_CHECKER")
public class ElfinderChecker extends IdEntity {

	private String relationship;
	private String elfinder;
	private Integer checker;

	@Column(name = "RELATIONSHIP_", length = 100)
	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	@Column(name = "ELFINDER_", length = 100)
	public String getElfinder() {
		return elfinder;
	}

	public void setElfinder(String elfinder) {
		this.elfinder = elfinder;
	}

	@Column(name = "CHECKER_")
	public Integer getChecker() {
		return checker;
	}

	public void setChecker(Integer checker) {
		this.checker = checker;
	}

}
