package com.techstar.modules.elfinder.controller.executors;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;
import com.techstar.modules.elfinder.controller.executor.AbstractLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class TmbLocalCommandExecutor extends AbstractLocalCommandExecutor implements CommandExecutor {
	
	private static final TimeZone TIMEZONE = TimeZone.getTimeZone("Europe/London");

	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext) throws Exception {
		String target = param.getTarget();
		FsItemEx fsi = super.findItem(fsService, target);

		try (InputStream in = fsi.openInputStream(); OutputStream out = response.getOutputStream()) {
			BufferedImage image = ImageIO.read(in);
			if (image != null) {
				int width = fsService.getServiceConfig().getTmbWidth();
				ResampleOp rop = new ResampleOp(DimensionConstrain.createMaxDimension(width, -1));
				rop.setNumberOfThreads(4);
				BufferedImage b = rop.filter(image, null);

				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// ImageIO.write(b, "png", baos);
				// byte[] bytesOut = baos.toByteArray();

				String value = DateFormatUtils.format(DateUtils.addDays(Calendar.getInstance().getTime(), 2 * 360),
						"d MMM yyyy HH:mm:ss 'GMT'", TIMEZONE, Locale.ENGLISH);
				response.setHeader("Last-Modified", value);
				response.setHeader("Expires", value);

				ImageIO.write(b, "png", out);
			}
		}
	}
}
