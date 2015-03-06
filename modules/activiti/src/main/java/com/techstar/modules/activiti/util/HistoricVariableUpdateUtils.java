package com.techstar.modules.activiti.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.activiti.collections.HistoricDetailComparator;
import com.techstar.modules.commons.lang3.time.DateUtils;

/**
 * HistoricVariableUpdate取值工具类
 * 
 * @author sundoctor
 * 
 */
public final class HistoricVariableUpdateUtils {

	/**
	 * 从details查找出名称为valueName的值
	 * 
	 * @param details
	 * @param variableName
	 * @return
	 */
	public static Object getObject(List<HistoricDetail> details, final String variableName) {

		if (CollectionUtils.isNotEmpty(details)) {
			Collections.sort(details, new HistoricDetailComparator());
			HistoricVariableUpdate variable = null;
			for (HistoricDetail detail : details) {
				variable = (HistoricVariableUpdate) detail;
				if (StringUtils.equals(variableName, variable.getVariableName())) {
					return variable.getValue();
				}
			}
		}

		return null;
	}

	/**
	 * 从details查找出名称为valueNames的值
	 * 
	 * @param details
	 * @param variableNames
	 * @return
	 */
	public static Object getObject(List<HistoricDetail> details, final List<String> variableNames) {
		Object obj = null;
		for (String variableName : variableNames) {
			obj = getObject(details, variableName);
			if (obj != null) {
				return obj;
			}
		}
		return obj;
	}

	/**
	 * 从details查找出名称为valueName的值
	 * 
	 * @param details
	 * @param variableName
	 * @return
	 */
	public static String getString(List<HistoricDetail> details, final String variableName) {
		Object obj = getObject(details, variableName);
		return obj == null ? null : obj.toString();
	}

	/**
	 * 从details查找出名称为valueNames的值
	 * 
	 * @param details
	 * @param variableNames
	 * @return
	 */
	public static String getString(List<HistoricDetail> details, final List<String> variableNames) {
		String value = null;
		for (String variableName : variableNames) {
			value = getString(details, variableName);
			if (value != null) {
				return value;
			}
		}
		return value;
	}

	/**
	 * 从details查找出名称为valueName的值
	 * 
	 * @param details
	 * @param variableName
	 * @return
	 */
	public static Date getDate(List<HistoricDetail> details, final String variableName) {
		Object obj = getObject(details, variableName);

		if (obj == null) {
			return null;
		}

		if (Date.class.isAssignableFrom(obj.getClass())) {
			return (Date) obj;
		} else if (String.class.isAssignableFrom(obj.getClass())) {
			return DateUtils.parseDate(obj.toString());
		} else if (Long.class.isAssignableFrom(obj.getClass()) || long.class.isAssignableFrom(obj.getClass())) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis((Long) obj);
			return cal.getTime();
		} else {
			throw new IllegalArgumentException("不支持的类型转换：" + obj);
		}

	}

	/**
	 * 从details查找出名称为valueNames的值
	 * 
	 * @param details
	 * @param variableNames
	 * @return
	 */
	public static Date getDate(List<HistoricDetail> details, final List<String> variableNames) {
		Date value = null;
		for (String variableName : variableNames) {
			value = getDate(details, variableName);
			if (value != null) {
				return value;
			}
		}
		return value;
	}

	/**
	 * 从details查找出名称为valueName的值
	 * 
	 * @param details
	 * @param variableName
	 * @return
	 */
	public static List<Object> getObjects(List<HistoricDetail> details, final String variableName) {

		List<Object> results = null;
		if (CollectionUtils.isNotEmpty(details)) {
			results = new ArrayList<Object>();
			HistoricVariableUpdate variable = null;
			for (HistoricDetail detail : details) {
				variable = (HistoricVariableUpdate) detail;
				if (StringUtils.equals(variableName, variable.getVariableName())) {
					results.add(variable.getValue());
				}
			}

		}

		return results;
	}
}
