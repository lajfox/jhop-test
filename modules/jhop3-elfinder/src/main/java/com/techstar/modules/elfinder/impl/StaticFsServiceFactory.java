package com.techstar.modules.elfinder.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsServiceFactory;


public class StaticFsServiceFactory implements FsServiceFactory
{
	FsService _fsService;

	@Override
	public FsService getFileService(HttpServletRequest request, ServletContext servletContext)
	{
		return _fsService;
	}

	public FsService getFsService()
	{
		return _fsService;
	}

	public void setFsService(FsService fsService)
	{
		_fsService = fsService;
	}
}
