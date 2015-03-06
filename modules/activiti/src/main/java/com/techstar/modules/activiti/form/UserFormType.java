package com.techstar.modules.activiti.form;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户表单字段类型
 * 
 * @author sundoctor
 * 
 */
public class UserFormType extends AbstractUserFormType {

	public static final String TYPE_NAME = "user";

	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {

		if (StringUtils.isNotEmpty(propertyValue)) {
			// Check if user exists
			checkUsers(propertyValue);
			return propertyValue;
		}
		return null;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		return ObjectUtils.toString(modelValue);
	}
}
