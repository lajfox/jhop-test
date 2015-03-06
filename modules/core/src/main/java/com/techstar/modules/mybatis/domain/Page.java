package com.techstar.modules.mybatis.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * <p>
 * 分页信息。
 * </p>
 * <p>
 * 继承ArrayList是因为如果方法的返回类型是Page，而mybatis有如下判断：
 * </p>
 * 
 * <pre>
 * if (List.class.isAssignableFrom(method.getReturnType())) {
 * 	returnsList = true;// 即只有返回List才执行selectList。
 * }
 * </pre>
 * 
 * *
 * 
 * @author ZengWenfeng
 * @date 2013-1-14
 */
public class Page<T> extends ArrayList<T> {

	private static final long serialVersionUID = -1869849102804369584L;

	private List<T> content = new ArrayList<T>();// 当前页记录集合
	private long total;// 记录总数
	private int number;// 当前页码
	private int size;// 每页记录数

	public Page() {
	}

	public Page(int number, int size) {
		this.number = number;
		this.size = size;
	}

	public Page(int number, int size, long total) {
		this(number, size);
		this.total = total;
	}

	public Page(List<T> content, int number, int size, long total) {
		this(number, size, total);
		this.content = content;
	}

	public int getNumber() {
		return this.number;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}

	public int getTotalPages() {
		return getSize() == 0 ? 0 : (int) Math.ceil((double) total / (double) getSize());
	}

	public int getNumberOfElements() {
		return content == null ? 0 : content.size();
	}

	public long getTotalElements() {
		return total;
	}

	public void setTotalElements(long total) {
		this.total = total;
	}

	public boolean hasPreviousPage() {
		return getNumber() > 0;
	}

	public boolean isFirstPage() {

		return !hasPreviousPage();
	}

	public boolean hasNextPage() {

		return ((getNumber() + 1) * getSize()) < total;
	}

	public boolean isLastPage() {

		return !hasNextPage();
	}

	public Iterator<T> iterator() {

		return content.iterator();
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public List<T> getContent() {
		return Collections.unmodifiableList(content);
	}

	public boolean hasContent() {

		return !content.isEmpty();
	}

	public int getOffset() {
		return this.getNumber() * this.getSize();
	}

	public int getLimit() {
		return this.getOffset() + this.getSize();
	}

}
