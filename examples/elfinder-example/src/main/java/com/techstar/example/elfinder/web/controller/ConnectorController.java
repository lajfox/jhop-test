package com.techstar.example.elfinder.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techstar.modules.elfinder.controller.ConnectorControllerAdapter;
import com.techstar.modules.elfinder.localfs.LocalFsVolume;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsVolume;

@Controller
@RequestMapping("connector")
public class ConnectorController extends ConnectorControllerAdapter {

	@Override
	protected FsVolume[] getVolumes() {
		// 增加用户自有文档配置
		FsVolume[] volumes = new FsVolume[1];
		volumes[0] = new LocalFsVolume("自定义文档", new File("/home/sundoctor/视频"), true, true);
		return volumes;
	}
	
	@Override
	protected void saveFsService(HttpServletRequest httpServletRequest, FsService fsService) {
		httpServletRequest.getSession().setAttribute("AAA", fsService);
	}

}
