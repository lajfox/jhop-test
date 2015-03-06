package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

public class JasperReportsOdsView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsOdsView() {
		setContentType("application/vnd.oasis.opendocument.spreadsheet");
	}

	@Override
	protected JRExporter createExporter() {
		return new JROdsExporter();
	}

	@Override
	protected boolean useWriter() {
		return false;
	}
}
