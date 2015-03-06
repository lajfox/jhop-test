package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.jasperreports.web.servlet.support.RequestContext;
import com.techstar.modules.utils.FreeMarkerUtils;

public class JasperReportsHtmlView extends org.springframework.web.servlet.view.jasperreports.JasperReportsHtmlView {

	private static final Pattern PAGEINDEXPATTERN = Pattern.compile("&jr_pageindex=[\\d|\\s]*",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern USERSESSIONPATTERN = Pattern.compile("&jr_usesession=[true|false|\\s+]*",
			Pattern.CASE_INSENSITIVE);

	protected void renderReportUsingWriter(JRExporter exporter, JasperPrint populatedReport,
			HttpServletResponse response) throws Exception {

		RequestContext requestContext = JasperReportsMultiFormatView.getRequestContext();
		HttpServletRequest request = requestContext.getRequest();

		if (requestContext.isHtmlsession()) {
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, populatedReport);
		}

		String imageURI = StringUtils.trim(requestContext.getImageUri());
		if (StringUtils.isNotEmpty(imageURI)) {
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
					String.format(imageURI, requestContext.getContextPath(), new Date().getTime()));
		}

		createHtmlHeader(exporter, populatedReport, requestContext);

		JasperReportsMultiFormatView.removeRequestContext();
		super.renderReportUsingWriter(exporter, populatedReport, response);
	}

	/**
	 * 通过freemarker模板生成html设置JRHtmlExporterParameter.HTML_HEADER，子类可以Override
	 * 
	 * @param exporter
	 * @param requestContext
	 */
	protected void createHtmlHeader(final JRExporter exporter, final JasperPrint populatedReport,
			final RequestContext requestContext) {
		String headerftl = requestContext.getHtmlheader();
		if (StringUtils.isNotEmpty(headerftl)) {

			Map<String, Object> data = new HashMap<String, Object>();

			if (requestContext.isShowpage()) {

				int pageSize = populatedReport.getPages().size();
				Integer pageIndex = requestContext.getPageindex();
				int lastPageIndex = pageSize - 1;

				data.put("pageSize", pageSize);
				data.put("lastPageIndex", lastPageIndex);
				data.put("pageIndex", pageIndex);

				if (pageSize > 1 && pageIndex != null) {
					exporter.setParameter(JRHtmlExporterParameter.PAGE_INDEX, pageIndex);

					String queryString = requestContext.getQueryString();
					if (StringUtils.isNotEmpty(queryString)) {

						data.put("firstPage", pageIndex == 0 ? null : buildQueryString(queryString, 0));
						data.put("nextPage",
								(pageIndex + 1) <= lastPageIndex ? buildQueryString(queryString, pageIndex + 1) : null);
						data.put("previousPage", (pageIndex - 1) >= 0 ? buildQueryString(queryString, pageIndex - 1)
								: null);
						data.put("lastPage",
								pageIndex == lastPageIndex ? null : buildQueryString(queryString, lastPageIndex));

					}

				}
			} else {
				exporter.setParameter(JRHtmlExporterParameter.PAGE_INDEX, null);
			}

			data.put("ctx", requestContext.getContextPath());
			data.put("queryString", requestContext.getQueryString());
			data.put("requestURI", requestContext.getRequestURI());
			data.put("showpage", requestContext.isShowpage());
			data.put("downloadMappings", requestContext.getDownloadMappings());

			String html = FreeMarkerUtils.crateHTML(requestContext.getServletContext(), data,
					requestContext.getDirectory(), headerftl);
			if (StringUtils.isNotEmpty(html)) {
				exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, html);
			}
		}
	}

	private String buildQueryString(String queryString, int pageIndex) {
		StringBuilder sb = new StringBuilder(queryString.length() + 30);

		queryString = this.remove(PAGEINDEXPATTERN, queryString);
		queryString = this.remove(USERSESSIONPATTERN, queryString);

		sb.append(queryString);
		sb.append("&").append(RequestContext.PAGEINDEXKEY).append("=").append(pageIndex);
		sb.append("&").append(RequestContext.USESESSIONKEY).append("=true");

		return sb.toString();
	}

	private String remove(final Pattern pattern, final String queryString) {
		Matcher m = pattern.matcher(queryString);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
