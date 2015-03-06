package com.techstar.modules.elfinder.controller.executors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class SizeLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {

	@Override
	protected void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {
		String targets[] = param.getTargets_();

		long size = 0;
		for (String target : targets) {
			FsItemEx fsi = super.findItem(fsService, target);
			size += fsi.getSize();
		}
		json.put("size", size);

	}

}
