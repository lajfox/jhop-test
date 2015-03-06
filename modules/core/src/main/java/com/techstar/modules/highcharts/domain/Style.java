package com.techstar.modules.highcharts.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Style {

	private String fontSize;
	private String fontFamily;
	private String textShadow;

	public Style(String fontSize) {
		this.fontSize = fontSize;
	}

	public Style(String fontSize, String fontFamily) {
		this(fontSize);
		this.fontFamily = fontFamily;
	}

	public Style(String fontSize, String fontFamily, String textShadow) {
		this(fontSize, fontFamily);
		this.textShadow = textShadow;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public String getTextShadow() {
		return textShadow;
	}

	public void setTextShadow(String textShadow) {
		this.textShadow = textShadow;
	}

}
