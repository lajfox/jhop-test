package com.techstar.modules.highcharts.domain;

import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Highcharts {

	private Collection<Series> series;
	private Collection<String> categories;
	private Map<String, Object> map;

	public Collection<Series> getSeries() {
		return series;
	}

	public void setSeries(Collection<Series> series) {
		this.series = series;
	}

	public Collection<String> getCategories() {
		return categories;
	}

	public void setCategories(Collection<String> categories) {
		this.categories = categories;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}
