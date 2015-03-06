package com.techstar.modules.activiti.service;

import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.activiti.repository.mybatis.ActivitiMybatisDao;
import com.techstar.modules.activiti.repository.mybatis.ActivitiProcdefMybatisDao;

/**
 * 流程定义扩展业务类
 * 
 * @author sundoctor
 * 
 */
@Service
@Transactional(readOnly = true)
public class ProcdefService extends ActivitiService<ProcessDefinition> {

	@Autowired
	private ActivitiProcdefMybatisDao activitiProcdefMybatisDao;

	@Override
	protected ActivitiMybatisDao<ProcessDefinition> getActivitiMybatisDao() {
		return activitiProcdefMybatisDao;
	}

}
