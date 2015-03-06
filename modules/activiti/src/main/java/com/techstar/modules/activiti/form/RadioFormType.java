package com.techstar.modules.activiti.form;

import java.util.Map;

import org.activiti.engine.impl.form.EnumFormType;

/**
 * radio表单类型
 * 
 * @author sundoctor
 * 
 */
public class RadioFormType extends EnumFormType {

	public static final String TYPE_NAME = "radio";

	public RadioFormType(Map<String, String> values) {
		super(values);
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

}
