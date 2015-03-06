package com.techstar.modules.elfinder.controller.executors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.ElfinderChecker;
import com.techstar.modules.elfinder.domain.Param;

public class OpenDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {

		boolean init = BooleanUtils.toBoolean(param.getInit());
		boolean tree = BooleanUtils.toBoolean(param.getTree());
		String target = param.getTarget();

		String[] categories = StringUtils.split(request.getParameter("category"), ",");

		List<Elfinder> list = new ArrayList<Elfinder>();
		if (init) {
			json.put("api", 2.1);
			json.put("netDrivers", new Object[0]);
		}

		List<ElfinderChecker> checkers = getCheckers();

		List<Elfinder> roots = null;
		if (tree) {
			roots = findRoots(checkers, categories);
			if (CollectionUtils.isNotEmpty(roots)) {
				list.addAll(roots);
				addSubfolders(list, roots);
			}
		}

		if (StringUtils.isNotEmpty(target) && ArrayUtils.isNotEmpty(categories) && !exists(target, categories)) {
			target = null;
		}

		Elfinder cwd = null;
		if (StringUtils.isEmpty(target) && CollectionUtils.isNotEmpty(roots)) {
			cwd = roots.get(0);
		} else {
			cwd = findCwd(target, checkers, categories);
		}

		if (cwd != null) {
			if (!list.contains(cwd)) {
				list.add(cwd);
			}
			addChildren(list, cwd);
		}

		json.put("files", elfinder2JsonArray(request, list, checkers));
		json.put("cwd", getElfinderfo(request, cwd, checkers));
		json.put("options", getOptions(request, cwd));

	}

	private boolean exists(final String target, final String[] categories) {
		if (ArrayUtils.isEmpty(categories)) {
			return this.elfinderDao.exists("id", target);
		} else if (categories.length == 1) {
			return this.elfinderDao.exists("idAndCategory", target, categories[0]);
		} else {
			return this.elfinderDao.exists("idAndCategoryIn", target, Arrays.asList(categories));
		}
	}

}
