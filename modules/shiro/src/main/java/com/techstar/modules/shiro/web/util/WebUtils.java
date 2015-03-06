package com.techstar.modules.shiro.web.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WebUtils extends org.apache.shiro.web.util.WebUtils {

	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

	public static boolean isAjax(final ServletRequest request, final String ajax) {
		HttpServletRequest req = WebUtils.toHttp(request);

		boolean isajax = BooleanUtils.toBoolean(request.getParameter(ajax));
		String xrequestedwith = req.getHeader("X-Requested-With");

		logger.info("isajax:{}", isajax);
		logger.info("xrequestedwith:{}", xrequestedwith);

		return isajax || StringUtils.contains(xrequestedwith, "XMLHttpRequest");
	}
}
