package com.techstar.modules.elfinder.controller.executors;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

public class SearchDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {

		String q = param.getQ();
		String target = param.getTarget();

		List<Elfinder> files = this.elfinderDao.findBy("parentIdAndNameLike", target, q);

		
		json.put("files", elfinder2JsonArray(request, files,getCheckers()));

	}
}
