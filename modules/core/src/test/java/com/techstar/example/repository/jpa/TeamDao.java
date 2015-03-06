package com.techstar.example.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.techstar.example.entity.Team;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface TeamDao extends MyJpaRepository<Team, String> {

	@Query(" from Team  where id=?1")
	List<Team> findByPerson(String persons);
}
