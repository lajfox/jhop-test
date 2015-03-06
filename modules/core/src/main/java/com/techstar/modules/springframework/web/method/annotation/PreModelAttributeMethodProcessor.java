package com.techstar.modules.springframework.web.method.annotation;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

import com.techstar.modules.commons.beanutils.PropertyUtils;
import com.techstar.modules.springframework.core.annotation.AnnotationUtils;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;

public class PreModelAttributeMethodProcessor extends AbstractMethodProcessor {

	public PreModelAttributeMethodProcessor() {
		super();
	}

	public PreModelAttributeMethodProcessor(boolean annotationNotRequired) {
		super(annotationNotRequired);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(PreModelAttribute.class)) {
			return true;
		} else if (super.annotationNotRequired) {
			return !BeanUtils.isSimpleProperty(parameter.getParameterType());
		} else {
			return false;
		}
	}

	@Override
	public final void resolveArgument(final MethodParameter parameter, final WebDataBinder binder,
			final NativeWebRequest request) throws Exception {

		setDisallowedFields(parameter, binder);

		Object target = binder.getTarget();
		if (target != null) {

			bindRequestParameters(binder, request);
			validateIfApplicable(binder, parameter);
			if (binder.getBindingResult().hasErrors()) {
				if (isBindExceptionRequired(binder, parameter)) {
					throw new BindException(binder.getBindingResult());
				}
			}
			
			String idAttributeName = getIdAttributeName(parameter,target);
			if(StringUtils.isNotEmpty(idAttributeName)){							
				String idAttributeValue = request.getNativeRequest(ServletRequest.class).getParameter(idAttributeName);
				if(ArrayUtils.contains(EMPTYIDS, idAttributeValue) || StringUtils.startsWith(idAttributeValue, "jqg")){
					try {
						PropertyUtils.setProperty(target, idAttributeName, null);
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e ) {					
						//e.printStackTrace();
					} 
				}
			}
		}

	}

	/**
	 * 实现 Preparable二次部分绑定的效果,先根据form的id从数据库查出实体对象,再把Form提交的内容绑定到该对象上
	 * 
	 * @param parameter
	 * @param request
	 * @param target
	 */
	@Override
	public Object preLoad(final MethodParameter parameter, final NativeWebRequest request, final Object target) {
		// search for ModelPreLoad annotation
		PreModelAttribute modelAttribute = parameter.getParameterAnnotation(PreModelAttribute.class);
		if (modelAttribute != null && modelAttribute.preparable()) {
			// 查找当前实体主键属性名称
			String idAttributeName = getIdAttributeName(modelAttribute, target);
			return findOne(idAttributeName, target, request);
		}

		return target;
	}
	
	private String getIdAttributeName( final MethodParameter parameter, final Object target) {
		PreModelAttribute modelAttribute = parameter.getParameterAnnotation(PreModelAttribute.class);		
		return getIdAttributeName(modelAttribute,target);
	}

	private String getIdAttributeName(final PreModelAttribute modelAttribute, final Object target) {

		String idAttributeName = modelAttribute.value();
		if (StringUtils.isEmpty(idAttributeName)) {
			JpaEntityInformation<?, ?> jpaEntityInformation = JpaMetamodelEntityInformation.getMetadata(
					target.getClass(), em);
			idAttributeName = jpaEntityInformation.getIdAttribute().getName();
		}
		if (StringUtils.isEmpty(idAttributeName)) {
			idAttributeName = AnnotationUtils.getIdAttributeName(target.getClass());
		}
		return idAttributeName;
	}

	/**
	 * 不自动绑定对象中的属性，另行处理。
	 * 
	 * @param parameter
	 * @param binder
	 */
	private void setDisallowedFields(final MethodParameter parameter, final WebDataBinder binder) {
		PreModelAttribute modelAttribute = parameter.getParameterAnnotation(PreModelAttribute.class);
		if (modelAttribute != null) {
			String disallowedFields[] = modelAttribute.disallowedFields();
			if (ArrayUtils.isNotEmpty(disallowedFields)) {
				binder.setDisallowedFields(disallowedFields);
			}
		}
	}

}
