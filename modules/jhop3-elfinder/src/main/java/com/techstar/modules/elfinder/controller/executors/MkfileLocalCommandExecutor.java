package com.techstar.modules.elfinder.controller.executors;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class MkfileLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {
		String target = param.getTarget();
		String name = param.getName();

		FsItemEx fsi = super.findItem(fsService, target);

		if (fsi.isLocked()) {
			json.put("error", "errPerm");
		} else {
			FsItemEx file = new FsItemEx(fsi, name);
			try {
				file.createFile();
				json.put("added", new Object[] { getFsItemInfo(request, file) });
			} catch (IOException e) {
				logger.error("不能创建文件:{},excpetion:{}",name,e);
				if (file.exists()) {
					file.deleteFile();
				}
				json.put("error", new String[] { "errMkfile", name });
			}
		}
	}
}
