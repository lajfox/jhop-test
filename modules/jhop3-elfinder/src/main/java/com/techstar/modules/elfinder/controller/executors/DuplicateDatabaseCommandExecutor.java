package com.techstar.modules.elfinder.controller.executors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

@Transactional(readOnly = true)
public class DuplicateDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	@Override
	@Transactional(readOnly = false)
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {
		String[] targets = param.getTargets_();

		List<Elfinder> added = new ArrayList<Elfinder>();

		Elfinder src = null, dst = null;
		String name = null, baseName = null, extension = null, newName = null;
		for (String target : targets) {
			src = super.findItem(target);
			name = src.getName();
			baseName = FilenameUtils.getBaseName(name).replaceAll("\\(\\d+\\)$", "");
			extension = FilenameUtils.getExtension(name);

			int i = 1;
			while (true) {
				newName = String.format("%s(%d)%s", baseName, i,
						(StringUtils.isEmpty(extension) ? "" : "." + extension));
				if (!this.elfinderDao.exists("parentAndName", src.getParent(), newName)) {
					break;
				}
				i++;
			}

			dst = copy(src, src.getParent());
			dst.setName(newName);

			this.elfinderDao.save(dst);

			added.add(dst);
		}

		json.put("added", elfinder2JsonArray(request, added, getCheckers()));
	}

}
