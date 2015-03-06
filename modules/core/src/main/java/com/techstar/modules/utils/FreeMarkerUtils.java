package com.techstar.modules.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.util.WebUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * FreeMarker工具类
 * 
 * @author sundoctor
 * 
 */
public final class FreeMarkerUtils {

	private static final Logger logger = LoggerFactory.getLogger(FreeMarkerUtils.class);
	private static final Map<String, Configuration> cfgMap = new HashMap<String, Configuration>();
	public static final String DEFAULT_TEMPLATE_DIRECTORY = "/WEB-INF/freemarker/";

	/**
	 * 创建html静态页面,模板目录默认为"/WEB-INF/freemarker/"
	 * 
	 * @param context
	 *            ServletContext
	 * @param data
	 *            模板数据
	 * @param templateFile
	 *            模板文件
	 * @return
	 */
	public static String crateHTML(final ServletContext context, final Map<String, Object> data,
			final String templateFile) {
		return crateHTML(context, data, DEFAULT_TEMPLATE_DIRECTORY, templateFile);
	}

	/**
	 * 创建html静态页面
	 * 
	 * @param context
	 *            ServletContext
	 * @param data
	 *            模板数据
	 * @param templatedirectory
	 *            模板目录
	 * @param templateFile
	 *            模板文件
	 * @return
	 */
	public static String crateHTML(final ServletContext context, final Map<String, Object> data,
			final String templateDirectory, final String templateFile) {

		Configuration configuration = getConfiguration(context, templateDirectory);

		try {
			Template t = configuration.getTemplate(templateFile, "UTF-8");
			return FreeMarkerTemplateUtils.processTemplateIntoString(t, data);
		} catch (IOException e) {
			logger.error("生成html静态文件异常", e);
			throw new RuntimeException("生成html静态文件异常", e);
		} catch (TemplateException e) {
			logger.error("生成html静态文件失异常", e);
			throw new RuntimeException("生成html静态文件异常", e);
		}
	}

	/**
	 * 创建html静态页面
	 * 
	 * @param clazz
	 *            Class
	 * 
	 * @param data
	 *            模板数据
	 * @param pathPrefix
	 *            模板文件所在的classpath目录,即对应参数clazz的classpath下任何一级目录
	 * @param templateFile
	 *            模板文件,应该存在于参数pathPrefix的目录下
	 * @return
	 */
	public static String crateHTML(final Class<?> clazz, final Map<String, Object> data, final String pathPrefix,
			final String templateFile) {

		Configuration configuration = getConfiguration(clazz, pathPrefix);

		try {
			Template t = configuration.getTemplate(templateFile, "UTF-8");
			return FreeMarkerTemplateUtils.processTemplateIntoString(t, data);
		} catch (IOException e) {
			logger.error("生成html静态文件异常", e);
			throw new RuntimeException("生成html静态文件异常", e);
		} catch (TemplateException e) {
			logger.error("生成html静态文件失异常", e);
			throw new RuntimeException("生成html静态文件异常", e);
		}
	}

	/**
	 * 创建html静态页面
	 * 
	 * @param dir
	 *            模板文件所在目录
	 * @param data
	 *            模板数据
	 * @param templateFile
	 *            模板文件
	 * @return
	 */
	public static String crateHTML(final File dir, final Map<String, Object> data, final String templateFile) {

		Configuration configuration = getConfiguration(dir);

		try {
			Template t = configuration.getTemplate(templateFile, "UTF-8");
			return FreeMarkerTemplateUtils.processTemplateIntoString(t, data);
		} catch (IOException e) {
			logger.error("生成html静态文件异常", e);
			throw new RuntimeException("生成html静态文件异常", e);
		} catch (TemplateException e) {
			logger.error("生成html静态文件失异常", e);
			throw new RuntimeException("生成html静态文件异常", e);
		}
	}

