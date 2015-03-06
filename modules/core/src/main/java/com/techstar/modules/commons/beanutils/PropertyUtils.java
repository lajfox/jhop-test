package com.techstar.modules.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;

import com.techstar.modules.commons.lang3.time.DateUtils;

public final class PropertyUtils extends org.apache.commons.beanutils.PropertyUtils {

	public static Object getProperty(final Object bean, final String name) {
		return getProperty(bean, name, null);
	}

	public static <E> Object getProperty(final Object bean, final String name, final E defaul) {
		try {
			Object obj = org.apache.commons.beanutils.PropertyUtils.getProperty(bean, name);
			return obj == null ? defaul : obj;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getString(final Object bean, final String name) {
		return getString(bean, name, null);
	}

	public static String getString(final Object bean, final String name, final String defaul) {
		Object obj = getProperty(bean, name, defaul);
		return obj == null ? defaul : (String) obj;
	}

	public static Integer getInteger(final Object bean, final String name) {
		return getInteger(bean, name, null);
	}

	public static Integer getInteger(final Object bean, final String name, final Integer defaul) {
		Object obj = getProperty(bean, name, defaul);
		return obj == null ? defaul : NumberUtils.toInt((String) obj);
	}

	public static Long getLong(final Object bean, final String name) {
		return getLong(bean, name, null);
	}

	public static Long getLong(final Object bean, final String name, final Long defaul) {
		Object obj = getProperty(bean, name, defaul);
		return obj == null ? defaul : NumberUtils.toLong((String) obj);
	}

	public static Double getDouble(final Object bean, final String name) {
		return getDouble(bean, name, null);
	}

	public static Double getDouble(final Object bean, final String name, final Double defaul) {
		Object obj = getProperty(bean, name, defaul);
		return obj == null ? defaul : NumberUtils.toDouble((String) obj);
	}
	
	
	public static Number getNumber(final Object bean, final String name) {
		return getNumber(bean, name, null);
	}

	public static Number getNumber(final Object bean, final String name, final Double defaul) {		
		Object obj = getProperty(bean, name, defaul);
		return obj == null ? defaul : (Number) obj;
	}

	public static Date getDate(final Object bean, final String name) {
		return getDate(bean, name, null);
	}

	public static Date getDate(final Object bean, final String name, final Date defaul) {
		Object obj = getProperty(bean, name, defaul);

		if (obj == null) {
			return null;
		} else {
			if (Date.class.isAssignableFrom(obj.getClass())) {
				return (Date) obj;
			} else if (String.class.isAssignableFrom(obj.getClass())) {
				return DateUtils.parseDate(obj.toString());
			} else if (long.class.isAssignableFrom(obj.getClass()) || Long.class.isAssignableFrom(obj.getClass())) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis((Long) obj);
				return cal.getTime();
			} else {
				throw new IllegalArgumentException("不支持的日期类型转换：" + obj.getClass().getName());
			}
		}
	}

}
