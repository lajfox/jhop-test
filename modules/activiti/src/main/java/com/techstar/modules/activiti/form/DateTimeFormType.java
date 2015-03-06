package com.techstar.modules.activiti.form;

import org.activiti.engine.impl.form.DateFormType;

/**
 * 时间表单类型:yyyy-MM-dd HH:mm
 * @author sundoctor
 *
 */
public class DateTimeFormType extends DateFormType {

	public static final String TYPE_NAME = "datetime";

	public DateTimeFormType() {
		this("yyy-MM-dd HH:mm");
	}
	
	public DateTimeFormType(String datePattern) {
		super(datePattern);
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

}
