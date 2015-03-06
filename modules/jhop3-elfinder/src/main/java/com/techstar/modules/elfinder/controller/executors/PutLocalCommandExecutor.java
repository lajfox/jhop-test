package com.techstar.modules.elfinder.controller.executors;

import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class PutLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {
		String target = param.getTarget();

		FsItemEx fsi = super.findItem(fsService, target);
		OutputStream os = fsi.openOutputStream();
		IOUtils.write(request.getParameter("content"), os, "utf-8");
		IOUtils.closeQuietly(os);
		json.put("changed", new Object[] { super.getFsItemInfo(request, fsi) });
	}
}
