package com.techstar.modules.activiti.repository.mybatis;

import org.apache.ibatis.session.RowBounds;

import com.techstar.modules.mybatis.domain.Page;

/**
 * 流程共同接口
 * 
 * @author sundoctor
 * 
 */

public interface ActivitiMybatisDao<T> {

	/**
	 * 流程分页查询
	 * 
	 * @param rowBounds
	 * @return 流程分页数据
	 */
	Page<T> findPageBy(final RowBounds rowBounds);
}
