package com.techstar.modules.activiti.form;

import java.util.Map;

/**
 * select 表单类型，多选
 * 
 * @author sundoctor
 * 
 */
public class MultipleSelectFormType extends MultipleValuesFormType {

	public static final String TYPE_NAME = "multipleselect";
	
	protected int size;	

	public MultipleSelectFormType(Map<String, String> values) {
		this(values,5);
	}
	
	public MultipleSelectFormType(Map<String, String> values,int size) {
		super(values);
		this.size = size;
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	

}
