package com.techstar.modules.mail.config;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
// @ComponentScan(basePackages = "com.techstar.modules.mail")
@PropertySource(value = { "classpath:com/techstar/modules/mail/config/mail.properties", "classpath:mail.properties",
		"classpath:application.properties" }, ignoreResourceNotFound = true)
public class MailConfig {

	private static final Logger logger = LoggerFactory.getLogger(MailConfig.class);

	@Autowired
	private Environment env;

	@Bean
	public JavaMailSender mailSender() {

		JavaMailSenderImpl sender = new JavaMailSenderImpl();

		String property = getProperty("mail.host");
		logger.info("mail.host:{}", property);
		sender.setHost(property);

		property = getProperty("mail.username");
		logger.info("mail.username:{}", property);
		sender.setUsername(property);

		property = getProperty("mail.password");
		logger.info("mail.password:{}", property);
		sender.setPassword(property);

		property = getProperty("mail.transport.protocol");
		if (StringUtils.isNotEmpty(property)) {
			logger.info("mail.transport.protocol:{}", property);
			sender.setProtocol(property);
		}

		Properties properties = new Properties();

		property = getProperty("mail.smtp.auth");
		if (StringUtils.isNotEmpty(property)) {
			logger.info("mail.smtp.auth:{}", property);
			properties.setProperty("mail.smtp.auth", property);
		}

		property = getProperty("mail.smtp.timeout");
		if (StringUtils.isNotEmpty(property)) {
			logger.info("mail.smtp.timeout:{}", property);
			properties.setProperty("mail.smtp.timeout", property);
		}

		property = getProperty("mail.debug");
		if (StringUtils.isNotEmpty(property)) {
			logger.info("mail.debug:{}", property);
			properties.setProperty("mail.debug", property);
		}

		sender.setJavaMailProperties(properties);

		return sender;
	}

	private String getProperty(final String key) {
		return StringUtils.trim(env.getProperty(key));
	}

}
