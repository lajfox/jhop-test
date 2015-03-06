package com.techstar.modules.activiti.form;

import java.util.Arrays;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 多用户表单字段类型,用逗号（,）分隔
 * 
 * @author henryyan
 */
public class UsersFormType extends AbstractUserFormType {

	public static final String TYPE_NAME = "users";

	@Override
	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {

		if (StringUtils.isNotEmpty(propertyValue)) {
			String[] split = StringUtils.split(propertyValue, ",");

			// Check if user exists
			checkUsers(split);

			return Arrays.asList(split);
		}

		return null;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		return ObjectUtils.toString(modelValue);
	}

}
