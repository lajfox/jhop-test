package com.techstar.modules.elfinder.controller.executors;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

public class LsDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {
		String target = param.getTarget();
		List<Elfinder> childrens = this.findChildrens(target);
		
		
		json.put("list", elfinder2JsonArray(request, childrens,getCheckers()));
	}
}
