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

public class ArchiveCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {

	@Autowired
	private CompressCommandFactory compressCommandFactory;

	@Override
	protected void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {

		CompressCommand command = compressCommandFactory.get(param.getType());
		if (command == null) {
			logger.error("未知的命令:{}", param.getType());
			json.put("error", "errUnknownCmd");
		} else {
			FsItemEx newFile = null;
			try {
				newFile = command.compress(fsService, param);
				json.put("added", new Object[] { getFsItemInfo(request, newFile) });
			} catch (IOException e) {
				logger.error("无法创建压缩包:{}",e);
				if (newFile != null && newFile.exists()) {
					newFile.deleteFile();
				}
				json.put("error", "errArchive");
			}
		}

	}

}
