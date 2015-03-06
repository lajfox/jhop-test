package com.techstar.modules.elfinder.controller.executors;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class UploadLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {
	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {

		List<FsItemEx> added = new ArrayList<FsItemEx>();

		String target = param.getTarget();
		FsItemEx dir = super.findItem(fsService, target);

		InputStream is = null;
		OutputStream os = null;
		for (CommonsMultipartFile fis : param.getUploadFiles()) {
			String fileName = fis.getOriginalFilename();
			FsItemEx newFile = new FsItemEx(dir, fileName);
			newFile.createFile();
			is = fis.getInputStream();
			os = newFile.openOutputStream();

			IOUtils.copy(is, os);

			added.add(newFile);
		}
		IOUtils.closeQuietly(os);
		IOUtils.closeQuietly(is);

		json.put("added", files2JsonArray(request, added));
	}
}
