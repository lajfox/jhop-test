package com.techstar.modules.mybatis.domain;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;

import com.techstar.modules.springframework.data.jpa.domain.Filters;
import com.techstar.modules.springframework.data.jpa.domain.Page;
import com.techstar.modules.springframework.data.jpa.domain.SingleSearch;

public class MyRowBounds extends RowBounds {

	private static final Page DEFAULTPAGE = new Page();
	private Page page = DEFAULTPAGE;// 分页／排序
	private SingleSearch singleSearch;// 单条件查询
	private Filters filters;// 高级查询
	private Map<String, String> toolbarSearchMap;// 工具栏中要查询的字段

	private boolean pagination = true;// 是否分页
	private boolean autoCount = true;// 是否自动查询总记录数
	private Map<String, String> dataTypeMap;// 设置各字段的类型,不设置默认为string
	private long total;// 查询数据记录总数

	private Filters defFilters;// 自定义查询

	public MyRowBounds() {
		super();
	}

	public MyRowBounds(int offset, int limit) {
		super(offset, limit);
	}

	public MyRowBounds(int offset, int limit, boolean pagination) {
		super(offset, limit);
		this.pagination = pagination;
	}

	public MyRowBounds(int offset, int limit, boolean pagination, boolean autoCount) {
		this(offset, limit, pagination);
		this.autoCount = autoCount;
	}

	public Filters getDefFilters() {
		return defFilters;
	}

	public void setDefFilters(Filters defFilters) {
		this.defFilters = defFilters;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public SingleSearch getSingleSearch() {
		return singleSearch;
	}

	public void setSingleSearch(SingleSearch singleSearch) {
		this.singleSearch = singleSearch;
	}

	public Filters getFilters() {
		return filters;
	}

	public void setFilters(Filters filters) {
		this.filters = filters;
	}

	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	public boolean isPagination() {
		return pagination;
	}

	public void setPagination(boolean pagination) {
		this.pagination = pagination;
	}

	public Map<String, String> getDataTypeMap() {
		return dataTypeMap;
	}

	public void setDataTypeMap(Map<String, String> dataTypeMap) {
		this.dataTypeMap = dataTypeMap;
	}

	public Map<String, String> getToolbarSearchMap() {
		return toolbarSearchMap;
	}

	public void setToolbarSearchMap(Map<String, String> toolbarSearchMap) {
		this.toolbarSearchMap = toolbarSearchMap;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public String getOrders() {
		if (page != null && StringUtils.isNotEmpty(page.getSidx()) && StringUtils.isNotEmpty(page.getSord())) {
			return this.page.getSidx() + " " + this.page.getSord();
		} else {
			return null;
		}
	}

	public boolean hasSingle() {
		return singleSearch != null && StringUtils.isNotEmpty(singleSearch.getSearchField());
	}

	public boolean hasSearch() {
		return hasSingle() || (MapUtils.isNotEmpty(toolbarSearchMap) && filters == null)
				|| (filters != null && CollectionUtils.isNotEmpty(filters.getRules()));
	}
}
