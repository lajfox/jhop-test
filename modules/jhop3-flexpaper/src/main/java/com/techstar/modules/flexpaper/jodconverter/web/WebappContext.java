package com.techstar.modules.flexpaper.jodconverter.web;

import java.io.File;

import javax.servlet.ServletContext;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.artofsolving.jodconverter.util.PlatformUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techstar.modules.flexpaper.util.ProcessUtils;

public class WebappContext {

	private static final Logger logger = LoggerFactory.getLogger(WebappContext.class);

	private static final String PARAMETER_OFFICE_PORT = "office.port";
	private static final String PARAMETER_OFFICE_HOME = "office.home";
	private static final String PARAMETER_OFFICE_PROFILE = "office.profile";
	private static final String KEY = WebappContext.class.getName();

	private final OfficeManager officeManager;
	private final OfficeDocumentConverter documentConverter;

	public WebappContext(ServletContext servletContext) {

		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		String officePortParam = servletContext.getInitParameter(PARAMETER_OFFICE_PORT);
		if (officePortParam != null) {
			configuration.setPortNumber(Integer.parseInt(officePortParam));
		}
		String officeHomeParam = servletContext.getInitParameter(PARAMETER_OFFICE_HOME);
		logger.info("officeHomeParam:{}", officeHomeParam);
		if (officeHomeParam != null) {
			configuration.setOfficeHome(new File(officeHomeParam));
		}
		String officeProfileParam = servletContext.getInitParameter(PARAMETER_OFFICE_PROFILE);
		if (officeProfileParam != null) {
			configuration.setTemplateProfileDir(new File(officeProfileParam));
		}

		officeManager = configuration.buildOfficeManager();
		documentConverter = new OfficeDocumentConverter(officeManager);
	}

	protected static void init(ServletContext servletContext) {
		WebappContext instance = new WebappContext(servletContext);
		servletContext.setAttribute(KEY, instance);

		if (PlatformUtils.isWindows()) {
			ProcessUtils.exec("taskkill /f /im soffice.bin");
		} else {
			// ProcessUtils.exec("pkill -9 soffice.bin");
			ProcessUtils.exec("killall -9 soffice.bin");
		}

		instance.officeManager.start();
	}

	protected static void destroy(ServletContext servletContext) {
		WebappContext instance = get(servletContext);
		instance.officeManager.stop();
	}

	public static WebappContext get(ServletContext servletContext) {
		return (WebappContext) servletContext.getAttribute(KEY);
	}

	public OfficeManager getOfficeManager() {
		return officeManager;
	}

	public OfficeDocumentConverter getDocumentConverter() {
		return documentConverter;
	}

}
