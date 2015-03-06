package com.techstar.modules.highcharts.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//@JsonInclude(Include.NON_EMPTY)
public class Category<T> {

	private T name;
	private Number y;
	@JsonInclude(Include.NON_EMPTY)
	private String color;

	public Category(T name, Number y) {
		this.name = name;
		this.y = y;
	}

	public Category(T name, Number y, String color) {
		this(name,y);
		this.color = color;
	}

	public Category() {

	}

	public T getName() {
		return name;
	}

	public void setName(T name) {
		this.name = name;
	}

	public Number getY() {
		return y;
	}

	public void setY(Number y) {
		this.y = y;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
