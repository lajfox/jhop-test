package com.techstar.modules.activiti.form;

import org.activiti.engine.form.AbstractFormType;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 多用户表单字段类型,用逗号（,）分隔
 * 
 * @author henryyan
 */
public class CustomFormType extends AbstractFormType {

	public static final String TYPE_NAME = "custom";

	@Override
	public String getName() {
		return TYPE_NAME;
	}

	@Override
	public Object convertFormValueToModelValue(String propertyValue) {
		return propertyValue;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		return ObjectUtils.toString(modelValue);
	}

}
