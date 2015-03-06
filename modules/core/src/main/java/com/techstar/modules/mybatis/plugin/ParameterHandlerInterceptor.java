package com.techstar.modules.mybatis.plugin;

import java.sql.PreparedStatement;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.hibernate.dialect.pagination.LimitHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参数查询拦截，利用hiernate的LimitHandler设置分页参数
 * 
 * @author sundoctor
 * 
 */
@Intercepts({ @Signature(type = ParameterHandler.class, method = "setParameters", args = { PreparedStatement.class }) })
public class ParameterHandlerInterceptor implements Interceptor {

	protected static Logger logger = LoggerFactory.getLogger(ParameterHandlerInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		LimitHandler limitHandler = InterceptorContext.getLimitHandler();
		if (limitHandler != null) {
			PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];

			int index = InterceptorContext.getIndex();
			logger.info("index:{}", index);

			int i = limitHandler.bindLimitParametersAtStartOfQuery(ps, index);
			logger.info("i:{}", i);
			int j = limitHandler.bindLimitParametersAtEndOfQuery(ps, index + i);
			logger.info("j:{}", j);
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

}
