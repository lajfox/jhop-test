package com.techstar.modules.flexpaper.commands;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.techstar.modules.flexpaper.command.AbstractSwftoolsCommand;
import com.techstar.modules.flexpaper.domain.QueryParam;
import com.techstar.modules.flexpaper.util.ConverUtils;
import com.techstar.modules.flexpaper.util.ProcessUtils;

public class SwfrenderCommond extends AbstractSwftoolsCommand {

	@Override
	protected void execute(final QueryParam param, final HttpServletRequest request,
			final HttpServletResponse response, final ServletContext servletContext) throws IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		File swfFile = ConverUtils.getConverFile(param, propertySource.getPathswf(), "swf");
		if (!swfFile.exists()) {
			logger.error("SWF文件:{}不存在！", swfFile.getName());
			write("SWF文件:" + swfFile.getName() + "不存在！", response);
			return;
		}

		String baseName = FilenameUtils.getBaseName(swfFile.getName());
		String fileName = param.isSplitmode() ? baseName + "_" + param.getPage() + ".png" : baseName + ".png";
		File pngFile = new File(swfFile.getParentFile(), fileName);

		if (isNotConverted(swfFile, pngFile)) {

			String command = param.isSplitmode() ? propertySource.getRendersplitpage() : propertySource.getRenderpage();

			command = command.replace("{swffile}", swfFile.getAbsolutePath().replace('\\', '/'));
			command = command.replace("{pngfile}", pngFile.getAbsolutePath().replace('\\', '/'));
			if (param.isSplitmode()) {
				command = command.replace("{page}", param.getPage().toString());
			}

			if (!ProcessUtils.exec(command)) {
				logger.error("SWF文件:{}转换PNG错误", swfFile.getName());
				write("SWF文件:" + swfFile.getName() + "转换PNG错误", response);
				return;
			}
		}

		if (pngFile.exists()) {

			if (propertySource.isAllowcache()) {
				setCacheHeaders(response);
			}

			if (!propertySource.isAllowcache() || (propertySource.isAllowcache() && endOrRespond(request, response))) {
				response.setContentType("image/png");
				OutputStream out = null;
				try {
					out = response.getOutputStream();
					FileUtils.copyFile(pngFile, out);
				} finally {
					IOUtils.closeQuietly(out);
				}
			}
		} else {
			logger.error("PNG file :{}不存在.", pngFile.getName());
			write("PNG file : " + pngFile.getName() + "不存在.", response);
		}

	}

	private boolean isNotConverted(File swfFile, File pngFile) {
		return !pngFile.exists() || swfFile.lastModified() > pngFile.lastModified();
	}

}
