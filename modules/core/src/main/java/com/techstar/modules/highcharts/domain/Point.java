package com.techstar.modules.highcharts.domain;

import org.apache.commons.lang3.ArrayUtils;

public class Point {

	private String[] properties;

	public Point(String... properties) {
		this.properties = properties;
	}

	public String[] getProperties() {
		return properties;
	}

	public void setProperties(String[] properties) {
		this.properties = properties;
	}

	public String getColor() {
		return properties.length >= 3 ? properties[2] : null;
	}

	public boolean isTime() {
		return false;
	}

	public boolean isCategories() {
		return false;
	}

	public boolean valid() {
		if (ArrayUtils.isNotEmpty(this.getProperties())) {
			return true;
		} else {
			throw new IllegalArgumentException("参数错误，参数properties为空");
		}
	}

}
