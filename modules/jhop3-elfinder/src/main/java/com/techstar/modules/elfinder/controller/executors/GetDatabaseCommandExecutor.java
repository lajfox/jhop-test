package com.techstar.modules.elfinder.controller.executors;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Content;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

public class GetDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {

		String target = param.getTarget();
		Content content = this.elfinderDao.findOne(Content.class, target);
		if (content != null && ArrayUtils.isNotEmpty(content.getContent())) {
			try {
				json.put("content", IOUtils.toString( content.getContent(), "utf-8"));
			} catch (IOException e) {
				Elfinder fsi = this.findItem(target);
				logger.error("无法打开:{},exception:{}", fsi.getName(), e);
				json.put("error", new String[] { "errOpen", fsi.getName() });
			}
		}else{
			json.put("content", "");
		}
	}
}
