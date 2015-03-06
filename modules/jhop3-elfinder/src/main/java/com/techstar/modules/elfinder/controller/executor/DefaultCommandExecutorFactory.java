package com.techstar.modules.elfinder.controller.executor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultCommandExecutorFactory implements CommandExecutorFactory, ApplicationContextAware {
	String _classNamePattern;
	private ApplicationContext applicationContext;

	@Override
	public CommandExecutor get(String commandName) {
		String className = String.format(_classNamePattern, commandName);
		return this.applicationContext.getBean(className, CommandExecutor.class);
	}

	public String getClassNamePattern() {
		return _classNamePattern;
	}

	public void setClassNamePattern(String classNamePattern) {
		_classNamePattern = classNamePattern;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
