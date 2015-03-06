package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

public class JasperReportsXlsxView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsXlsxView() {
		setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	}

	@Override
	protected JRExporter createExporter() {
		return new JRXlsxExporter();
	}

	@Override
	protected boolean useWriter() {
		return false;
	}
	
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
