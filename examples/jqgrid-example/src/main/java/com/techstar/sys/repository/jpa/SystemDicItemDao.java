package com.techstar.sys.repository.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.sys.entity.SystemDicItem;
/***
 * 系统字典
 * @date 2012-11-02
 * @author lrm
 */
public interface SystemDicItemDao extends	MyJpaRepository<SystemDicItem, String> {
	
	@Query("select new Map(a.ename as id,a.itemname as text) from SystemDicItem a where a.systemDicType.sign =?1 and a.valid=true order by orderno asc")
	List<Map<String, Object>> findByStdSign(final String stdSign);
	
	@Query("select a from SystemDicItem a where a.systemDicType.sign =?1 and a.valid=true and a.parent is null order by orderno asc ")
	List<SystemDicItem> findObjByStdSign(final String stdSign);
	
	@Query("select a from SystemDicItem a where a.parent.id =?1 and a.valid=true order by orderno asc ")
	List<SystemDicItem> findSubDictList(final String parentid);
}
