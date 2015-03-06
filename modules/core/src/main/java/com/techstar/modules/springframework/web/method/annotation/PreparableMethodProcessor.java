package com.techstar.modules.springframework.web.method.annotation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.web.context.request.NativeWebRequest;

import com.techstar.modules.springframework.core.annotation.AnnotationUtils;
import com.techstar.modules.springframework.web.bind.annotation.Preparable;

public class PreparableMethodProcessor extends AbstractMethodProcessor {

	public PreparableMethodProcessor() {
		super();
	}

	public PreparableMethodProcessor(boolean annotationNotRequired) {
		super(annotationNotRequired);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(Preparable.class)) {
			return true;
		} else if (this.annotationNotRequired) {
			return !BeanUtils.isSimpleProperty(parameter.getParameterType());
		} else {
			return false;
		}
	}

	/**
	 * 根据form的id从数据库查出实体对象
	 * 
	 * @param parameter
	 * @param request
	 * @param target
	 */
	@Override
	public Object preLoad(final MethodParameter parameter, final NativeWebRequest request, final Object target) {

		// search for Preparable annotation
		Preparable preparable = parameter.getParameterAnnotation(Preparable.class);
		if (preparable != null ) {
			// 查找当前实体主键属性名称
			String idAttributeName = getIdAttributeName(preparable, target);
			return findOne(idAttributeName, target, request);
		}

		return target;
	}

	private String getIdAttributeName(final Preparable preparable, final Object target) {

		String idAttributeName = preparable.value();

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

}
