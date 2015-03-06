package com.techstar.modules.highcharts.domain;

public class Item {

	private Series series;
	private Point key;
	
	public Item(Series series,Point key){
		this.series = series;
		this.key = key;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public Point getKey() {
		return key;
	}

	public void setKey(Point key) {
		this.key = key;
	}

}
