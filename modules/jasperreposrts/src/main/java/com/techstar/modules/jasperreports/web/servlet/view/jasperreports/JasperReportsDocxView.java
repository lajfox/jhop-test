package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

public class JasperReportsDocxView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsDocxView() {
		setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
	}

	@Override
	protected JRExporter createExporter() {
		return new JRDocxExporter();
	}

	@Override
	protected boolean useWriter() {
		return false;
	}
}
