package com.techstar.security.repository.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.security.entity.Organization;

public interface OrganizationDao extends MyJpaRepository<Organization, String> {

    @Query(value = "select a.id,a.name as name,a.level_,a.parent_ as parent,a.order_,0 as cnt from SYS_ORGANIZATION a order by a.order_", nativeQuery = true)
    List<Map<String, Object>> getAll();
    
    @Query(value = "select a.id,a.name as name,a.level_,a.parent_ as parent,a.order_,count(c.id) as cnt from SYS_ORGANIZATION a left outer join ss_user c on (c.unitid=a.id and c.id=?1) group by a.id,a.name,a.level_,a.parent_ ,a.order_  order by a.order_", nativeQuery = true)
    List<Map<String, Object>> findByUser(final String id);
    
    @Query(value = "select a.id,a.name as name,a.level_,a.parent_ as parent,a.order_,count(c.id) as cnt from SYS_ORGANIZATION a left outer join SS_USER_ORGANIZATION b on (a.id=b.ORGANIZATION_ID) left outer join ss_user c on (b.user_id=c.id and c.id=?1) group by a.id,a.name,a.level_,a.parent_ ,a.order_  order by a.order_", nativeQuery = true)
    List<Map<String, Object>> findByUsers(final String id);
    
    /***
     * 查询除当前节点及子结点以外的数据
     * 
     * @Date 2013-5-30 下午2:25:21
     * @author lrm
     */
    @Query(value = "select a.id,a.name as name,a.level_,a.parent_ as parent,a.order_,count(c.id) as cnt from SYS_ORGANIZATION a left outer join SYS_ORGANIZATION c on (c.parent_=a.id and c.id=?1) where a.id not in (select t.id  from SYS_ORGANIZATION t start with t.id = ?2 connect by prior t.id = t.parent_) group by a.id,a.name,a.level_,a.parent_ ,a.order_  order by a.order_", nativeQuery = true)
    List<Map<String, Object>> findByIdAndSonNot(final String id1,final String id2);

}
