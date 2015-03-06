package com.techstar.modules.springframework.core.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Id;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.techstar.modules.springframework.util.ReflectionUtils;

public final class AnnotationUtils extends org.springframework.core.annotation.AnnotationUtils {

	public static String getIdAttributeName(Class<?> clazz) {
		Assert.notNull(clazz);
		String name = getIdAttributeNameByMethod(clazz);
		return StringUtils.isEmpty(name) ? getIdAttributeNameByField(clazz) : name;
	}

	private static String getIdAttributeNameByField(Class<?> clazz) {
		Field[] fields = ReflectionUtils.getAllDeclaredFields(clazz);
		if (ArrayUtils.isNotEmpty(fields)) {
			for (Field field : fields) {
				if (field.getAnnotation(Id.class) != null) {
					return field.getName();
				}
			}
		}
		return null;
	}

	private static String getIdAttributeNameByMethod(Class<?> clazz) {
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
		if (ArrayUtils.isNotEmpty(methods)) {
			for (Method method : methods) {
				if (method.getAnnotation(Id.class) != null) {
					return StringUtils.uncapitalize(method.getName().replace("get", ""));
				}
			}
		}
		return null;
	}
}
