package com.techstar.modules.springframework.web.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.techstar.modules.commons.beanutils.PropertyUtils;

public class WebUtils extends org.springframework.web.util.WebUtils {

	/**
	 * 将Map转换为key1:value1;key2:value2:key3:value3...形式
	 * 
	 * @param map
	 * @return
	 */
	public static String toValue(final Map<String, String> map) {
		if (MapUtils.isEmpty(map)) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			Entry<String, String> entry = null;
			while (iterator.hasNext()) {
				entry = iterator.next();
				sb.append(entry.getKey()).append(":").append(entry.getValue());
				sb.append(";");
			}
			sb.deleteCharAt(sb.length() - 1);

			return sb.toString();
		}

	}

	/**
	 * 将beans转换为value1:lable1;value2:lable2,value3:lable3...
	 * 
	 * @param beans
	 * @param id
	 * @param name
	 * @return
	 */
	public static String toValue(final List<?> beans, final String value, final String lable) {
		if (CollectionUtils.isEmpty(beans)) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();

			for (Object bean : beans) {
				sb.append(PropertyUtils.getProperty(bean, value)).append(":")
						.append(PropertyUtils.getProperty(bean, lable));
				sb.append(";");
			}

			return sb.toString();
		}

	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Assert.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					if (StringUtils.isNotEmpty(values[0])) {
						params.put(unprefixed, values[0]);
					}
				}
			}
		}
		return params;
	}
}
