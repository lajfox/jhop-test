package com.techstar.hbjmis.repository.jpa;

import java.util.Map;

import org.hibernate.transform.ResultTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.techstar.hbjmis.entity.Account;
import com.techstar.modules.springframework.data.jpa.domain.QuerySpecification;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface AccountDao extends MyJpaRepository<Account, String> {

	@Query("select new map(a.id as id,a.num as num, a.name as name, a.debit as debit,a.credit as credit,a.balance as balance, a.type as type,a.createtime as createtime,b.id as personid, b.name as personname,b.address as address,b.phone as phone) from Account a join a.owner b")
	Page<Map<String, Object>> findByJPQL(Pageable pageable, QuerySpecification spec);

	@Query(value = "select a.*,b.id as personid, b.name as personname,b.address as address,b.phone as phone from SS_Account a ,SS_Person b where a.person_id = b.id",
			nativeQuery = true)
	Page<Map<String, Object>> findBySQL(Pageable pageable, ResultTransformer transformer,QuerySpecification spec);
}