	/**
	 * 创建html静态页面,模板目录默认为"/WEB-INF/freemarker/"
	 * 
	 * @param context
	 *            ServletContext
	 * @param data
	 *            模板数据
	 * @param templateFile
	 *            模板文件
	 * @param targetDirectory
	 *            　目标html文件存放目录
	 * @param targetFileName
	 *            　目标html文件名称
	 */
	public static void crateHTML(final ServletContext context, final Map<String, Object> data,
			final String templateFile, final String targetDirectory, final String targetFileName) {
		crateHTML(context, data, DEFAULT_TEMPLATE_DIRECTORY, templateFile, targetDirectory, targetFileName);
	}

	/**
	 * 创建html静态页面
	 * 
	 * 
	 * @param context
	 *            ServletContext
	 * @param data
	 *            模板数据
	 * @param templateDirectory
	 *            模板目录
	 * @param templateFile
	 *            模板文件
	 * @param targetDirectory
	 *            　目标html文件存放目录
	 * @param targetFileName
	 *            　目标html文件名称
	 */
	public static void crateHTML(final ServletContext context, final Map<String, Object> data,
			final String templateDirectory, final String templateFile, final String targetDirectory,
			final String targetFileName) {

		Configuration configuration = getConfiguration(context, templateDirectory);
		crateHTML(configuration, context, data, templateFile, targetDirectory, targetFileName);
	}

	/**
	 * 创建html静态页面
	 * 
	 * @param configuration
	 * @param context
	 *            ServletContext
	 * @param data
	 *            模板数据
	 * @param templateFile
	 *            模板文件
	 * @param targetDirectory
	 *            　目标html文件存放目录
	 * @param targetFileName
	 *            　目标html文件名称
	 */
	public static void crateHTML(final Configuration configuration, final ServletContext context,
			final Map<String, Object> data, final String templateFile, final String targetDirectory,
			final String targetFileName) {

		Writer out = null;
		try {
			Template t = configuration.getTemplate(templateFile, "UTF-8");

			String path = WebUtils.getRealPath(context, targetDirectory);
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File htmlFile = new File(dir, targetFileName + ".html");
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8"));
			// 处理模版
			t.process(data, out);
			out.flush();

		} catch (IOException e) {
			logger.error("生成html静态文件异常", e);
			throw new RuntimeException("生成html静态文件异常", e);
		} catch (TemplateException e) {
			logger.error("生成html静态文件失异常", e);
			throw new RuntimeException("生成html静态文件异常", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 
	 * @param context
	 *            ServletContext
	 * @param templateDirectory
	 *            模板目录
	 * @return
	 */
	private static Configuration getConfiguration(final ServletContext context, final String templateDirectory) {
		Configuration cfg = cfgMap.get(templateDirectory);
		if (cfg == null) {
			cfg = new Configuration();
			cfg.setServletContextForTemplateLoading(context, templateDirectory);
			cfg.setEncoding(Locale.getDefault(), "UTF-8");

			cfgMap.put(templateDirectory, cfg);
		}
		return cfg;
	}

	private static Configuration getConfiguration(final Class<?> clazz, final String pathPrefix) {
		String key = clazz.getName() + pathPrefix;
		Configuration cfg = cfgMap.get(key);
		if (cfg == null) {
			cfg = new Configuration();
			cfg.setClassForTemplateLoading(clazz, pathPrefix);
			cfg.setEncoding(Locale.getDefault(), "UTF-8");

			cfgMap.put(key, cfg);
		}
		return cfg;
	}

	private static Configuration getConfiguration(final File dir) {
		String key = dir.getAbsolutePath();
		Configuration cfg = cfgMap.get(key);
		if (cfg == null) {
			cfg = new Configuration();
			try {
				cfg.setDirectoryForTemplateLoading(dir);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			cfg.setEncoding(Locale.getDefault(), "UTF-8");

			cfgMap.put(key, cfg);
		}
		return cfg;
	}
}
