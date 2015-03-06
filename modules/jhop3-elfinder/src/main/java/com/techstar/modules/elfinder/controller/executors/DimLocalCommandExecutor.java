package com.techstar.modules.elfinder.controller.executors;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class DimLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {

	@Override
	protected void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {
		String target = param.getTarget();
		FsItemEx fsi = super.findItem(fsService, target);
		InputStream is = fsi.openInputStream();
		BufferedImage image = ImageIO.read(is);

		StringBuilder sb = new StringBuilder();
		sb.append(image.getWidth()).append("x").append(image.getHeight());

		json.put("dim", sb.toString());

		IOUtils.closeQuietly(is);

	}

}
