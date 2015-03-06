package com.techstar.modules.activiti.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.activiti.repository.mybatis.ActivitiMybatisDao;
import com.techstar.modules.mybatis.domain.Page;

/**
 * 流程公共业务类
 * 
 * @author sundoctor
 * 
 */

@Transactional(readOnly = true)
public abstract class ActivitiService<T> {

	protected abstract ActivitiMybatisDao<T> getActivitiMybatisDao();

	/**
	 * 流程分页查询
	 * 
	 * 
	 * @param rowBounds
	 * @return 流程分页数据
	 * @see ActivitiMybatisDao#findPageBy(RowBounds)
	 */
	public Page<T> findPageBy(final RowBounds rowBounds) {
		return getActivitiMybatisDao().findPageBy(rowBounds);
	}

}
