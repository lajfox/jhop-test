package com.techstar.hbjmis.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.google.common.collect.Sets;
import com.techstar.modules.springframework.data.jpa.domain.IdEntity;

/**
 * 开发团队.
 * 
 * @author ZengWenfeng
 */
@Entity
@Table(name = "SS_TEAM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Team extends IdEntity {

	private String name;//队名
	private Person master;//领队
	private Set<Person> persons = Sets.newHashSet();//队员

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToOne
	@JoinColumn(name = "MASTER_ID")
	public Person getMaster() {
		return master;
	}

	public void setMaster(Person master) {
		this.master = master;
	}

	@OneToMany(mappedBy = "team")
	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
}
