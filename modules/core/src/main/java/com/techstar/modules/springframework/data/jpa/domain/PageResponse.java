package com.techstar.modules.springframework.data.jpa.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;

@XmlRootElement(name = "Page")
public class PageResponse<T> extends Response<T> {

	private int number;
	private int size;
	private int totalPages;
	private int numberOfElements;
	private long totalElements;

	public PageResponse() {

	}

	public PageResponse(List<T> content) {
		super(content);
	}

	public PageResponse(List<T> content, Object userdata) {
		super(content, userdata);
	}

	public PageResponse(Page<T> page) {
		if (page != null) {
			this.number = page.getNumber() + 1;
			this.size = page.getSize();
			this.totalPages = page.getTotalPages();
			this.numberOfElements = page.getNumberOfElements();
			this.totalElements = page.getTotalElements();
			this.setContent(page.getContent());
		}
	}

	public PageResponse(Page<T> page, Object userdata) {
		this(page);
		this.setUserdata(userdata);
	}

	public PageResponse(com.techstar.modules.mybatis.domain.Page<T> page) {
		if (page != null) {
			this.number = page.getNumber() + 1;
			this.size = page.getSize();
			this.totalPages = page.getTotalPages();
			this.numberOfElements = page.getNumberOfElements();
			this.totalElements = page.getTotalElements();
			this.setContent(page.getContent());
		}
	}

	public PageResponse(com.techstar.modules.mybatis.domain.Page<T> page, Object userdata) {
		this(page);
		this.setUserdata(userdata);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

}
