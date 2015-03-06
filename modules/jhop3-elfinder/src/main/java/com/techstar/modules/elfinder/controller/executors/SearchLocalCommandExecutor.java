package com.techstar.modules.elfinder.controller.executors;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class SearchLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {

		String q = param.getQ();
		String target = param.getTarget();
		FsItemEx fsi = super.findItem(fsService, target);
		
		Map<String, FsItemEx> files = new LinkedHashMap<String, FsItemEx>();
		search(files, fsi, q);		

		
		
		json.put("files", files2JsonArray(request, files.values()));
		
	}
}
