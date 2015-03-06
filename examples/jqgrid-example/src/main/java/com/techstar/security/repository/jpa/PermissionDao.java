package com.techstar.security.repository.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.security.entity.Permission;

public interface PermissionDao extends MyJpaRepository<Permission, String> {

	@Query(value = "select a.id,a.display_name as name,a.level_,a.parent_ as parent,a.order_,0 as cnt from ss_permission a order by a.order_", nativeQuery = true)
	List<Map<String, Object>> getAll();

	@Query(value = "select a.id,a.display_name as name,a.level_,a.parent_ as parent,a.order_,count(c.id) as cnt from ss_permission a left outer join ss_role_permission b on (a.id=b.permission_id) left outer join  ss_role c on (b.role_id=c.id and c.id=?1) group by a.id, a.display_name,a.level_,a.parent_ ,a.order_ order by a.order_", nativeQuery = true)
	List<Map<String, Object>> findByRole(final String id);

	@Query(value = "select a.id,a.display_name as name,a.level_,a.parent_ as parent,a.order_,count(c.id) as cnt from ss_permission a left outer join ss_user_permission b on (a.id=b.permission_id) left outer join ss_user c on (b.user_id=c.id and c.id=?1) group by a.id,a.display_name,a.level_,a.parent_ ,a.order_  order by a.order_", nativeQuery = true)
	List<Map<String, Object>> findByUser(final String id);



}
