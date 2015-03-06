package com.techstar.modules.shiro.web.servlet.handler;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;

import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class ShiroMappingExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected Results exception2Results(Exception ex) {

		if (ex instanceof UnauthorizedException) {
			return new Results(false, "权限不足", HttpServletResponse.SC_FORBIDDEN);
		} else {
			return super.exception2Results(ex);
		}
	}

}
