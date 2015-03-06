package com.techstar.modules.commons.beanutils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.springframework.data.jpa.domain.support.DateConverter;

public final class ConvertUtils  extends org.apache.commons.beanutils.ConvertUtils{

	private static final DateConverter DATECONVERTER = new DateConverter();
	private static final JsonMapper BINDER = JsonMapper.nonDefaultMapper();
	
	/**
	 * 转换字符串类型到clazz的property类型的值.
	 * 
	 * @param value
	 *            待转换的字符串
	 * @param clazz
	 *            提供类型信息的Class
	 */
	@SuppressWarnings("unchecked")
	public static <E> E convert2(String value, Class<E> toType) {
		register(DATECONVERTER, Date.class);
		Object obj = org.apache.commons.beanutils.ConvertUtils.convert(value, toType);
		return obj == null ? null : (E) obj;
	}
	
	
	
	public static Object[] converts(String value, Class<?> clazz) {
		Assert.hasText(value);
		String[] values = null;
		if (StringUtils.isNotEmpty(value)) {
			if (value.startsWith("[") && value.endsWith("]")) {
				values = BINDER.fromJson(value, String[].class);
			} else {
				values = new String[1];
				values[0] = value;
			}
		}
		
		int i = 0;
		Object[] objs = new Object[values.length];
		for (String p : values) {
			objs[i] = convert2(p, clazz);
			i++;
		}
		return objs;
	}
	
	@SuppressWarnings("rawtypes")
	public static <E> Comparable convertComparable(String value, Class<E> toType) {
		Object obj = convert2(value, toType);
		return obj == null ? null : (Comparable) obj;
	}	
}
