package com.techstar.modules.flexpaper.command;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.techstar.modules.flexpaper.domain.QueryParam;

public interface CommandContext {	

	HttpServletRequest getRequest();

	HttpServletResponse getResponse();

	ServletContext getServletContext();	
	
	QueryParam getQueryParam();

}
