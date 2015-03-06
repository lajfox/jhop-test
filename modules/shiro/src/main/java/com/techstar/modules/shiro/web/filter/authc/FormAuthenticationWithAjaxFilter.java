package com.techstar.modules.shiro.web.filter.authc;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.shiro.web.util.WebUtils;
import com.techstar.modules.springframework.data.jpa.domain.Results;

/**
 * 自己实现的formAuthcFilter，加入Ajax登陆
 * 
 * @author sundoctor
 * 
 */

public class FormAuthenticationWithAjaxFilter extends FormAuthenticationWithVerifyCodeFilter {

	private static final Logger logger = LoggerFactory.getLogger(FormAuthenticationWithAjaxFilter.class);
	private static final JsonMapper MAPPER = new JsonMapper();

	private static final String ISAJAX = "_ajax";
	private String ajax = ISAJAX;

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		if (WebUtils.isAjax(request, ajax)) {
			logger.info("AJAX 登录成功");

			Results results = new Results(true, "登录成功", HttpServletResponse.SC_OK);

			HttpServletResponse res = WebUtils.toHttp(response);
			res.setContentType("application/json;charset=utf-8");
			Writer writer = res.getWriter();
			writer.write(MAPPER.toJson(results));
			IOUtils.closeQuietly(writer);

		} else {
			issueSuccessRedirect(request, response);
		}
		// we handled the success redirect directly, prevent the chain from continuing:
		return false;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {

		if (WebUtils.isAjax(request, ajax)) {
			logger.info("AJAX 登录失败");

			Results results = new Results(false);
			results.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			setFailureAttribute(results, e);

			HttpServletResponse res = WebUtils.toHttp(response);
			res.setContentType("application/json;charset=utf-8");
			try {
				Writer writer = res.getWriter();
				writer.write(MAPPER.toJson(results));
				IOUtils.closeQuietly(writer);
			} catch (IOException e1) {
				logger.error("AJAX 登录失败，返回异常", e);
			}

		} else {
			setFailureAttribute(request, e);
			// login failed, let request continue back to the login page:
		}
		return true;
	}

	protected void setFailureAttribute(final Results results, AuthenticationException ae) {
		String className = ae.getClass().getName();
		String message = proterties.getProperty(className);
		results.setMessage(message);
	}

}
