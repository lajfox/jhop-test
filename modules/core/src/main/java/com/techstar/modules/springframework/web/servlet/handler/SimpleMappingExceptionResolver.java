package com.techstar.modules.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.techstar.modules.springframework.data.jpa.domain.Results;

public class SimpleMappingExceptionResolver extends
		org.springframework.web.servlet.handler.SimpleMappingExceptionResolver {

	protected static final Logger logger = LoggerFactory.getLogger(SimpleMappingExceptionResolver.class);
	private static final String ISAJAX = "_ajax";
	private String ajax = ISAJAX;
	private static final String XREQUESTEDWITH = "X-Requested-With";
	private static final String XMLHTTPREQUEST = "XMLHttpRequest";

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		// 判断是否AJAX请求
		if (isAjax(request)) {
			logger.error("error:", ex);

			Results results = exception2Results(ex);
			ErrorConext.set(results);
			return new ModelAndView("forward:/static/errors");
		} else {
			return super.doResolveException(request, response, handler, ex);
		}
	}

	protected Results exception2Results(Exception ex) {

		int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		String message = "服务器内部错误";

		if (ex instanceof BindException) {
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
			message = "用户输入错误";
		}
		return new Results(false, message, statusCode);
	}

	private boolean isAjax(HttpServletRequest request) {
		boolean isajax = BooleanUtils.toBoolean(request.getParameter(ajax));
		String xrequestedwith = request.getHeader(XREQUESTEDWITH);
		return isajax || StringUtils.contains(xrequestedwith, XMLHTTPREQUEST);
	}

}
