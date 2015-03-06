package com.techstar.modules.elfinder.controller.executors;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsVolume;

public class OpenLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {

	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {

		boolean init = BooleanUtils.toBoolean(param.getInit());
		boolean tree = BooleanUtils.toBoolean(param.getTree());
		String target = param.getTarget();

		Map<String, FsItemEx> files = new LinkedHashMap<String, FsItemEx>();
		if (init) {
			json.put("api", 2.1);
			json.put("netDrivers", new Object[0]);
		}

		if (tree) {
			for (FsVolume v : fsService.getVolumes()) {
				FsItemEx root = new FsItemEx(v.getRoot(), fsService);
				files.put(root.getHash(), root);
				addSubfolders(files, root);
			}
		}

		FsItemEx cwd = findCwd(fsService, target);
		files.put(cwd.getHash(), cwd);
		addChildren(files, cwd);

		json.put("files", files2JsonArray(request, files.values()));
		json.put("cwd", getFsItemInfo(request, cwd));
		json.put("options", getOptions(request, cwd, fsService.getServiceConfig().getArchivers()));

	}
}
