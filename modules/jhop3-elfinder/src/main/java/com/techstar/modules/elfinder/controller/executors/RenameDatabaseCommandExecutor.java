package com.techstar.modules.elfinder.controller.executors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

@Transactional(readOnly = true)
public class RenameDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	@Transactional(readOnly = false)
	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {

		String target = param.getTarget();
		String name = param.getName();

		Elfinder fsi = super.findItem(target);
		fsi.setName(name);

		this.elfinderDao.save(fsi);


		json.put("added", new Object[] { getElfinderfo(request, fsi,getCheckers()) });
		json.put("removed", new String[] { target });
	}
}
