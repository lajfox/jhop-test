package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;

public class JasperReportsXHtmlView extends JasperReportsHtmlView {

	public JasperReportsXHtmlView() {
		setContentType("text/html");
	}

	@Override
	protected JRExporter createExporter() {
		return new JRXhtmlExporter();
	}

}
