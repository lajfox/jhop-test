package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRXml4SwfExporter;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

public class JasperReportsXml4SwfView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsXml4SwfView() {
		setContentType("text/xml;charset=UTF-8");
	}

	@Override
	protected JRExporter createExporter() {
		return new JRXml4SwfExporter();
	}

	@Override
	protected boolean useWriter() {
		return true;
	}
}
