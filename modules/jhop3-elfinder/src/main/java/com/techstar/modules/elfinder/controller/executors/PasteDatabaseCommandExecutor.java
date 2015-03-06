package com.techstar.modules.elfinder.controller.executors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.ElfinderChecker;
import com.techstar.modules.elfinder.domain.Param;

@Transactional(readOnly = true)
public class PasteDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	@Transactional(readOnly = false)
	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {
		String[] targets = param.getTargets_();

		boolean cut = BooleanUtils.toBoolean(param.getCut());

		List<Elfinder> added = new ArrayList<Elfinder>();
		List<String> removed = new ArrayList<String>();

		List<ElfinderChecker> checkers = getCheckers();
		Elfinder parent = super.findItem(param.getDst());

		if (notPermisson(checkers, parent)) {
			param.setTargets_(null);
			json.put("error", "errPerm");
			return;
		}

		Elfinder src = null, dst = null;
		for (String target : targets) {

			src = super.findItem(target);

			if (notPermisson(checkers, src)) {
				param.setTargets_(null);
				json.put("error", "errPerm");
				break;
			}

			dst = copy(src, parent);
			added.add(dst);

			if (cut) {
				removed.add(src.getId());
			}

		}

		if (CollectionUtils.isNotEmpty(added)) {
			this.elfinderDao.save(added);
		}
		if (CollectionUtils.isNotEmpty(removed)) {
			this.elfinderDao.delete(removed);
		}

		json.put("added", elfinder2JsonArray(request, added, checkers));
		json.put("removed", removed);
	}

}
