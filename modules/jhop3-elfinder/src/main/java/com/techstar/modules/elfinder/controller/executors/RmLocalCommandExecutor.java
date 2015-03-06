package com.techstar.modules.elfinder.controller.executors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;


public class RmLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor
{
	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception
	{
		String[] targets = param.getTargets_();
		
		List<String> removed = new ArrayList<String>();

		for (String target : targets)
		{
			FsItemEx ftgt = super.findItem(fsService, target);
			ftgt.delete();
			removed.add(ftgt.getHash());
		}

		json.put("removed", removed.toArray(new String[removed.size()]));
	}
}
