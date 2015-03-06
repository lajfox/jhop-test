package com.techstar.modules.shiro.web.filter.authc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techstar.modules.shiro.web.util.WebUtils;


/**
 * 自定义UserFilter，增加AJAX验证功能
 * @author sundoctor
 *
 */
public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {

	private static final Logger logger = LoggerFactory.getLogger(UserFilter.class);
	private static final String ISAJAX = "_ajax";
	private String ajax = ISAJAX;

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if (WebUtils.isAjax(request, this.ajax)) {
			logger.info("Ajax request");
			HttpServletResponse res = WebUtils.toHttp(response);
			res.setContentType("text/html;charset=utf-8");
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "登录超时");
		} else {
			logger.info("No Ajax request");
			saveRequestAndRedirectToLogin(request, response);
		}
		return false;
	}

}
