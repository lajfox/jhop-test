package com.techstar.modules.activiti.form;

import org.activiti.engine.impl.form.StringFormType;

/**
 * 表单隐藏值类型
 * 
 * @author sundoctor
 * 
 */
public class HiddenFormType extends StringFormType {

	public static final String TYPE_NAME = "hidden";

	@Override
	public String getName() {
		return TYPE_NAME;
	}

}
