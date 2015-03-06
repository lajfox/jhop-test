package com.techstar.modules.activiti.form;

import org.activiti.engine.impl.form.StringFormType;

/**
 * textarea表单类型
 * 
 * @author sundoctor
 * 
 */
public class TextAreaFormType extends StringFormType {

	public static final String TYPE_NAME = "textarea";
	
	protected int rows = 5;
	protected int cols = 30;
	
	public TextAreaFormType(){
		this(5, 50);
	}
	
	public TextAreaFormType(int rows,int cols){
		this.rows = rows;
		this.cols = cols;
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}
	
	

}
