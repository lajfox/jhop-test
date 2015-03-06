package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

public class JasperReportsRftView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsRftView() {
		setContentType("application/rtf");
	}

	@Override
	protected JRExporter createExporter() {
		return new JRRtfExporter();
	}

	@Override
	protected boolean useWriter() {
		return true;
	}
}
