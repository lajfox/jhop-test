package com.techstar.modules.flexpaper.commands;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.flexpaper.command.AbstractSwftoolsCommand;
import com.techstar.modules.flexpaper.domain.QueryParam;
import com.techstar.modules.flexpaper.util.ConverUtils;
import com.techstar.modules.flexpaper.util.ProcessUtils;

public class SwfdumpCommond extends AbstractSwftoolsCommand {

	private static final Pattern PATTERN = Pattern.compile("[\\d]+", Pattern.CASE_INSENSITIVE);

	@Override
	protected void execute(final QueryParam param, final HttpServletRequest request,
			final HttpServletResponse response, final ServletContext servletContext) throws IOException {

		File swfFile = ConverUtils.getConverFile(param,propertySource.getPathswf(),"swf");
		if (swfFile.exists()) {

			StringBuilder sb = new StringBuilder();
			sb.append(StringUtils.defaultString(param.getCallback(), ""));
			sb.append("({");

			String width = null, height = null;
			String command = propertySource.getSwfheight();
			command = command.replace("{swffile}", swfFile.getAbsolutePath());
			String ret = ProcessUtils.execs(command);
			if (StringUtils.isNotEmpty(ret)) {
				height = getNumerics(ret);
				if (StringUtils.isNotEmpty(height)) {
					sb.append(",\"height\":").append(height);
				}
			} else {
				logger.error("错误:{}", command);
				write("错误:" + command, response);
				return;
			}

			command = propertySource.getSwfwidth();
			command = command.replace("{swffile}", swfFile.getAbsolutePath());
			 ret = ProcessUtils.execs(command);
			if (StringUtils.isNotEmpty(ret)) {
				width = getNumerics(ret);
				if (StringUtils.isNotEmpty(width)) {
					sb.append("\"width\":").append(width);
				}
			} else {
				logger.error("错误:{}", command);
				write("错误:" + command, response);
				return;
			}

			sb.append("})");

			response.setContentType("application/json; charset=UTF-8");
			write(sb.toString(), response);
		} else {
			logger.error("SWF文件不存在:{}", swfFile.getAbsolutePath());
			write("SWF文件不存在:" + swfFile.getAbsolutePath(), response);
		}

	}

	public String getNumerics(final String input) {
		Matcher matcher = PATTERN.matcher(input);
		return matcher.find() ? matcher.group() : null;
	}

}
