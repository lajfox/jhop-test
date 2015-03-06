package com.techstar.modules.elfinder.controller.executor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public interface CommandExecutionContext {

	FsService getFileService();

	HttpServletRequest getRequest();

	HttpServletResponse getResponse();

	ServletContext getServletContext();

	Param getParam();

}
