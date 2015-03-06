package com.techstar.modules.activiti.form;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.form.EnumFormType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 多值表单类型超类
 * 
 * @author sundoctor
 * @see CheckboxFormType
 * @see MultipleSelectFormType
 * 
 */
public class MultipleValuesFormType extends EnumFormType {

	public MultipleValuesFormType(Map<String, String> values) {
		super(values);
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		validateValue(propertyValue);
		String[] split = tokenizeToStringArray(propertyValue, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		return Arrays.asList(split);
	}

	@Override
	@SuppressWarnings("unchecked")
	public String convertModelValueToFormValue(Object modelValue) {
		
		List<String> values = null;
		if (modelValue != null) {
			if (!(modelValue instanceof List)) {
				throw new ActivitiIllegalArgumentException("Model value should be a java.util.List");
			}
			
			values = (List<String>) modelValue;
			validateValue(values);
		}
		
		return CollectionUtils.isEmpty(values) ? null : StringUtils.join(values,",");
	}

	/**
	 * 验证表单值的正确性
	 */

	protected void validateValue(List<String> value) {
		if (CollectionUtils.isNotEmpty(value) && MapUtils.isNotEmpty(values)) {
			for (String key : value) {
				if (!values.containsKey(key)) {
					throw new ActivitiIllegalArgumentException("Invalid value for enum form property: " + value);
				}
			}
		}
	}

	/**
	 * 验证表单值的正确性
	 */
	@Override
	protected void validateValue(String value) {
		if (StringUtils.isNotEmpty(value) && MapUtils.isNotEmpty(values)) {

			String[] valueArray = tokenizeToStringArray(value,
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

			if (ArrayUtils.isEmpty(valueArray)) {
				throw new ActivitiIllegalArgumentException("Invalid value for enum form property: " + value);
			}

			for (String key : valueArray) {

				if (!values.containsKey(key)) {
					throw new ActivitiIllegalArgumentException("Invalid value for enum form property: " + value);
				}
			}
		}
	}

}
