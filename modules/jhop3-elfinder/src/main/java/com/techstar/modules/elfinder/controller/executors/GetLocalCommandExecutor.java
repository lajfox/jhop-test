package com.techstar.modules.elfinder.controller.executors;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class GetLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {
		String target = param.getTarget();
		InputStream is = null;
		FsItemEx fsi = super.findItem(fsService, target);
		try {
			is = fsi.openInputStream();
			String content = IOUtils.toString(is, "utf-8");
			json.put("content", content);
		} catch (IOException e) {
			logger.error("无法打开:{},exception:{}", fsi.getName(), e);
			json.put("error", new String[] { "errOpen", fsi.getName() });
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
}
