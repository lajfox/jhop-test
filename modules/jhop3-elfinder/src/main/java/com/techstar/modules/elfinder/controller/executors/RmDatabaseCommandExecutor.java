package com.techstar.modules.elfinder.controller.executors;

import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Param;

@Transactional(readOnly = true)
public class RmDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	@Override
	@Transactional(readOnly = false)
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {
		String[] targets = param.getTargets_();

		this.elfinderDao.delete(Arrays.asList(targets));

		json.put("removed", targets);
	}
}
