package com.techstar.modules.elfinder.controller.executors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

@Transactional(readOnly = true)
public class ParentsDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {
		String target = param.getTarget();

		List<Elfinder> files = new ArrayList<Elfinder>();
		if (StringUtils.isNotEmpty(target)) {
			Elfinder fsi = findItem(target);

			while (fsi != null && fsi.hasParent()) {
				super.addSubfolders(files, fsi);
				fsi = fsi.getParent();
			}
		}

		
		json.put("tree", elfinder2JsonArray(request, files,getCheckers()));
	}
}
