package com.techstar.modules.flexpaper.jodconverter.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@WebListener
public class WebappContextListener implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(WebappContextListener.class);

	public void contextInitialized(ServletContextEvent event) {
		logger.info("jodconverter WebappContextListener init");
		WebappContext.init(event.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent event) {
		logger.info("jodconverter WebappContextListener destroy");
		WebappContext.destroy(event.getServletContext());
	}

}
