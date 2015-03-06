package com.techstar.hbjmis.repository.mybatis;


import com.techstar.hbjmis.entity.Team;
import com.techstar.modules.mybatis.repository.MyBatisRepository;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现.
 * 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author calvin
 */
@MyBatisRepository
public interface TeamMybatisDao {

	Team getWithDetail(String id);
}
