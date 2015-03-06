package com.techstar.modules.activiti.form;

import java.util.Map;

import org.activiti.engine.impl.form.EnumFormType;

/**
 * select 表单类型，单选，同EnumFormType
 * 
 * @author sundoctor
 * 
 */
public class SelectFormType extends EnumFormType {

	public static final String TYPE_NAME = "select";

	public SelectFormType(Map<String, String> values) {
		super(values);
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

}
