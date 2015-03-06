package com.techstar.modules.jasperreports.web.servlet.support;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

public class RequestContext {

	public static final String JRKEY = "jr_";
	public static final String URLKEY = "jr_url";
	public static final String REPORTDATAKEYKEY = "jr_data";
	public static final String SUBURLKEY = "jr_suburl";
	public static final String SUBREPORTDATAKEY = "jr_subdata";

	public static final String FORMATKEY = JasperReportsMultiFormatView.DEFAULT_FORMAT_KEY;
	public static final String HTMLSESSIONKEY = "jr_htmlsession";
	public static final String PAGEINDEXKEY = "jr_pageindex";
	public static final String HTMLHEADERKEY = "jr_htmlheader";
	public static final String HTMLFOOTERKEY = "jr_htmlfooter";
	public static final String FILENAMEKEY = "jr_filename";
	public static final String USESESSIONKEY = "jr_usesession";
	public static final String SHOWPAGEKEY = "jr_showpage";
	public static final String IMAGES_URI_KEY = "images_uri";

	private HttpServletRequest request;

	private HttpServletResponse response;

	private Map<String, Object> model;

	private ServletContext servletContext;

	private boolean htmlsession;
	private String directory;
	private String htmlheaderftl;
	private String htmlfooterftl;
	private boolean showpage;

	private List<String> downloadMappings;

	public RequestContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model,
			ServletContext servletContext, boolean htmlsession, String directory, String htmlheaderftl,
			String htmlfooterftl, List<String> downloadMappings, boolean showpage) {
		initContext(request, response, model, servletContext, htmlsession, directory, htmlheaderftl, htmlfooterftl,
				downloadMappings, showpage);
	}

	protected void initContext(final HttpServletRequest request, final HttpServletResponse response,
			final Map<String, Object> model, final ServletContext servletContext, boolean htmlsession,
			String directory, String htmlheaderftl, String htmlfooterftl, List<String> downloadMappings,
			boolean showpage) {

		this.request = request;
		this.response = response;
		this.model = model;
		this.htmlsession = htmlsession;
		this.directory = directory;
		this.htmlfooterftl = htmlfooterftl;
		this.htmlheaderftl = htmlheaderftl;
		this.downloadMappings = downloadMappings;
		this.servletContext = servletContext;
		this.showpage = showpage;

	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String getContextPath() {
		return this.request.getContextPath();
	}

	public String getQueryString() {
		return this.request.getQueryString();
	}

	public String getRequestURI() {
		return this.request.getRequestURI();
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public String getUrl() {
		String url = MapUtils.getString(model, URLKEY);
		return StringUtils.isEmpty(url) ? this.request.getParameter(URLKEY) : url;
	}

	public String getData() {
		String data = MapUtils.getString(model, REPORTDATAKEYKEY);
		return StringUtils.isEmpty(data) ? this.request.getParameter(REPORTDATAKEYKEY) : data;
	}

	public String getSubUrl() {
		String url = MapUtils.getString(model, SUBURLKEY);
		return StringUtils.isEmpty(url) ? this.request.getParameter(SUBURLKEY) : url;
	}

	public String getSubData() {
		String data = MapUtils.getString(model, SUBREPORTDATAKEY);
		return StringUtils.isEmpty(data) ? this.request.getParameter(SUBREPORTDATAKEY) : data;
	}

	public String getFormat() {
		String formart = MapUtils.getString(model, FORMATKEY);
		return StringUtils.isEmpty(formart) ? this.request.getParameter(FORMATKEY) : formart;
	}

	public boolean isHtmlsession() {
		String str = MapUtils.getString(model, HTMLSESSIONKEY);
		return StringUtils.isEmpty(str) ? this.htmlsession : BooleanUtils.toBoolean(str);
	}

	public boolean isShowpage() {
		String str = MapUtils.getString(model, SHOWPAGEKEY);
		return StringUtils.isEmpty(str) ? this.showpage : BooleanUtils.toBoolean(str);
	}

	public Integer getPageindex() {
		Integer i = NumberUtils.createInteger(MapUtils.getString(model, PAGEINDEXKEY));
		return i == null ? NumberUtils.createInteger(this.request.getParameter(PAGEINDEXKEY)) : i;
	}

	public String getHtmlheader() {
		String str = MapUtils.getString(model, HTMLHEADERKEY);
		return StringUtils.isEmpty(str) ? this.htmlheaderftl : str;
	}

	public String getHtmlfooter() {
		String str = MapUtils.getString(model, HTMLFOOTERKEY);
		return StringUtils.isEmpty(str) ? this.htmlfooterftl : str;
	}

	public String getDirectory() {
		return directory;
	}

	public List<String> getDownloadMappings() {
		return downloadMappings;
	}

	public String getImageUri() {
		return MapUtils.getString(model, IMAGES_URI_KEY);
	}

}
