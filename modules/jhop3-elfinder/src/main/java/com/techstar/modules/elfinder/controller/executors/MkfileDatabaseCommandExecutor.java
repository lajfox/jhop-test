package com.techstar.modules.elfinder.controller.executors;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.ElfinderChecker;
import com.techstar.modules.elfinder.domain.Param;

@Transactional(readOnly = true)
public class MkfileDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	@Transactional(readOnly = false)
	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {
		String target = param.getTarget();
		String name = param.getName();

		List<ElfinderChecker> checkers = getCheckers();
		Elfinder parent = super.findItem(target);

		if (notPermisson(checkers, parent)) {
			json.put("error", "errPerm");
		} else {

			Elfinder file = new Elfinder();

			file.setParent(parent);
			file.setName(name);
			file.setMtime(new Date());
			file.setMime("text/plain");
			file.setRead(parent.getRead());
			file.setWrite(parent.getWrite());
			file.setLocked(parent.getLocked());
			file.setCategory(parent.getCategory());

			// 设置根目录
			file.setRootDir(parent.getRootDir());

			this.elfinderDao.save(file);

			json.put("added", new Object[] { getElfinderfo(request, file, checkers) });

		}
	}
}
