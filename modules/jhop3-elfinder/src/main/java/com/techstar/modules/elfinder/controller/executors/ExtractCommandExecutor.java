package com.techstar.modules.elfinder.controller.executors;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.techstar.modules.elfinder.compress.CompressCommand;
import com.techstar.modules.elfinder.compress.CompressCommandFactory;
import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class ExtractCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {

	@Autowired
	private CompressCommandFactory compressCommandFactory;

	@Override
	protected void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {

		FsItemEx fsi = super.findItem(fsService, param.getTarget());
		CompressCommand command = compressCommandFactory.get(fsi.getMimeType());
		if (command == null) {
			logger.error("未知的命令:{}", fsi.getMimeType());
			json.put("error", "errUnknownCmd");
		} else {
			FsItemEx dir = null;
			try {
				dir = command.extract(fsService, param);
				json.put("added", new Object[] { getFsItemInfo(request, dir) });
			} catch (IOException e) {
				logger.error("无法从 {} 提取文件.exception:{}", fsi.getName(), e);
				if (dir != null && dir.exists()) {
					dir.deleteFolder();
				}
				json.put("error", new String[] { "errExtract", fsi.getName() });
			}
		}

	}

}
