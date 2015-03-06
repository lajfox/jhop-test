package com.techstar.modules.highcharts.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Tooltip {

	private String valueSuffix;

	public Tooltip(String valueSuffix) {
		this.valueSuffix = valueSuffix;
	}

	public String getValueSuffix() {
		return valueSuffix;
	}

	public void setValueSuffix(String valueSuffix) {
		this.valueSuffix = valueSuffix;
	}

}
