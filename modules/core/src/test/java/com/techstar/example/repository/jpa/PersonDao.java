package com.techstar.example.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.techstar.example.entity.Person;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface PersonDao extends MyJpaRepository<Person, String> {

	@Query("select x from Person x where x.team.id=?1")
	List<Person> findByTeam(String id);
}
