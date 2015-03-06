package com.techstar.modules.mail.service;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;

import com.techstar.modules.mail.domain.Attachment;

/**
 * 发送邮件服务抽象类
 * 
 * @author sundoctor
 * 
 */
public abstract class MailService {

	protected static final Logger logger = LoggerFactory.getLogger(MailService.class);
	@Autowired
	private Environment env;

	public String getDefaultFrom() {
		String from = StringUtils.trim(env.getProperty("mail.default.from"));
		logger.info("mail.default.from:{}", from);
		return from;
	}

	/**
	 * 发送邮件
	 * 
	 * @param simplemailmessage
	 *            　SimpleMailMessage
	 */
	public abstract void send(SimpleMailMessage simplemailmessage);

	/**
	 * 发送邮件
	 * 
	 * @param from
	 *            发件人
	 * @param to
	 *            　收件人
	 * @param subject
	 *            　邮件标题
	 * @param text
	 *            　邮件内容
	 */
	public void send(final String from, final String to, final String subject, final String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(text);
		send(msg);
	}

	/**
	 * 发送邮件, 邮件模板目录默认为"/WEB-INF/freemarker/"
	 * 
	 * @param simplemailmessage
	 *            SimpleMailMessage
	 * @param context
	 *            ServletContext
	 * @param templateName
	 *            邮件模板文件名称
	 * @param model
	 *            邮件模板内容
	 */
	public abstract void send(final SimpleMailMessage simplemailmessage, final ServletContext context,
			final String templateName, final Map<String, Object> model);

	/**
	 * 发送邮件
	 * 
	 * @param simplemailmessage
	 *            SimpleMailMessage
	 * @param context
	 *            ServletContext
	 * @param templatedirectory
	 *            邮件模板目录
	 * @param templateName
	 *            邮件模板文件名称
	 * @param model
	 *            邮件内容
	 */
	public abstract void send(final SimpleMailMessage msg, final ServletContext context,
			final String templatedirectory, final String templateName, final Map<String, Object> model);

	/**
	 * 发送邮件, 邮件模板目录默认为"/WEB-INF/freemarker/"
	 * 
	 * @param simplemailmessage
	 *            SimpleMailMessage
	 * @param context
	 *            ServletContext
	 * @param templateName
	 *            邮件模板文件名称
	 * @param model
	 *            邮件模板内容
	 * @param attachments
	 *            邮件附件
	 */
	public abstract void send(final SimpleMailMessage simplemailmessage, final ServletContext context,
			final String templateName, final Map<String, Object> model, final Attachment... attachments);

	/**
	 * 发送邮件
	 * 
	 * @param simplemailmessage
	 *            SimpleMailMessage
	 * @param context
	 *            ServletContext
	 * @param templatedirectory
	 *            邮件模板目录
	 * @param templateName
	 *            邮件模板文件名称
	 * @param model
	 *            邮件模板内容
	 * @param attachments
	 *            邮件附件
	 */
	public abstract void send(final SimpleMailMessage simplemailmessage, final ServletContext context,
			final String templatedirectory, final String templateName, final Map<String, Object> model,
			final Attachment... attachments);

	/**
	 * 发送邮件,邮件模板目录默认为"/WEB-INF/freemarker/"
	 * 
	 * @param simplemailmessage
	 *            SimpleMailMessage
	 * @param context
	 *            ServletContext
	 * @param templateName
	 *            邮件模板文件名称
	 * @param model
	 *            邮件模板内容
	 * @param files
	 *            邮件附件
	 */
	public abstract void send(final SimpleMailMessage simplemailmessage, final ServletContext context,
			final String templateName, final Map<String, Object> model, final File... files);

	/**
	 * 发送邮件
	 * 
	 * @param simplemailmessage
	 *            SimpleMailMessage
	 * @param context
	 *            ServletContext
	 * @param templatedirectory
	 *            邮件模板目录
	 * @param templateName
	 *            邮件模板文件名称
	 * @param model
	 *            邮件模板内容
	 * @param files
	 *            邮件附件
	 */
	public abstract void send(final SimpleMailMessage simplemailmessage, final ServletContext context,
			final String templatedirectory, final String templateName, final Map<String, Object> model,
			final File... files);

	/**
	 * 发送邮件
	 * 
	 * @param simplemailmessage
	 *            SimpleMailMessage
	 * @param clazz
	 *            邮件模板类
	 * @param pathPrefix
	 *            邮件模板类路径前缀
	 * @param templateName
	 *            邮件模板文件名称
	 * @param model
	 *            邮件模板内容
	 */
	public abstract void send(final SimpleMailMessage simplemailmessage, final Class<?> clazz, final String pathPrefix,
			final String templateName, final Map<String, Object> model);

	/**
	 * 发送邮件
	 * 
	 * @param simplemailmessage
	 *            SimpleMailMessage
	 * @param clazz
	 *            邮件模板类
	 * @param pathPrefix
	 *            邮件模板类路径前缀
	 * @param templateName
	 *            邮件模板文件名称
	 * @param model
	 *            邮件模板内容
	 * @param attachments
	 *            邮件附件
	 */
	public abstract void send(final SimpleMailMessage simplemailmessage, final Class<?> clazz, final String pathPrefix,
			final String templateName, final Map<String, Object> model, final Attachment... attachments);

	/**
	 * 发送邮件
	 * 
	 * @param simplemailmessage
	 *            SimpleMailMessage
	 * @param clazz
	 *            邮件模板类
	 * @param pathPrefix
	 *            邮件模板类路径前缀
	 * @param templateName
	 *            邮件模板文件名称
	 * @param model
	 *            邮件模板内容
	 * @param files
	 *            邮件附件
	 */
	public abstract void send(final SimpleMailMessage simplemailmessage, final Class<?> clazz, final String pathPrefix,
			final String templateName, final Map<String, Object> model, final File... files);

}
