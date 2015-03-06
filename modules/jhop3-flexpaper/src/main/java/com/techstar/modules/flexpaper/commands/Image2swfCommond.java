package com.techstar.modules.flexpaper.commands;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.flexpaper.command.AbstractSwftoolsCommand;
import com.techstar.modules.flexpaper.domain.QueryParam;
import com.techstar.modules.flexpaper.util.ConverUtils;
import com.techstar.modules.flexpaper.util.ProcessUtils;

public abstract class Image2swfCommond extends AbstractSwftoolsCommand {

	protected abstract String getCommand();

	protected void before(final QueryParam param) throws IOException {

	}

	@Override
	public boolean execute(final QueryParam param,final boolean upload) throws IOException {
		boolean return_var = false;
		File swfFile = ConverUtils.getConverFile(param, propertySource.getPathswf(), "swf");
		before(param);
		
		File srcFile = param.getSrcFile();
		String command = getCommand();
		command = command.replace("{srcfile}", srcFile.getAbsolutePath());
		command = command.replace("{swffile}", swfFile.getAbsolutePath());
		return_var = ProcessUtils.exec(command);
		if (!StringUtils.equals(param.getOfficeFile().getName(), param.getSrcFile().getName())) {
			param.getSrcFile().delete();
		}
		return return_var;
	}

	@Override
	protected void execute(final QueryParam param, final HttpServletRequest request,
			final HttpServletResponse response, final ServletContext servletContext) throws IOException {

		File swfFile = ConverUtils.getConverFile(param, propertySource.getPathswf(), "swf");

		if (!param.getOfficeFile().exists()) {
			logger.error("文件:{}不存在！", param.getOfficeFile().getName());
			write("文件:" + param.getOfficeFile().getName() + "不存在！", response);
			return;
		}

		if (isNotConverted(param.getOfficeFile(), swfFile)) {
			boolean return_var = execute(param,false);// ProcessUtils.exec(command);
			if (!return_var) {
				logger.error("文件转换失败, 请确认文件转换工具swftools己经安装和当前用户对swf输出目录拥有读写权限");
				write("文件转换失败, 请确认文件转换工具swftools己经安装和当前用户对swf输出目录拥有读写权限", response);
				return;
			}
		}

		if (swfFile.exists()) {
			if (propertySource.isAllowcache()) {
				setCacheHeaders(response);
			}

			if (!propertySource.isAllowcache() || (propertySource.isAllowcache() && endOrRespond(request, response))) {
				response.setContentType("application/x-shockwave-flash");
				response.setHeader("Accept-Ranges", "bytes");
				OutputStream out = null;
				try {
					out = response.getOutputStream();
					FileUtils.copyFile(swfFile, out);
				} finally {
					IOUtils.closeQuietly(out);
				}
			}
		} else {
			logger.error("SWF file :{}不存在.", swfFile.getName());
			write("SWF file:" + swfFile.getName() + "不存在.", response);
		}
	}

	private boolean isNotConverted(File srcFile, File swfFile) {
		return !swfFile.exists() || srcFile.lastModified() > swfFile.lastModified();
	}
}
