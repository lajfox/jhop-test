package com.techstar.modules.elfinder.controller.executors;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;
import com.techstar.modules.elfinder.controller.executor.AbstractDatabaseCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.domain.Content;
import com.techstar.modules.elfinder.domain.Elfinder;
import com.techstar.modules.elfinder.domain.Param;

public class TmbDatabaseCommandExecutor extends AbstractDatabaseCommandExecutor implements CommandExecutor {

	private static final TimeZone TIMEZONE = TimeZone.getTimeZone("Europe/London");

	@Override
	public void execute(Param param, HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext) throws Exception {
		String target = param.getTarget();
		Elfinder fsi = super.findItem(target);
		Content conten = this.elfinderDao.findOne(Content.class, target);

		if (ArrayUtils.isNotEmpty(conten.getContent())) {
			try (BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(conten.getContent()));
					OutputStream out = response.getOutputStream()) {
				BufferedImage image = ImageIO.read(in);
				if (image != null) {
					int width = fsi.getWidth() == null ? 80 : fsi.getWidth();
					ResampleOp rop = new ResampleOp(DimensionConstrain.createMaxDimension(width, -1));
					rop.setNumberOfThreads(4);
					BufferedImage b = rop.filter(image, null);

					String value = DateFormatUtils.format(DateUtils.addDays(Calendar.getInstance().getTime(), 2 * 360),
							"d MMM yyyy HH:mm:ss 'GMT'", TIMEZONE, Locale.ENGLISH);
					response.setHeader("Last-Modified", value);
					response.setHeader("Expires", value);

					ImageIO.write(b, "png", out);
				}
			}
		}
	}
}
