package com.techstar.modules.elfinder.repository.jpa;


import org.springframework.data.jpa.repository.Query;

import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface ElfinderDao extends MyJpaRepository<Elfinder, String> {

	@Query("select sum(size) from Elfinder where id in (?1)")
	long size(final String[] ids);
}
