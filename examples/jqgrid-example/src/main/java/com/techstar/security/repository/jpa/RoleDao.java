package com.techstar.security.repository.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.security.entity.Role;

public interface RoleDao extends MyJpaRepository<Role, String> {

	@Query(value = "select a.id, a.name,0 as cnt from ss_role a order by a.name", nativeQuery = true)
	List<Map<String, Object>> getAll();

	@Query(value = "select a.id,a.name,count(c.id) as cnt from ss_role a left outer join ss_user_role b on (a.id=b.role_id) left outer join ss_user c on (b.user_id=c.id and c.id=?1) group by a.id,a.name order by a.name",
			nativeQuery = true)
	List<Map<String, Object>> findByUser(final String id);
}
