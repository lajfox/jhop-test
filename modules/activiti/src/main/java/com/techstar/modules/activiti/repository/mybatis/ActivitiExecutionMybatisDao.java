package com.techstar.modules.activiti.repository.mybatis;

import org.activiti.engine.runtime.Execution;

import com.techstar.modules.mybatis.repository.MyBatisRepository;

/**
 * 流程定义接口
 * 
 * @author sundoctor
 * 
 */

@MyBatisRepository
public interface ActivitiExecutionMybatisDao extends ActivitiMybatisDao<Execution> {

}
