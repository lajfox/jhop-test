package com.techstar.modules.mybatis.plugin;

import static com.techstar.modules.commons.beanutils.ConvertUtils.convert2;
import static com.techstar.modules.mybatis.util.SQLUtils.getAfterGroupByInsertPoint;
import static com.techstar.modules.mybatis.util.SQLUtils.getCountString;
import static com.techstar.modules.mybatis.util.SQLUtils.getLineSql;
import static com.techstar.modules.mybatis.util.SQLUtils.getOrders;
import static com.techstar.modules.mybatis.util.SQLUtils.removeOrders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.internal.util.ReflectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.techstar.modules.dozer.mapper.BeanMapper;
import com.techstar.modules.mybatis.domain.MyRowBounds;
import com.techstar.modules.mybatis.domain.Page;
import com.techstar.modules.mybatis.query.SqlQuery;
import com.techstar.modules.springframework.data.jpa.domain.Filters;
import com.techstar.modules.springframework.data.jpa.domain.Rule;
import com.techstar.modules.springframework.data.jpa.domain.SingleSearch;

/**
 * 分页查询拦截
 * 
 * @author sundoctor
 * 
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }) })
public class PaginationInterceptor extends SqlQuery implements Interceptor {

	protected static Logger log = LoggerFactory.getLogger(PaginationInterceptor.class);

	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	private static final Map<String, Dialect> DIALECTMAP = new HashMap<String, Dialect>();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		Object rowBounds = invocation.getArgs()[2];

		if (rowBounds != null && rowBounds instanceof MyRowBounds) {

			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Object parameter = invocation.getArgs()[1];
			parameter = this.initParameter(mappedStatement, parameter);

			MetaObject metaParameter = MetaObject.forObject(parameter, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY);
			BoundSql boundSql = mappedStatement.getBoundSql(parameter);

			String originalSql = boundSql.getSql().trim();
			MetaObject metaBoundSql = MetaObject.forObject(boundSql, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY);

			Configuration configuration = mappedStatement.getConfiguration();
			MyRowBounds myRowBounds = (MyRowBounds) rowBounds;

			originalSql = getLineSql(originalSql);
			String orders = getOrders(originalSql);// 获取sql中的order by语句
			if (StringUtils.isNotEmpty(orders)) {
				originalSql = removeOrders(originalSql);// 删除sql中的order by语句
			}
			String groupbys = null;
			int idx = getAfterGroupByInsertPoint(originalSql);
			if (idx > 0) {
				groupbys = originalSql.substring(idx);
				originalSql = originalSql.substring(0, idx);
			}

			StringBuilder sbSql = new StringBuilder(originalSql.length() + 300);
			sbSql.append(originalSql);

			// 单条件搜索
			singleSerach(myRowBounds, configuration, boundSql, metaBoundSql, metaParameter, sbSql);

			// 高级查询
			advancedSearch(myRowBounds, configuration, boundSql, metaBoundSql, metaParameter, sbSql);

			// 自定义查询
			defSearch(myRowBounds, configuration, boundSql, metaBoundSql, metaParameter, sbSql);

			// 工具栏搜索
			toolbarSearch(myRowBounds, configuration, boundSql, metaBoundSql, metaParameter, sbSql);

			// 增加order by
			originalSql = addGroupbyAndOrders(sbSql, groupbys, orders, myRowBounds);

			if (myRowBounds.isPagination()) {

				@SuppressWarnings("rawtypes")
				Page<?> page = new Page(myRowBounds.getPage().getPage() - 1, myRowBounds.getPage().getRows());

				long count = 0;
				if (myRowBounds.isAutoCount()) {
					count = getCount(invocation, mappedStatement, boundSql, parameter, getCountString(originalSql));
				} else {
					count = myRowBounds.getTotal();
				}

				if (count <= 0) {
					return page;
				} else {
					page.setTotalElements(count);
					InterceptorContext.setPage(page);

					Dialect dialect = this.instantiateDialect(configuration);

					RowSelection selection = new RowSelection();
					selection.setFirstRow((myRowBounds.getPage().getPage() - 1) * myRowBounds.getPage().getRows());
					selection.setMaxRows(myRowBounds.getPage().getRows());
					LimitHandler limitHandler = dialect.buildLimitHandler(originalSql, selection);
					InterceptorContext.setLimitHandler(limitHandler);

					List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
					int index = CollectionUtils.isEmpty(parameterMappings) ? 1 : parameterMappings.size() + 1;
					InterceptorContext.setIndex(index);

					String pageSql = limitHandler.getProcessedSql();

					// buildParameter(configuration, boundSql, metaBoundSql,
					// metaParameter, myRowBounds);

					BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, parameterMappings,
							parameter);
					MappedStatement newMs = copyMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

					invocation.getArgs()[0] = newMs;
					invocation.getArgs()[1] = parameter;
					invocation.getArgs()[2] = new MyRowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
				}
			} else {
				if (myRowBounds.hasSearch()) {
					BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), originalSql,
							boundSql.getParameterMappings(), parameter);
					MappedStatement newMs = copyMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

					invocation.getArgs()[0] = newMs;
					invocation.getArgs()[1] = parameter;
					invocation.getArgs()[2] = new MyRowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
				}
			}

		}

		return invocation.proceed();

	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

	/**
	 * //工具栏搜索
	 * 
	 * @param myRowBounds
	 * @param configuration
	 * @param boundSql
	 * @param metaBoundSql
	 * @param metaParameter
	 * @param sbSql
	 */
	private void toolbarSearch(final MyRowBounds myRowBounds, final Configuration configuration,
			final BoundSql boundSql, final MetaObject metaBoundSql, final MetaObject metaParameter,
			final StringBuilder sbSql) {

		Map<String, String> map = myRowBounds.getToolbarSearchMap();
		if (MapUtils.isNotEmpty(map) && myRowBounds.getFilters() == null) {
			where(sbSql);

			Map<String, String> dataTypeMap = myRowBounds.getDataTypeMap();
			String op = "eq";// 相等
			int i = 0;
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String value = entry.getValue();
				if (StringUtils.isNotEmpty(value)) {
					if (i != 0) {
						sbSql.append(" and ");
					}

					Class<?> javaType = this.getJavaType(dataTypeMap, entry.getKey());
					if (String.class.isAssignableFrom(javaType)) {
						op = "cn";// 包含于
					} else {
						op = "eq";// 相等
					}

					buildSql(configuration, boundSql, metaBoundSql, metaParameter, sbSql, dataTypeMap, op,
							entry.getKey(), value);

					i++;
				}
			}
		}
	}

	/**
	 * 高级查询
	 * 
	 * @param myRowBounds
	 * @param configuration
	 * @param boundSql
	 * @param metaBoundSql
	 * @param metaParameter
	 * @param sbSql
	 */
	private void advancedSearch(final MyRowBounds myRowBounds, final Configuration configuration,
			final BoundSql boundSql, final MetaObject metaBoundSql, final MetaObject metaParameter,
			final StringBuilder sbSql) {

		Filters filters = myRowBounds.getFilters();
		if (filters != null && CollectionUtils.isNotEmpty(filters.getRules())) {

			Map<String, String> dataTypeMap = myRowBounds.getDataTypeMap();
			Assert.notEmpty(dataTypeMap, "没有设置datatype参数");

			where(sbSql);
			sbSql.append(" ( ");
			advancedSearch(dataTypeMap, filters, configuration, boundSql, metaBoundSql, metaParameter, sbSql);
			sbSql.append(" ) ");
		}
	}

	/**
	 * 自定义查询
	 * 
	 * @param myRowBounds
	 * @param configuration
	 * @param boundSql
	 * @param metaBoundSql
	 * @param metaParameter
	 * @param sbSql
	 */
	private void defSearch(final MyRowBounds myRowBounds, final Configuration configuration, final BoundSql boundSql,
			final MetaObject metaBoundSql, final MetaObject metaParameter, final StringBuilder sbSql) {

		Filters filters = myRowBounds.getDefFilters();
		if (filters != null && CollectionUtils.isNotEmpty(filters.getRules())) {

			Map<String, String> dataTypeMap = myRowBounds.getDataTypeMap();
			Assert.notEmpty(dataTypeMap, "没有设置datatype参数");

			where(sbSql);
			sbSql.append(" ( ");
			advancedSearch(dataTypeMap, filters, configuration, boundSql, metaBoundSql, metaParameter, sbSql);
			sbSql.append(" ) ");
		}
	}

	/**
	 * 高级查询
	 * 
	 * @param dataTypeMap
	 * @param filters
	 * @param configuration
	 * @param boundSql
	 * @param metaBoundSql
	 * @param metaParameter
	 * @param sbSql
	 */
	private void advancedSearch(final Map<String, String> dataTypeMap, final Filters filters,
			final Configuration configuration, final BoundSql boundSql, final MetaObject metaBoundSql,
			final MetaObject metaParameter, final StringBuilder sbSql) {

		if (filters != null) {

			List<Rule> rules = filters.getRules();
			if (CollectionUtils.isNotEmpty(rules)) {
				int i = 0;

				for (Rule rule : rules) {

					if (i != 0) {
						sbSql.append(" ").append(filters.getGroupOp()).append(" ");
					}

					buildSql(configuration, boundSql, metaBoundSql, metaParameter, sbSql, dataTypeMap, rule.getOp(),
							rule.getField(), rule.getData());

					i++;
				}

			}

			List<Filters> groups = filters.getGroups();
			if (CollectionUtils.isNotEmpty(groups)) {
				for (Filters fs : groups) {
					sbSql.append(" ").append(filters.getGroupOp()).append(" (");
					advancedSearch(dataTypeMap, fs, configuration, boundSql, metaBoundSql, metaParameter, sbSql);
					sbSql.append(") ");
				}
			}
		}
	}

	/**
	 * 单条件搜索
	 * 
	 * @param myRowBounds
	 * @param configuration
	 * @param boundSql
	 * @param metaBoundSql
	 * @param metaParameter
	 * @param sbSql
	 */
	private void singleSerach(final MyRowBounds myRowBounds, final Configuration configuration,
			final BoundSql boundSql, final MetaObject metaBoundSql, final MetaObject metaParameter,
			final StringBuilder sbSql) {
		// 单条件搜索
		if (myRowBounds.hasSingle()) {

			SingleSearch single = myRowBounds.getSingleSearch();
			Map<String, String> dataTypeMap = myRowBounds.getDataTypeMap();
			Assert.notEmpty(dataTypeMap, "没有设置datatype参数");

			where(sbSql);

			buildSql(configuration, boundSql, metaBoundSql, metaParameter, sbSql, dataTypeMap, single.getSearchOper(),
					single.getSearchField(), single.getSearchString());
		}

	}

	/**
	 * 根据条件构建sql
	 * 
	 * @param configuration
	 * @param boundSql
	 * @param metaBoundSql
	 * @param metaParameter
	 * @param sbSql
	 * @param dataTypeMap
	 * @param op
	 * @param field
	 * @param value
	 */
	private final void buildSql(final Configuration configuration, final BoundSql boundSql,
			final MetaObject metaBoundSql, final MetaObject metaParameter, final StringBuilder sbSql,
			final Map<String, String> dataTypeMap, final String op, final String field, final String value) {

		condition(sbSql, field, op);
		sbSql.append(" ");

		if (isNotNNAndNu(op)) {

			Class<?> javaType = this.getJavaType(dataTypeMap, field);
			Assert.notNull(javaType, "设置的datatype参数里没有找到" + field + "属性");

			// 设置参数
			buildParameter(configuration, boundSql, metaBoundSql, metaParameter, field,
					convert2(getValue(op, value, javaType), javaType), javaType);

		}
	}

	/**
	 * 初始化参数
	 * 
	 * @param mappedStatement
	 * @param parameter
	 * @return
	 */
	private Object initParameter(final MappedStatement mappedStatement, Object parameter) {

		// 参数为空、其本类型、基本类型包装类、String、Date类型的参数，将其转换为Map<String,Object>
		if (parameter == null || ClassUtils.isPrimitiveOrWrapper(parameter.getClass())
				|| String.class.isAssignableFrom(parameter.getClass())
				|| Date.class.isAssignableFrom(parameter.getClass())) {
			Object value = parameter;
			parameter = new HashMap<String, Object>();

			MetaObject metaParameter = MetaObject.forObject(parameter, DEFAULT_OBJECT_FACTORY,
					DEFAULT_OBJECT_WRAPPER_FACTORY);

			BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
			if (CollectionUtils.isNotEmpty(parameterMappings) && value != null) {
				for (int i = 0; i < parameterMappings.size(); i++) {
					ParameterMapping parameterMapping = parameterMappings.get(i);
					String propertyName = parameterMapping.getProperty();
					metaParameter.setValue(propertyName, value);
				}
			}
		} else {
			// 将pojo转为Map
			if (!Map.class.isAssignableFrom(parameter.getClass())) {
				parameter = BeanMapper.map(parameter, Map.class);
			}
		}

		return parameter;
	}

	/**
	 * 设置参数
	 * 
	 * @param configuration
	 * @param boundSql
	 * @param metaBoundSql
	 * @param metaParameter
	 * @param property
	 *            参数名称
	 * @param value
	 *            　参数值
	 * @param javaType
	 *            　参数类型
	 */
	private void buildParameter(final Configuration configuration, final BoundSql boundSql,
			final MetaObject metaBoundSql, final MetaObject metaParameter, final String property, final Object value,
			final Class<?> javaType) {

		//modify by ZengWenfeng 2014-5-21
		//增加uuid区别同一字段同时查询，如时间的createtime >=? and createtime<= >
		String tmpProperty = property+UUID.randomUUID().toString();
		metaParameter.setValue(tmpProperty, value);

		ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, tmpProperty, javaType);
		ParameterMapping parameterMapping = builder.build();

		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (CollectionUtils.isEmpty(parameterMappings)) {
			parameterMappings = new ArrayList<ParameterMapping>();
		}
		parameterMappings.add(parameterMapping);

		metaBoundSql.setValue("parameterMappings", parameterMappings);
	}

	/**
	 * 追加sql排序
	 * 
	 * @param originalSql
	 * @param orders
	 * @param myRowBounds
	 * @return
	 */
	private String addGroupbyAndOrders(final StringBuilder sbSql, final String groupbys, final String orders,
			final MyRowBounds myRowBounds) {

		if (StringUtils.isNotEmpty(groupbys)) {
			sbSql.append(" ").append(groupbys).append(" ");
		}

		if (StringUtils.isNotEmpty(orders)) {
			sbSql.append(orders);
		}

		com.techstar.modules.springframework.data.jpa.domain.Page page = myRowBounds.getPage();
		if (page != null && StringUtils.isNotEmpty(myRowBounds.getOrders())) {
			if (StringUtils.isEmpty(orders)) {
				sbSql.append(" order by ");
			} else {
				sbSql.append(" , ");
			}
			sbSql.append(myRowBounds.getOrders());
		}

		return sbSql.toString();

	}

	private Dialect instantiateDialect(final Configuration configuration) {
		String dialectName = getDialectName(configuration);
		Dialect dialect = DIALECTMAP.get(dialectName);
		if (dialect == null) {
			try {
				dialect = (Dialect) ReflectHelper.classForName(dialectName).newInstance();
				DIALECTMAP.put(dialectName, dialect);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("Dialect class not found: " + dialectName);
			} catch (Exception e) {
				throw new RuntimeException("Could not instantiate given dialect class: " + dialectName, e);
			}
		}
		return dialect;
	}

	private String getDialectName(Configuration configuration) {
		String dialect = configuration.getVariables().getProperty("dialect");
		Assert.hasText(dialect, "The dialect was not set. Set the property dialect.");
		return dialect;
	}

	/**
	 * <p>
	 * 查询总数
	 * </p>
	 * 
	 * @param ivk
	 * @param ms
	 * @param boundSql
	 * @param param
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private long getCount(final Invocation ivk, final MappedStatement ms, final BoundSql boundSql, final Object param,
			final String countSql) throws SQLException {

		DataSource dataSource = ms.getConfiguration().getEnvironment().getDataSource();
		Connection conn = DataSourceUtils.getConnection(dataSource);

		BoundSql bs = new BoundSql(ms.getConfiguration(), countSql, boundSql.getParameterMappings(), param);
		ParameterHandler parameterHandler = new DefaultParameterHandler(ms, param, bs);

		ResultSet rs = null;
		PreparedStatement stmt = null;
		long count = 0;
		try {
			stmt = conn.prepareStatement(countSql);
			parameterHandler.setParameters(stmt);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getLong(1);
			}
		} finally {
			rs.close();
			stmt.close();
			// conn.close();
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
		log.info("count={}", count);
		return count;
	}

	private MappedStatement copyMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		// builder.keyProperty(ms.getKeyProperties()[0]);
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.cache(ms.getCache());
		MappedStatement newMs = builder.build();
		return newMs;
	}

	private static class BoundSqlSqlSource implements SqlSource {
		private BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}
}
