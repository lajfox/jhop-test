package com.techstar.example.repository.jpa;

import java.util.List;
import java.util.Map;

import org.hibernate.transform.ResultTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techstar.example.entity.Role;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface RoleDao extends MyJpaRepository<Role, String> {

	//JPQL查询
	@Query("select x from Role x where x.name=?1")
	Role findOneByName1(String name);

	@Query("select x from Role x where x.name=:name")
	Role findOneByName2(@Param("name") String name);

	@Query("select x.name as name,x.id as id from Role x where x.name=?1")
	Map<String, Object> findOneByName3(String name);
	
	@Query("select x.name as name,x.id as id from Role x where x.name=?1")
	Map<String, Object> findOneByName3(String name, ResultTransformer transformer);

	@Query("select new map(x.name as name,x.id as id) from Role x where x.name=?1")
	Map<String, Object> findOneByName4(String name);

	//sql查询
	@Query(value = "select x.* from SS_Role x where x.name=?1", nativeQuery = true)
	Role findOneByName5(String name);

	@Query(value = "select x.* from SS_Role x where x.name=:name", nativeQuery = true)
	Role findOneByName6(@Param("name") String name);

	@Query(value = "select x.name as name,x.id as id from SS_Role x where x.name=?1", nativeQuery = true)
	Map<String, Object> findOneByName7(String name);
	@Query(value = "select x.name as name,x.id as id from SS_Role x where x.name=?1", nativeQuery = true)
	
	Map<String, Object> findOneByName7(String name, ResultTransformer transformer);	
	

	//JPQL查询
	@Query("select x from Role x where x.name=?1")
	List<Role> findByName1(String name);
	
	@Query("select x from Role x where x.name=?1")
	List<Role> findByName1(String name,Sort sort);	
	
	@Query("select x from Role x where x.name=?1")
	Page<Role> findByName1(String name,Pageable pageable);
	
	@Query("select x from Role x where x.name=?1")
	Page<Role> findByName1(String name,Pageable pageable, ResultTransformer transformer);

	@Query("select x from Role x where x.name=:name")
	List<Role> findByName2(@Param("name") String name);
	
	@Query("select x.name as name,x.id as id from Role x where x.name=?1")
	List<Map<String, Object>> findByName3(String name);

	@Query("select x.name as name,x.id as id from Role x where x.name=?1")
	List<Map<String, Object>> findByName3(String name, ResultTransformer transformer);

	@Query("select new map(x.name as name,x.id as id) from Role x where x.name=?1")
	List<Map<String, Object>> findByName4(String name);	
	

	//SQL查询
	@Query(value = "select x.* from SS_Role x where x.name=?1", nativeQuery = true)
	List<Role> findByName5(String name);
	
	@Query(value = "select x.* from SS_Role x where x.name=?1", nativeQuery = true)
	List<Role> findByName5(String name,Sort sort);
	
	@Query(value = "select a.* from ss_user_role c,SS_Role a,ss_user b  where c.role_id=a.id and c.user_id = b.id and a.name=?1", nativeQuery = true)
	Page<Map<String, Object>> findByName5(String name,Pageable pageable, ResultTransformer transformer);

	@Query(value = "select x.* from SS_Role x where x.name=:name", nativeQuery = true)
	List<Role> findByName6(@Param("name") String name);

	@Query(value = "select x.name as name,x.id as id from SS_Role x where x.name=?1", nativeQuery = true)
	List<Map<String, Object>> findByName7(String name);
	
	@Query(value = "select x.name as name,x.id as id from SS_Role x where x.name=?1", nativeQuery = true)
	List<Map<String, Object>> findByName7(String name, ResultTransformer transformer);
	
	@Query(value = "select x.name as name,x.id as id from SS_Role x where x.name=?1", nativeQuery = true)
	Page<Map<String, Object>> findByName8(String name,Pageable pageable, ResultTransformer transformer);
	
	
	//JPQL更新
	@Modifying
	@Query("update Role u set u.name = ?1 where u.id = ?2")
	int updateRole(String name, String id);
	
	//SQL更新
	@Modifying
	@Query(value="insert into SS_Role(id,name) values(?1,?2)",nativeQuery=true)
	int insertRole(String id,String name);
}
