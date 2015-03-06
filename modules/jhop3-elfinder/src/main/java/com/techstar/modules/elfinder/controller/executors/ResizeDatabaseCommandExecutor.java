package com.techstar.modules.elfinder.controller.executors;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseJsonCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Content;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

@Transactional(readOnly = true)
public class ResizeDatabaseCommandExecutor extends AbstractDatabaseJsonCommandExecutor implements CommandExecutor {

	
	@Transactional(readOnly = false)
	@Override
	protected void execute(Param param, HttpServletRequest request, ServletContext servletContext, JSONObject json)
			throws Exception {
		String target = param.getTarget();
		String mode = param.getMode();

		Elfinder elfinder = super.findItem(target);
		Content content = elfinder.getContent();

		try (BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(content.getContent()));
				ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			BufferedImage image = ImageIO.read(in);

			if (StringUtils.equals("resize", mode)) {
				if (image.getWidth() != param.getWidth() || image.getHeight() != param.getHeight()) {
					Thumbnails.of(in).size(param.getWidth(), param.getHeight()).toOutputStream(out);
				}
			} else if (StringUtils.equals("crop", mode)) {
				Thumbnails.of(in).sourceRegion(param.getX(), param.getY(), param.getWidth(), param.getHeight())
						.size(param.getWidth(), param.getHeight()).toOutputStream(out);

			} else if (StringUtils.equals("rotate", mode)) {
				Thumbnails.of(in).size(param.getWidth(), param.getHeight()).rotate(param.getDegree())
						.keepAspectRatio(false).toOutputStream(out);
			}
			
			byte[] data = out.toByteArray();
			if(ArrayUtils.isNotEmpty(data)){
				content.setContent(data);
				content.setElfinder(elfinder);
				
				elfinder.setContent(content);
				elfinder.setMtime(new Date());
				elfinder.setSize((long)data.length);
				
				this.elfinderDao.save(elfinder);
			}
		}


		json.put("changed", new Object[] { super.getElfinderfo(request, elfinder,getCheckers()) });

	}

}
