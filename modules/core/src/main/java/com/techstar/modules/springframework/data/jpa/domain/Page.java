package com.techstar.modules.springframework.data.jpa.domain;

public class Page {

	private int rows;// 每页显示的行数
	private int totalrows;// 一次加载记录总数
	private int page;// 当前页码

	private String sidx;// 排序字段
	private String sord;// 排序值 asc/desc

	public Page() {
		this(0, 10);
	}

	public Page(int page, int rows) {
		this.page = page;
		this.rows = rows;
	}

	public int getRows() {
		if (this.totalrows > 0 && this.rows < this.totalrows) {
			this.rows = this.totalrows;
		}
		return this.rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getTotalrows() {
		return this.totalrows;
	}

	public void setTotalrows(int totalrows) {
		this.totalrows = totalrows;
	}

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSidx() {
		return this.sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return this.sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

}
