package com.techstar.modules.mail.service;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.techstar.modules.mail.domain.Attachment;
import com.techstar.modules.mail.javamail.MimeMessageHelper;
import com.techstar.modules.utils.FreeMarkerUtils;

/**
 * 发送邮件服务类
 * 
 * @author sundoctor
 * 
 */
@Component("mailService")
public class DefaultMailService extends MailService {

	@Autowired
	protected JavaMailSender mailSender;
	private static final String[] EMPYTCC = new String[0];

	@Override
	public void send(SimpleMailMessage msg) {
		try {
			mailSender.send(msg);
		} catch (MailException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void send(final SimpleMailMessage msg, final ServletContext context, final String templateName,
			final Map<String, Object> model) {
		send(msg, context, FreeMarkerUtils.DEFAULT_TEMPLATE_DIRECTORY, templateName, model, (File) null);
	}

	@Override
	public void send(final SimpleMailMessage msg, final ServletContext context, final String templatedirectory,
			final String templateName, final Map<String, Object> model) {
		send(msg, context, templatedirectory, templateName, model, (File) null);
	}

	@Override
	public void send(final SimpleMailMessage msg, final ServletContext context, final String templateName,
			final Map<String, Object> model, final File... files) {
		send(msg, context, FreeMarkerUtils.DEFAULT_TEMPLATE_DIRECTORY, templateName, model, files);
	}

	@Override
	public void send(final SimpleMailMessage msg, final ServletContext context, final String templatedirectory,
			final String templateName, final Map<String, Object> model, final File... files) {
		String content = FreeMarkerUtils.crateHTML(context, model, templatedirectory, templateName);
		try {
			MimeMessage mimeMsg = mailSender.createMimeMessage();
			MimeMessageHelper helper = createMimeMessageHelper(mimeMsg, msg, content);

			if (files != null) {
				for (File file : files) {
					if (file != null) {
						helper.addAttachment(file.getName(), file);
					}
				}
			}

			mailSender.send(mimeMsg);
		} catch (MessagingException ex) {
			logger.error(ex.getMessage(), ex);
		}

	}

	@Override
	public void send(final SimpleMailMessage msg, final ServletContext context, final String templateName,
			final Map<String, Object> model, final Attachment... attachments) {
		send(msg, context, FreeMarkerUtils.DEFAULT_TEMPLATE_DIRECTORY, templateName, model, attachments);
	}

	@Override
	public void send(final SimpleMailMessage msg, final ServletContext context, final String templatedirectory,
			final String templateName, final Map<String, Object> model, final Attachment... attachments) {
		String content = FreeMarkerUtils.crateHTML(context, model, templatedirectory, templateName);
		try {
			MimeMessage mimeMsg = mailSender.createMimeMessage();
			MimeMessageHelper helper = createMimeMessageHelper(mimeMsg, msg, content);

			if (attachments != null) {
				for (Attachment attachment : attachments) {
					if (attachment != null && attachment.getInputStream() != null) {
						helper.addAttachment(attachment.getFileName(), attachment.getInputStream());
					}
				}
			}

			mailSender.send(mimeMsg);
		} catch (MessagingException ex) {
			logger.error(ex.getMessage(), ex);
		}

	}

	@Override
	public void send(final SimpleMailMessage msg, final Class<?> clazz, final String pathPrefix,
			final String templateName, final Map<String, Object> model) {
		send(msg, clazz, pathPrefix, templateName, model, (File) null);
	}

	@Override
	public void send(final SimpleMailMessage msg, final Class<?> clazz, final String pathPrefix,
			final String templateName, final Map<String, Object> model, final File... files) {
		String content = FreeMarkerUtils.crateHTML(clazz, model, pathPrefix, templateName);
		try {
			MimeMessage mimeMsg = mailSender.createMimeMessage();
			MimeMessageHelper helper = createMimeMessageHelper(mimeMsg, msg, content);

			if (files != null) {
				for (File file : files) {
					if (file != null) {
						helper.addAttachment(file.getName(), file);
					}
				}
			}

			mailSender.send(mimeMsg);
		} catch (MessagingException ex) {
			logger.error(ex.getMessage(), ex);
		}

	}

	@Override
	public void send(final SimpleMailMessage msg, final Class<?> clazz, final String pathPrefix,
			final String templateName, final Map<String, Object> model, final Attachment... attachments) {
		String content = FreeMarkerUtils.crateHTML(clazz, model, pathPrefix, templateName);
		try {
			MimeMessage mimeMsg = mailSender.createMimeMessage();
			MimeMessageHelper helper = createMimeMessageHelper(mimeMsg, msg, content);

			if (attachments != null) {
				for (Attachment attachment : attachments) {
					if (attachment != null && attachment.getInputStream() != null) {
						helper.addAttachment(attachment.getFileName(), attachment.getInputStream());
					}
				}
			}

			mailSender.send(mimeMsg);
		} catch (MessagingException ex) {
			logger.error(ex.getMessage(), ex);
		}

	}

	private MimeMessageHelper createMimeMessageHelper(final MimeMessage mimeMsg, final SimpleMailMessage msg,
			final String content) throws MessagingException {

		MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true, "utf-8");
		helper.setTo(msg.getTo());
		helper.setCc(msg.getCc() == null ? EMPYTCC: msg.getCc() );
		helper.setBcc(msg.getBcc() == null ? EMPYTCC : msg.getBcc() );

		if (StringUtils.isNotEmpty(msg.getReplyTo())) {
			helper.setReplyTo(msg.getReplyTo());
		}

		if (StringUtils.isNotEmpty(msg.getFrom()) || StringUtils.isNotEmpty(getDefaultFrom())) {
			helper.setFrom(StringUtils.isEmpty(msg.getFrom()) ? this.getDefaultFrom() : msg.getFrom());
		}

		helper.setSubject(msg.getSubject());
		helper.setText(content, true);

		return helper;
	}
}
