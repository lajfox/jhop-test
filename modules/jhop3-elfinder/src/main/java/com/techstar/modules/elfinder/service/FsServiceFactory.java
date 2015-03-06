package com.techstar.modules.elfinder.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public interface FsServiceFactory
{

	FsService getFileService(HttpServletRequest request, ServletContext servletContext);

}
