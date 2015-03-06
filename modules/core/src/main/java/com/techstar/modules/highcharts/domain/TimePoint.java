package com.techstar.modules.highcharts.domain;

import org.apache.commons.lang3.ArrayUtils;

public class TimePoint extends Point {

	public TimePoint(String... properties) {
		super(properties);
	}

	

	@Override
	public boolean isTime() {
		return true;
	}

	@Override
	public boolean valid() {
		if (ArrayUtils.isNotEmpty(this.getProperties()) && this.getProperties().length >= 2) {
			return true;
		} else {
			throw new IllegalArgumentException("参数错误，参数必须为[\"x\",\"y\"]");
		}
	}
}
