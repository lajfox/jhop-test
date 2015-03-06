package com.techstar.modules.elfinder.controller;

import javax.servlet.http.HttpServletRequest;

import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsVolume;

public class ConnectorControllerAdapter extends AbstractConnectorController {

	@Override
	protected FsVolume[] getVolumes() {
		return null;
	}

	@Override
	protected void beforeProcess(Param param) {

	}

	@Override
	protected void saveFsService(HttpServletRequest httpServletRequest, FsService fsService) {

	}

}
