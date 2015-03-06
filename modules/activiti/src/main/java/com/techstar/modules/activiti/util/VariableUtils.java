package com.techstar.modules.activiti.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.activiti.domain.PropertyType;
import com.techstar.modules.activiti.domain.Variable;

public final class VariableUtils {

	public static Map<String, Object> asMap(Collection<Variable> vars) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (CollectionUtils.isNotEmpty(vars)) {
			for (Variable var : vars) {
				if (StringUtils.isEmpty(var.getType())
						|| PropertyType.S == Enum.valueOf(PropertyType.class, var.getType())) {
					map.put(var.getKey(), var.getValue());
				} else {
					Class<?> targetType = Enum.valueOf(PropertyType.class, var.getType()).getValue();
					Object objectValue = ConvertUtils.convert(var.getValue(), targetType);
					map.put(var.getKey(), objectValue);
				}
			}
		}

		return map;
	}
}
