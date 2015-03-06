package com.techstar.modules.elfinder.controller.executors;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class ResizeLocalCommandExecutor extends AbstractLocalJsonCommandExecutor implements CommandExecutor {

	@Override
	protected void execute(FsService fsService, Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception {
		String target = param.getTarget();
		String mode = param.getMode();

		FsItemEx fsi = super.findItem(fsService, target);
		File file = fsi.getFile();
		File outFile = new File(file.getAbsolutePath());
		BufferedImage image = ImageIO.read(file);

		if (StringUtils.equals("resize", mode)) {
			if (image.getWidth() != param.getWidth() || image.getHeight() != param.getHeight()) {
				Thumbnails.of(file).size(param.getWidth(), param.getHeight()).toFile(outFile);
			}
		} else if (StringUtils.equals("crop", mode)) {
			Thumbnails.of(file).sourceRegion(param.getX(), param.getY(), param.getWidth(), param.getHeight())
					.size(param.getWidth(), param.getHeight()).toFile(outFile);

		} else if (StringUtils.equals("rotate", mode)) {
			Thumbnails.of(file).size(param.getWidth(), param.getHeight()).rotate(param.getDegree())
					.keepAspectRatio(false).toFile(outFile);
		}

		json.put("changed", new Object[] { super.getFsItemInfo(request, fsi) });

	}

}
