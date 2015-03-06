package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

/***
 * Xls格式文件导出，根据参数指定文件名(参数名：P_FILENAME)
 * @author lrm
 * 2014-08-26
 */
public class JasperReportsXlsView extends
		org.springframework.web.servlet.view.jasperreports.JasperReportsXlsView {
	
	/****
	 * 重写构造报表方法，目的是设置导出文件名
	 */
	@Override
	protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response)
			throws Exception {
		String fileName = model.get("P_FILENAME")==null?"file.xls":(String)model.get("P_FILENAME");
		fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");//java.net.URLEncoder.encode(fileName, "UTF-8")
		response.setHeader(HEADER_CONTENT_DISPOSITION, "attachment;filename="+fileName);
		super.renderReport(populatedReport, model, response);
	}
}
