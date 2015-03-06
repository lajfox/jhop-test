package com.techstar.hbjmis.service.account;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hbjmis.entity.Account;
import com.techstar.hbjmis.repository.jpa.AccountDao;
import com.techstar.hbjmis.repository.mybatis.AccountMybatisDao;
import com.techstar.modules.hibernate.transform.MyAliasToEntityMapResultTransformer;
import com.techstar.modules.mybatis.domain.Page;
import com.techstar.modules.springframework.data.jpa.domain.QuerySpecification;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.JsonTreeJpaServiceImpl;

@Component
@Transactional(readOnly = true)
public class AccountService extends JsonTreeJpaServiceImpl<Account, String> {

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private AccountMybatisDao accountMybatisDao;

	@Override
	protected MyJpaRepository<Account, String> getMyJpaRepository() {
		return accountDao;
	}

	/**
	 * mybatis查询例子
	 * 
	 * @param map
	 * @param rowBounds
	 * @return
	 */
	public Page<Map<String, Object>> get(final Map<String, Object> map, final RowBounds rowBounds) {
		return accountMybatisDao.get(map, rowBounds);
	}

	public Map<String, Object> summary(final Map<String, Object> map, final RowBounds rowBounds) {
		List<Map<String, Object>> list = accountMybatisDao.summary(map, rowBounds);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	public Page<Map<String, Object>> get1(final Account account, final RowBounds rowBounds) {
		return accountMybatisDao.get1(account, rowBounds);
	}

	public Page<Map<String, Object>> get2(final double debit, final RowBounds rowBounds) {
		return accountMybatisDao.get2(debit, rowBounds);
	}

	public Page<Map<String, Object>> get3(final RowBounds rowBounds) {
		return accountMybatisDao.get3(rowBounds);
	}

	public Page<Map<String, Object>> get4(final String name, final String personanme, final RowBounds rowBounds) {
		return accountMybatisDao.get4(name, personanme, rowBounds);
	}

	/**
	 * JQPL查询例子
	 * 
	 * @param pageable
	 * @param spec
	 * @return
	 */
	public org.springframework.data.domain.Page<Map<String, Object>> findByJPQL(Pageable pageable,
			QuerySpecification spec) {
		return this.accountDao.findByJPQL(pageable, spec);
	}

	/**
	 * SQL查询例子
	 * 
	 * @param pageable
	 * @param spec
	 * @return
	 */
	public org.springframework.data.domain.Page<Map<String, Object>> findBySQL(Pageable pageable,
			QuerySpecification spec) {
		return this.accountDao.findBySQL(pageable, MyAliasToEntityMapResultTransformer.INSTANCE, spec);
	}

}
