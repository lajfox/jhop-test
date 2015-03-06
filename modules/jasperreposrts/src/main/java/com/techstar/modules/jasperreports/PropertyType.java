package com.techstar.modules.jasperreports;

import java.util.Date;

/**
 * 属性数据类型.
 * 
 * @author zwf
 * 
 */
public enum PropertyType {
	S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class);

	private Class<?> clazz;

	PropertyType(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getValue() {
		return clazz;
	}
}
