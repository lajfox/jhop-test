package com.techstar.modules.elfinder.controller.executors;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Content;
import com.techstar.modules.elfinder.domain.Param;

public class DimDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	@Override
	protected void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {

		Content fsi = this.elfinderDao.findOne(Content.class, param.getTarget());

		if (fsi != null && ArrayUtils.isNotEmpty(fsi.getContent())) {
			try (BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(fsi.getContent()))) {

				BufferedImage image = ImageIO.read(in);

				StringBuilder sb = new StringBuilder();
				sb.append(image.getWidth()).append("x").append(image.getHeight());

				json.put("dim", sb.toString());
			}
		}

	}

}
