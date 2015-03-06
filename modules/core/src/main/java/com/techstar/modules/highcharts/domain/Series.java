package com.techstar.modules.highcharts.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Series {

	private String name;

	private String type;

	private Integer yAxis;

	private List<?> data;

	private Tooltip tooltip;

	private Long pointStart;

	private Long pointInterval;

	private String color;
	private Double pointPadding;
	private Double pointPlacement;
	private DataLabels dataLabels;

	public Series(String name) {
		this.name = name;
	}

	public Series(String name, Tooltip tooltip) {
		this(name);
		this.tooltip = tooltip;
	}

	public Series(String name, String type) {
		this(name);
		this.type = type;
	}

	public Series(String name, String type, Tooltip tooltip) {
		this(name, type);
		this.tooltip = tooltip;
	}

	public Series(String name, Integer yAxis) {
		this(name);
		this.yAxis = yAxis;
	}

	public Series(String name, Integer yAxis, Tooltip tooltip) {
		this(name, yAxis);
		this.tooltip = tooltip;
	}

	public Series(String name, String type, Integer yAxis) {
		this(name, yAxis);
		this.type = type;
	}

	public Series(String name, String type, Integer yAxis, Tooltip tooltip) {
		this(name, type, yAxis);
		this.tooltip = tooltip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getyAxis() {
		return yAxis;
	}

	public void setyAxis(Integer yAxis) {
		this.yAxis = yAxis;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public Tooltip getTooltip() {
		return tooltip;
	}

	public void setTooltip(Tooltip tooltip) {
		this.tooltip = tooltip;
	}

	public Long getPointStart() {
		return pointStart;
	}

	public void setPointStart(Long pointStart) {
		this.pointStart = pointStart;
	}

	public Long getPointInterval() {
		return pointInterval;
	}

	public void setPointInterval(Long pointInterval) {
		this.pointInterval = pointInterval;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getPointPadding() {
		return pointPadding;
	}

	public void setPointPadding(Double pointPadding) {
		this.pointPadding = pointPadding;
	}

	public Double getPointPlacement() {
		return pointPlacement;
	}

	public void setPointPlacement(Double pointPlacement) {
		this.pointPlacement = pointPlacement;
	}

	public DataLabels getDataLabels() {
		return dataLabels;
	}

	public void setDataLabels(DataLabels dataLabels) {
		this.dataLabels = dataLabels;
	}

}
