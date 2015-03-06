package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

public class JasperReportsOdtView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsOdtView() {
		setContentType("application/vnd.oasis.opendocument.text");
	}

	@Override
	protected JRExporter createExporter() {
		return new JROdtExporter();
	}

	@Override
	protected boolean useWriter() {
		return false;
	}
}
