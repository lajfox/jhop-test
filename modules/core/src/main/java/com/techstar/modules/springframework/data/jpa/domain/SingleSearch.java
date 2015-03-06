package com.techstar.modules.springframework.data.jpa.domain;


public final class SingleSearch {

	// 单条件搜索字段值
	private String searchString;
	// 单条件搜索操作
	private String searchOper;
	// 单条件查询字段
	private String searchField;

	public String getSearchString() {
		return this.searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchOper() {
		return this.searchOper;
	}

	public void setSearchOper(String searchOper) {
		this.searchOper = searchOper;
	}

	public String getSearchField() {
		return this.searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

}
