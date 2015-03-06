package com.techstar.hbjmis.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.techstar.modules.mybatis.domain.Page;
import com.techstar.modules.mybatis.repository.MyBatisRepository;
import com.techstar.security.entity.User;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author ZengWenfeng
 */
@MyBatisRepository
public interface UserMybatisDao {

	User get(String id);

	Page<User> search(Map<String, Object> parameters, RowBounds rowBounds);

	List<Map<String, Object>> search2(Map<String, Object> parameters);

	void save(User user);

	void delete(String id);


	
}
