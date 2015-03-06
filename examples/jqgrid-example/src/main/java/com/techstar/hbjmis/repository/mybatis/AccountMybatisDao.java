package com.techstar.hbjmis.repository.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.techstar.hbjmis.entity.Account;
import com.techstar.modules.mybatis.domain.Page;
import com.techstar.modules.mybatis.repository.MyBatisRepository;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现. 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author ZengWenfeng
 */
@MyBatisRepository
public interface AccountMybatisDao {

	Page<Map<String, Object>> get(final Map<String, Object> map, final RowBounds rowBounds);

	List<Map<String, Object>> summary(final Map<String, Object> map, final RowBounds rowBounds);

	Page<Map<String, Object>> get1(final Account account, final RowBounds rowBounds);

	Page<Map<String, Object>> get2(final double debit, final RowBounds rowBounds);

	Page<Map<String, Object>> get3(final RowBounds rowBounds);

	Page<Map<String, Object>> get4(@Param("name") final String name, @Param("personname") final String personname,
			final RowBounds rowBounds);
}
