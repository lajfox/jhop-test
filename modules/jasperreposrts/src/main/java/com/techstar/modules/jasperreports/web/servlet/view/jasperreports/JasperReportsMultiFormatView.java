package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import com.techstar.modules.jasperreports.web.servlet.support.RequestContext;

/**
 * 
 * @author sundoctor
 * 
 */
public class JasperReportsMultiFormatView extends
		org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView {

	private final Map<String, JasperReport> jasperReportMap = new HashMap<String, JasperReport>();
	private final Map<String, Map<String, JasperReport>> subJasperReportMap = new HashMap<String, Map<String, JasperReport>>();
	private static final ThreadLocal<RequestContext> requestContextThreadLocal = new ThreadLocal<RequestContext>();

	/**
	 * jasper报表模板存放默认目录
	 */
	private static final String DEFAULT_DIRECTORY = "WEB-INF/jasper/";

	/**
	 * jasper报表模板存放目录
	 */
	private String directory = DEFAULT_DIRECTORY;

	/**
	 * 当debug为true时，每次调用重新加载编译JasperReport，开发时可以设置为true,生产环境最好设置为false，提高效率
	 */
	private boolean debug = false;

	/**
	 * 当formart为html是否在session中保存
	 */
	private boolean htmlsession = true;

	private String myRequestContextAttribute;

	/**
	 * Stores the mappings of mapping keys to use session values.
	 */
	private Properties useSessionMappings;

	/**
	 * html header freemartker模板文件名称
	 */
	private String htmlheaderftl;

	/**
	 * html footer freemartker模板文件名称
	 */
	private String htmlfooterftl;

	/**
	 * 配置在html报表的下载格式
	 */
	private List<String> downloadMappings;

	/**
	 * html报表是否显示分页
	 */
	private boolean showpage;

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDirectory() {
		return this.directory.endsWith("/") ? this.directory : this.directory + "/";
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setDownloadMappings(List<String> downloadMappings) {
		this.downloadMappings = downloadMappings;
	}

	public void setHtmlsession(boolean htmlsession) {
		this.htmlsession = htmlsession;
	}

	public void setMyRequestContextAttribute(String myRequestContextAttribute) {
		this.myRequestContextAttribute = myRequestContextAttribute;
	}

	public void setUseSessionMappings(Properties useSessionMappings) {
		this.useSessionMappings = useSessionMappings;
	}

	public void setHtmlheaderftl(String htmlheaderftl) {
		this.htmlheaderftl = htmlheaderftl;
	}

	public void setHtmlfooterftl(String htmlfooterftl) {
		this.htmlfooterftl = htmlfooterftl;
	}

	public void setShowpage(boolean showpage) {
		this.showpage = showpage;
	}

	@Override
	protected void onInit() {
		loadReport(getDirectory());
	}

	public static RequestContext getRequestContext() {
		return requestContextThreadLocal.get();
	}

	public static void setRequestContext(final RequestContext requestContext) {
		requestContextThreadLocal.set(requestContext);
	}

	public static void removeRequestContext() {
		requestContextThreadLocal.remove();
	}

	@Override
	public JasperReport getReport() {

		JasperReport jasperReport = super.getReport();
		if (jasperReport == null) {
			String url = getRequestContext().getUrl();
			if (StringUtils.isNotEmpty(url)) {
				if (this.debug) {
					Resource resource = getApplicationContext().getResource(getDirectory() + url);
					jasperReport = loadReport(resource);
				} else {
					jasperReport = this.jasperReportMap.get(url);
					if (jasperReport == null) {
						Resource resource = getApplicationContext().getResource(getDirectory() + url);
						jasperReport = loadReport(resource);
						jasperReportMap.put(url, jasperReport);
					}
				}
			}
		}

		return jasperReport;
	}

	/**
	 * 处理子报表
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 主报表数据key
		//String reportDataKey = request.getParameter(RequestContext.REPORTDATAKEYKEY);
		//super.setReportDataKey(StringUtils.isEmpty(reportDataKey) ? null : reportDataKey);

		// 子报表数据keys
		String subReportDataKey = request.getParameter(RequestContext.SUBREPORTDATAKEY);
		if (StringUtils.isNotEmpty(subReportDataKey)) {
			String[] subReportDataKeys = tokenizeToStringArray(subReportDataKey,
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			for (String key : subReportDataKeys) {
				model.put(key, convertReportData(model.get(key)));
			}
		}

		String subUrl = request.getParameter(RequestContext.SUBURLKEY);
		if (StringUtils.isNotEmpty(subUrl)) {
			Resource resource = null;
			JasperReport jasperReport = null;
			String[] subUrls = tokenizeToStringArray(subUrl, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			if (this.debug) {
				for (String url : subUrls) {
					resource = getApplicationContext().getResource(getDirectory() + url);
					jasperReport = loadReport(resource);
					if (jasperReport != null) {
						model.put(url, jasperReport);
					}
				}
			} else {
				String mainUrl = request.getParameter(RequestContext.URLKEY);
				Map<String, JasperReport> subReports = subJasperReportMap.get(mainUrl);
				if (MapUtils.isEmpty(subReports)) {
					subReports = new HashMap<String, JasperReport>(subUrls.length);
				}

				for (String url : subUrls) {
					jasperReport = subReports.get(url);
					if (jasperReport == null) {
						resource = getApplicationContext().getResource(getDirectory() + url);
						jasperReport = loadReport(resource);
						if (jasperReport != null) {
							subReports.put(url, jasperReport);
						}
					}
				}

				if (MapUtils.isNotEmpty(subReports)) {
					subJasperReportMap.put(mainUrl, subReports);
					model.putAll(subReports);
				}
			}
		}

		if (StringUtils.isNotEmpty(myRequestContextAttribute)) {
			setRequestContext(createMyRequestContext(request, response, model));
		}

		super.renderMergedOutputModel(model, request, response);

	}

	/**
	 * 先从session里获取JasperPrint,如果存在直接返回，否则调用父类反回
	 */
	@Override
	protected JasperPrint fillReport(Map<String, Object> model) throws Exception {

		JasperPrint jasperPrint = null;
		if (isUseSessionMappings(model)) {
			// 先从session里获取JasperPrint
			jasperPrint = (JasperPrint) getRequestContext().getRequest().getSession()
					.getAttribute(getRequestContext().getUrl());
		}

		if (jasperPrint == null) {
			jasperPrint = super.fillReport(model);
		}

		return jasperPrint;
	}

	@Override
	protected void postProcessReport(JasperPrint populatedReport, Map<String, Object> model) throws Exception {

		String format = MapUtils.getString(model, RequestContext.FORMATKEY);
		if (StringUtils.equals("html", format) || StringUtils.equals("xhtml", format)) {
			RequestContext requestContext = getRequestContext();
			if (requestContext.isHtmlsession()) {
				// 将JasperPrint保存到session中供pdf/xls/docx等下载使用
				requestContext.getRequest().getSession().setAttribute(requestContext.getUrl(), populatedReport);
			}
		} else {
			removeRequestContext();
		}
	}

	/**
	 * Create a RequestContext to expose under the specified attribute name.
	 * <p>
	 * Default implementation creates a standard RequestContext instance for the
	 * given request and model. Can be overridden in subclasses for custom
	 * instances.
	 * 
	 * @param request
	 *            current HTTP request
	 * @param response
	 *            current HTTP response
	 * @return the RequestContext instance
	 * @see #setMyRequestContextAttribute
	 */
	protected RequestContext createMyRequestContext(final HttpServletRequest request,
			final HttpServletResponse response, final Map<String, Object> model) {
		return new RequestContext(request, response, model, getServletContext(), this.htmlsession, this.directory,
				this.htmlheaderftl, this.htmlfooterftl, this.downloadMappings, this.showpage);
	}

	/**
	 * 预先加载报表模板存放目录的*.jsper模板文件
	 * 
	 * @param directory
	 */
	private void loadReport(final String directory) {

		Resource resource = getApplicationContext().getResource(directory);
		File dir = null;
		try {
			dir = resource.getFile();

			if (!dir.isDirectory()) {
				throw new IllegalStateException(directory + "不是一个目录");
			}

			File files[] = dir.listFiles();
			if (ArrayUtils.isEmpty(files)) {
				logger.warn("{}目录为空");
			} else {
				String key = null;
				for (File file : files) {
					if (file.isFile() && file.getAbsolutePath().endsWith(".jasper")) {
						key = StringUtils
								.substringAfter(file.getAbsolutePath().replaceAll("\\\\", "/"), getDirectory());
						jasperReportMap.put(key, (JasperReport) JRLoader.loadObject(file));
					} else if (file.isDirectory()) {
						key = getDirectory()
								+ StringUtils.substringAfter(file.getAbsolutePath().replaceAll("\\\\", "/"),
										getDirectory());
						loadReport(key);
					}
				}
			}
		} catch (IOException ex) {
			throw new ApplicationContextException("Could not load JasperReports report from " + resource, ex);
		} catch (JRException ex) {
			throw new ApplicationContextException("Could not parse JasperReports report from " + resource, ex);
		}
	}

	/**
	 * 
	 * 是否使用session
	 * 
	 * @see #setUseSessionMappings
	 */
	private boolean isUseSessionMappings(Map<String, Object> model) {
		boolean result = false;
		String str = MapUtils.getString(model, RequestContext.USESESSIONKEY);
		if (StringUtils.isEmpty(str)) {

			if (this.useSessionMappings != null) {
				String format = MapUtils.getString(model, RequestContext.FORMATKEY);
				String useSession = this.useSessionMappings.getProperty(format);
				if (useSession != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Setting Content-Disposition header to: [" + useSession + "]");
					}
					result = BooleanUtils.toBoolean(useSession);
				}
			}
		} else {
			result = BooleanUtils.toBoolean(str);
		}
		return result;
	}

}
