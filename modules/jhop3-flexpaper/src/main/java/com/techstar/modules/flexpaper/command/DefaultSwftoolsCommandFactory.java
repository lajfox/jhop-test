package com.techstar.modules.flexpaper.command;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultSwftoolsCommandFactory implements SwftoolsCommandFactory, ApplicationContextAware {

	private ApplicationContext applicationContext;

	public SwftoolsCommand get(final String format) {
		return applicationContext.getBean((StringUtils.isEmpty(format) ? "swf" : format) + "Commond", SwftoolsCommand.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}
}
