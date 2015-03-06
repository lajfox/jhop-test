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
public class MkdirDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

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

			Elfinder dir = new Elfinder();
			dir.setParent(parent);
			dir.setName(name);
			dir.setMtime(new Date());
			dir.setMime(DIRECTORY);
			dir.setRead(parent.getRead());
			dir.setWrite(parent.getWrite());
			dir.setLocked(parent.getLocked());
			dir.setWidth(null);
			dir.setCategory(parent.getCategory());

			// 设置根目录
			dir.setRootDir(parent.getRootDir());

			this.elfinderDao.save(dir);

			json.put("added", new Object[] { getElfinderfo(request, dir, checkers) });

		}
	}
}
