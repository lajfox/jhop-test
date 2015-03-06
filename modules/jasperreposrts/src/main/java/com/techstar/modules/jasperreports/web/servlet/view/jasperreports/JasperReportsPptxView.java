package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

public class JasperReportsPptxView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsPptxView() {
		setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
	}

	@Override
	protected JRExporter createExporter() {
		return new JRPptxExporter();
	}

	@Override
	protected boolean useWriter() {
		return false;
	}
}
