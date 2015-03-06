package com.techstar.modules.elfinder.compress;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultCompressCommandFactory implements CompressCommandFactory, ApplicationContextAware {

	private ApplicationContext applicationContext;

	private static final Pattern FROMPATTERN = Pattern.compile("(application/)(x-rar|x-zip|x-tar|x-gzip|x-7z|x-bzip2)",
			Pattern.CASE_INSENSITIVE);

	private static final Map<String, String> FORMATMAP = new HashMap<String, String>();

	@Override
	public CompressCommand get(String type) {
		String className = getClassName(type);
		return this.applicationContext.getBean(className, CompressCommand.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private String getClassName(final String type) {

		String className = FORMATMAP.get(type);
		if (StringUtils.isEmpty(className)) {
			Matcher matcher = FROMPATTERN.matcher(type);
			while (matcher.find()) {
				className = matcher.group(2).replace("x-", "");
				className = StringUtils.equals("7z", className) ? "sevenZCompressCommand" : className
						+ "CompressCommand";
				FORMATMAP.put(type, className);
				return className;
			}
		}

		return className;
	}
}
