package com.techstar.modules.activiti.util;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.techstar.modules.commons.lang3.time.DateUtils;

/**
 * 变量取值工具类
 * 
 * @author sundoctor
 * 
 */
public final class DelegateTaskUtils {

	public static Object getObject(final DelegateTask delegateTask, final String variableName) {
		return delegateTask.getVariable(variableName);
	}

	public static String getString(final DelegateTask delegateTask, final String variableName) {
		return ObjectUtils.toString(getObject(delegateTask, variableName), null);
	}

	public static Integer getInteger(final DelegateTask delegateTask, final String variableName) {
		return NumberUtils.createInteger(getString(delegateTask, variableName));
	}

	public static Long getLong(final DelegateTask delegateTask, final String variableName) {
		return NumberUtils.createLong(getString(delegateTask, variableName));
	}

	public static Float getFloat(final DelegateTask delegateTask, final String variableName) {
		return NumberUtils.createFloat(getString(delegateTask, variableName));
	}

	public static Double getDouble(final DelegateTask delegateTask, final String variableName) {
		return NumberUtils.createDouble(getString(delegateTask, variableName));
	}

	public static Date getDate(final DelegateTask delegateTask, final String variableName) {
		Object obj = getObject(delegateTask, variableName);

		if (obj == null) {
			return null;
		} else {
			if (Date.class.isAssignableFrom(obj.getClass())) {
				return (Date) obj;
			}else if (String.class.isAssignableFrom(obj.getClass())){
				return DateUtils.parseDate(obj.toString());
			}else if (long.class.isAssignableFrom(obj.getClass()) || Long.class.isAssignableFrom(obj.getClass())){
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis((Long)obj);
				return cal.getTime();
			}else{
				throw new IllegalArgumentException("不支持的类型转换："+obj);
			}
		}
		
	}
}
