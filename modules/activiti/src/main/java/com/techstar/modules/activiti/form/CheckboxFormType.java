package com.techstar.modules.activiti.form;

import java.util.Map;

/**
 * checkbox表单类型
 * 
 * @author sundoctor
 * 
 */
public class CheckboxFormType extends MultipleValuesFormType {

	public static final String TYPE_NAME = "checkbox";

	public CheckboxFormType(Map<String, String> values) {
		super(values);
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

}
