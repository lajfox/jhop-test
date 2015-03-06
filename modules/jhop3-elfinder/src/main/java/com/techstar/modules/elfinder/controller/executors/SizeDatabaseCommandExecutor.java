package com.techstar.modules.elfinder.controller.executors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Param;

public class SizeDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	@Override
	protected void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {
		String targets[] = param.getTargets_();

		json.put("size", this.elfinderDao.size(targets));

	}

}
