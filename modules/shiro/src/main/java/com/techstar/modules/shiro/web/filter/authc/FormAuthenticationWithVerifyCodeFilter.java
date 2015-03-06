package com.techstar.modules.shiro.web.filter.authc;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.techstar.modules.shiro.authc.VerifyCodeException;

/**
 * 自己实现的formAuthcFilter，加入登陆时验证码校验
 * 
 * @author sundoctor
 * 
 */

public class FormAuthenticationWithVerifyCodeFilter extends FormAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(FormAuthenticationWithVerifyCodeFilter.class);

	private ImageCaptchaService imageCaptchaService;// 验证码服务类
	private String verifyCode = "verifyCode";// 校验码参数名称
	private boolean verify = true;// 是否校验
	protected Properties proterties;// 异常本地化信息

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}

	public boolean getVerify() {
		return this.verify;
	}

	public void setImageCaptchaService(ImageCaptchaService imageCaptchaService) {
		this.imageCaptchaService = imageCaptchaService;
	}

	/**
	 * 初始化加载登录异常本地化信息
	 * 
	 * @throws IOException
	 */
	@PostConstruct
	public void init() throws IOException {
		Resource resource = new ClassPathResource("exception.properties", this.getClass());
		proterties = PropertiesLoaderUtils.loadProperties(resource);
	}

	/**
	 * 登录时增加验证码校验
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

		AuthenticationToken token = createToken(request, response);
		if (token == null) {
			String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
					+ "must be created in order to execute a login attempt.";
			throw new IllegalStateException(msg);
		}

		if (verifyCode(request, token)) {
			return super.executeLogin(request, response);
		} else {
			return onLoginFailure(token, new VerifyCodeException("验证码错误"), request, response);
		}
	}

	@Override
	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		String className = ae.getClass().getName();
		String message = proterties.getProperty(className);
		request.setAttribute(getFailureKeyAttribute(), StringUtils.isEmpty(message) ? "登录失败，帐号或者密码错误" : message);
	}

	/**
	 * 验证码校验
	 * 
	 * @param request
	 * @param token
	 * @return
	 */
	protected boolean verifyCode(ServletRequest request, AuthenticationToken token) {

		boolean responseCorrect = true;
		if (verify) {
			Assert.notNull(imageCaptchaService);

			HttpServletRequest req = WebUtils.toHttp(request);
			String captchaId = req.getSession().getId();
			String captcha = req.getParameter(verifyCode);
			try {
				responseCorrect = imageCaptchaService.validateResponseForID(captchaId, captcha);
			} catch (CaptchaServiceException e) {
				logger.error("验证码错误", e);
				responseCorrect = false;
			}
		}

		return responseCorrect;
	}

	@Override
	protected String getPassword(ServletRequest request) {
		String password = request.getParameter(getPasswordParam());
		return StringUtils.isEmpty(password) ? null : password;
	}

	@Override
	protected String getUsername(ServletRequest request) {
		String username = request.getParameter(this.getUsernameParam());
		return StringUtils.isEmpty(username) ? null : username;
	}
}
