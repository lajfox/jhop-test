package com.techstar.modules.activiti.service;

import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.activiti.repository.mybatis.ActivitiExecutionMybatisDao;
import com.techstar.modules.activiti.repository.mybatis.ActivitiMybatisDao;

/**
 * 流程定义扩展业务类
 * 
 * @author sundoctor
 * 
 */
@Service
@Transactional(readOnly = true)
public class ExecutionService extends ActivitiService<Execution> {

	@Autowired
	private ActivitiExecutionMybatisDao activitiExecutionMybatisDao;

	@Override
	protected ActivitiMybatisDao<Execution> getActivitiMybatisDao() {
		return activitiExecutionMybatisDao;
	}

}
