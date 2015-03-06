package com.techstar.modules.elfinder.controller.executors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class PasteLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {
		String[] targets = param.getTargets_();
		// String src = param.getSrc();
		String dst = param.getDst();
		boolean cut = BooleanUtils.toBoolean(param.getCut());

		List<FsItemEx> added = new ArrayList<FsItemEx>();
		List<String> removed = new ArrayList<String>();

		// FsItemEx fsrc = super.findItem(fsService, src);
		FsItemEx fdst = super.findItem(fsService, dst);

		for (String target : targets) {

			FsItemEx ftgt = super.findItem(fsService, target);
			if (!ftgt.isWritable() || ftgt.isLocked()) {
				param.setTargets_(null);
				json.put("error", "errPerm");
				break;
			}

			String name = ftgt.getName();
			FsItemEx newFile = new FsItemEx(fdst, name);
			super.createAndCopy(ftgt, newFile);
			added.add(newFile);

			if (cut) {
				ftgt.delete();
				removed.add(ftgt.getHash());
			}
		}

		json.put("added", files2JsonArray(request, added));
		json.put("removed", removed);
	}
}
