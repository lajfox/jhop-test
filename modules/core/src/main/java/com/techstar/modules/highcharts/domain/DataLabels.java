package com.techstar.modules.highcharts.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class DataLabels {

	private Boolean enabled;
	private Integer rotation;
	private String color;
	private String align;
	private Integer x;
	private Integer y;
	private Style style;

	public DataLabels(Boolean enabled) {
		this.enabled = enabled;
	}

	public DataLabels(Boolean enabled, Style style) {
		this(enabled);
		this.style = style;
	}

	public DataLabels(Boolean enabled, Integer x, Integer y) {
		this(enabled);
		this.x = x;
		this.y = y;
	}

	public DataLabels(Boolean enabled, Integer x, Integer y, Style style) {
		this(enabled, x, y);
		this.style = style;
	}

	public DataLabels(Boolean enabled, String color) {
		this(enabled);
		this.color = color;
	}

	public DataLabels(Boolean enabled, String color, Style style) {
		this(enabled, color);
		this.style = style;
	}

	public DataLabels(Boolean enabled, String color, Integer x, Integer y) {
		this(enabled, color);
		this.x = x;
		this.y = y;
	}

	public DataLabels(Boolean enabled, String color, Integer x, Integer y, Style style) {
		this(enabled, color, x, y);
		this.style = style;
	}

	public DataLabels(Boolean enabled, Integer rotation) {
		this(enabled);
		this.rotation = rotation;
	}

	public DataLabels(Boolean enabled, Integer rotation, Style style) {
		this(enabled, rotation);
		this.style = style;
	}

	public DataLabels(Boolean enabled, Integer rotation, Integer x, Integer y) {
		this(enabled, rotation);
		this.x = x;
		this.y = y;
	}

	public DataLabels(Boolean enabled, Integer rotation, Integer x, Integer y, Style style) {
		this(enabled, rotation, x, y);
		this.style = style;
	}

	public DataLabels(Boolean enabled, String color, Integer rotation) {
		this(enabled, rotation);
		this.color = color;
	}

	public DataLabels(Boolean enabled, String color, Integer rotation, Style style) {
		this(enabled, color, rotation);
		this.style = style;
	}

	public DataLabels(Boolean enabled, String color, Integer rotation, Integer x, Integer y) {
		this(enabled, color, rotation);
		this.x = x;
		this.y = y;
	}

	public DataLabels(Boolean enabled, String color, Integer rotation, Integer x, Integer y, Style style) {
		this(enabled, color, rotation, x, y);
		this.style = style;
	}

	public DataLabels(Boolean enabled, Integer rotation, String align) {
		this(enabled, rotation);
		this.align = align;
	}

	public DataLabels(Boolean enabled, Integer rotation, String align, Style style) {
		this(enabled, rotation, align);
		this.style = style;
	}

	public DataLabels(Boolean enabled, Integer rotation, String align, Integer x, Integer y) {
		this(enabled, rotation, align);
		this.x = x;
		this.y = y;
	}

	public DataLabels(Boolean enabled, Integer rotation, String align, Integer x, Integer y, Style style) {
		this(enabled, rotation, align, x, y);
		this.style = style;
	}

	public DataLabels(Boolean enabled, String color, Integer rotation, String align) {
		this(enabled, rotation, align);
		this.color = color;
	}

	public DataLabels(Boolean enabled, String color, Integer rotation, String align, Style style) {
		this(enabled, color, rotation, align);
		this.style = style;
	}

	public DataLabels(Boolean enabled, String color, Integer rotation, String align, Integer x, Integer y) {
		this(enabled, color, rotation, align);
		this.x = x;
		this.y = y;
	}

	public DataLabels(Boolean enabled, String color, Integer rotation, String align, Integer x, Integer y, Style style) {
		this(enabled, color, rotation, align, x, y);
		this.style = style;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getRotation() {
		return rotation;
	}

	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

}
