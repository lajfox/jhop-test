package com.techstar.modules.activiti.repository.mybatis;

import org.activiti.engine.repository.Model;

import com.techstar.modules.mybatis.repository.MyBatisRepository;

/**
 * 流程模型接口
 * 
 * @author sundoctor
 * 
 */

@MyBatisRepository
public interface ActivitiModelMybatisDao extends ActivitiMybatisDao<Model> {

}
