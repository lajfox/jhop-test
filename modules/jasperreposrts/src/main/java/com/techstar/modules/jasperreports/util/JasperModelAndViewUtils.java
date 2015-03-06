package com.techstar.modules.jasperreports.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.springframework.web.servlet.ModelAndView;

import com.techstar.modules.jasperreports.web.servlet.support.RequestContext;

public final class JasperModelAndViewUtils {

	/**
	 * 数据导出
	 * 
	 * @param formatKey
	 *            导出报表格式，如pdf/xls/xlsx/docx/pptx等
	 * @param jr_url
	 *            　报表模板文件
	 * @param dataSource
	 *            JRDataSource
	 * @return jasperReportsMultiFormatView
	 */
	public static ModelAndView forword(final String formatKey, final String jr_url, final JRDataSource dataSource) {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put(RequestContext.FORMATKEY, formatKey);
		model.put(RequestContext.URLKEY, jr_url);
		model.put("mainDataSource", dataSource);

		return new ModelAndView("jasperReportsMultiFormatView", model);
	}

	/**
	 * 数据导出
	 * 
	 * @param formatKey
	 *            导出报表格式，如pdf/xls/xlsx/docx/pptx等
	 * @param jr_url
	 *            　报表模板文件
	 * @param model
	 *            报表参数
	 * @return jasperReportsMultiFormatView
	 */
	public static ModelAndView forword(final String formatKey, final String jr_url, final Map<String, Object> model) {
		model.put(RequestContext.FORMATKEY, formatKey);
		model.put(RequestContext.URLKEY, jr_url);
		return new ModelAndView("jasperReportsMultiFormatView", model);
	}
}
