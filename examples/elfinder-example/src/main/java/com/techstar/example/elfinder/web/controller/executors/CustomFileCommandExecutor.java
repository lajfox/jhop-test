package com.techstar.example.elfinder.web.controller.executors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.controller.executors.FileLocalCommandExecutor;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

@Component("fileCommandExecutor")
public class CustomFileCommandExecutor extends FileLocalCommandExecutor {

	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext) throws Exception {

		boolean download = BooleanUtils.toBoolean(param.getDownload());
		FsItemEx fsi = super.findItem(fsService, param.getTarget());
		String mime = fsi.getMimeType();

		if (download) {
			super.execute(fsService, param, request, response, servletContext);
		}  else if (mime.startsWith("video") || mime.startsWith("audio")) {
			request.getRequestDispatcher("jwplayer").forward(request, response);
		} else {
			request.getRequestDispatcher("flexPaper").forward(request, response);
		}
	}
}
