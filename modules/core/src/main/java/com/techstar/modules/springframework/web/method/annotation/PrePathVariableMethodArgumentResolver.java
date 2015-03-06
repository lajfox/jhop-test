package com.techstar.modules.springframework.web.method.annotation;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import com.techstar.modules.springframework.data.jpa.domain.support.DateConverter;
import com.techstar.modules.springframework.web.bind.annotation.PrePathVariable;

public class PrePathVariableMethodArgumentResolver extends PathVariableMethodArgumentResolver implements
		ApplicationContextAware {

	private static final DateConverter DATECONVERTER = new DateConverter();
	private Repositories repositories;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext context) {
		this.repositories = new Repositories(context);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(PrePathVariable.class);
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		PrePathVariable annotation = parameter.getParameterAnnotation(PrePathVariable.class);
		return new PathVariableNamedValueInfo(annotation);
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		Object pathValue = super.resolveName(name, parameter, request);

		PrePathVariable annotation = parameter.getParameterAnnotation(PrePathVariable.class);
		if (pathValue != null && annotation != null && annotation.preparable()) {
			Object target = createAttribute(parameter);
			return findOne(target, pathValue.toString());
		}
		return pathValue;
	}

	protected Object createAttribute(MethodParameter parameter) throws Exception {
		return BeanUtils.instantiateClass(parameter.getParameterType());
	}

	protected Object findOne(final Object target, String pathValue) {
		// 如果主键存在，根据主键查询
		if (StringUtils.isNotEmpty(pathValue)) {
			RepositoryInformation info = repositories.getRepositoryInformationFor(target.getClass());
			CrudRepository<?, Serializable> repository = repositories.getRepositoryFor(target.getClass());
			Serializable serializable = convertValue(pathValue, info.getIdType());
			return repository.findOne(serializable);
		}

		return target;
	}

	private static class PathVariableNamedValueInfo extends NamedValueInfo {

		private PathVariableNamedValueInfo(PrePathVariable annotation) {
			super(annotation.value(), true, ValueConstants.DEFAULT_NONE);
		}
	}

	/**
	 * 转换字符串类型到clazz的property类型的值.
	 * 
	 * @param value
	 *            待转换的字符串
	 * @param clazz
	 *            提供类型信息的Class
	 */
	@SuppressWarnings("unchecked")
	protected <E extends Serializable> E convertValue(String value, Class<E> toType) {
		ConvertUtils.register(DATECONVERTER, Date.class);
		Object obj = ConvertUtils.convert(value, toType);
		return obj == null ? null : (E) obj;
	}
}
