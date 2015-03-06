package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import java.io.ObjectOutputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsSingleFormatView;

public class JasperReportsAppletView extends AbstractJasperReportsSingleFormatView {

	public JasperReportsAppletView() {
		setContentType("application/octet-stream");
	}

	@Override
	protected void renderReport(JasperPrint populatedReport, Map<String, Object> model, HttpServletResponse response)
			throws Exception {

		ServletOutputStream ouputStream = response.getOutputStream();

		ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
		oos.writeObject(populatedReport);
		oos.flush();
		oos.close();

		ouputStream.flush();
		ouputStream.close();

	}

	@Override
	protected JRExporter createExporter() {
		return null;
	}

	@Override
	protected boolean useWriter() {
		return false;
	}

}
