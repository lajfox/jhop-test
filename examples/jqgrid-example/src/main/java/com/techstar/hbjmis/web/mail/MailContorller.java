package com.techstar.hbjmis.web.mail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.techstar.modules.mail.domain.Attachment;
import com.techstar.modules.mail.domain.ResourceAttachment;
import com.techstar.modules.mail.domain.StreamAttachment;
import com.techstar.modules.mail.service.MailService;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.security.entity.User;
import com.techstar.security.service.UserService;

@Controller
@RequestMapping(value = "/mail")
public class MailContorller implements ServletContextAware {

	@Autowired
	private MailService mailService;

	@Autowired
	private UserService userService;

	private ServletContext servletContext;

	public void setServletContext(ServletContext sc) {
		this.servletContext = sc;
	}

	/**
	 * 发送简单文本邮件
	 * 
	 * @return
	 */
	@RequestMapping(value = "send1", method = RequestMethod.GET)
	public String form() {
		return "mail/send1";
	}

	@RequestMapping(value = "send1", method = RequestMethod.POST)
	public @ResponseBody
	Results send(SimpleMailMessage msg) {
		msg.setFrom(mailService.getDefaultFrom());
		mailService.send(msg);

		return new Results(true, "发送邮件成功");
	}

	/**
	 * 发送html邮件并带附件
	 * 
	 * @return
	 */
	@RequestMapping(value = "send2", method = RequestMethod.GET)
	public String form2() {
		return "mail/send2";
	}

	@RequestMapping(value = "send2", method = RequestMethod.POST)
	public @ResponseBody
	Results send2(SimpleMailMessage msg, @RequestParam("attachment_file") CommonsMultipartFile attachmentFile)
			throws IOException {

		User user = userService.findOne(SubjectUtils.getPrincipal(ShiroUser.class).getId());
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("user", user);

		// 附件1　来自系统
		Resource resource1 = new ClassPathResource("mail/mail.properties");
		Attachment attachment1 = new ResourceAttachment("mail.properties", resource1);

		// 附件2　来自网页上传
		Attachment attachment2 = new StreamAttachment(attachmentFile.getOriginalFilename(),
				attachmentFile.getInputStream());

		// 附件3　来自系统
		Resource resource3 = new ServletContextResource(servletContext, "/api/signavio-svg.js");
		Attachment attachment3 = new ResourceAttachment("signavio-svg.js", resource3);

		// 附件4　来自系统
		resource3 = new ServletContextResource(servletContext, "/editor/oryx.js");
		Attachment attachment4 = new ResourceAttachment("oryxkk看看  .js", resource3);

		mailService.send(msg, servletContext, "NotifyUser.ftl", model, attachment1, attachment2, attachment3,
				attachment4);

		// 附件5　来自系统
		// File file1 = new
		// File(servletContext.getRealPath("/editor/build.xml"));
		// 附件6　来自系统
		// File file2 = new File(servletContext.getRealPath("/libs"),
		// "utils.js");
		// mailService.send(msg, servletContext, "NotifyUser.ftl", model, file1,
		// file2);

		return new Results(true, "发送邮件成功");
	}

}
