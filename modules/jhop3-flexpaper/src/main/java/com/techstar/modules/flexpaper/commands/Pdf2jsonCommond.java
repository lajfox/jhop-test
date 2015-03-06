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

public class Pdf2jsonCommond extends AbstractSwftoolsCommand {

	@Override
	protected void execute(final QueryParam param, final HttpServletRequest request,
			final HttpServletResponse response, final ServletContext servletContext) throws IOException {

		File pdfFile = param.getSrcFile();
		String pathpdf = pdfFile.getParent().replace('\\', '/');// 文件目录
		String pdfname = pdfFile.getName();// 文件名称

		if (!pdfFile.exists()) {
			logger.error("PDF文件:{}不存在！", pdfname);
			write("PDF文件:" + pdfname + "不存在！", response);
			return;
		}

		File jsFile = ConverUtils.getConverFile(param,propertySource.getPathswf(),"js");

		if (isNotConverted(pdfFile, jsFile)) {
			String command = param.isSplitmode() ? propertySource.getSplitjsonfile() : propertySource.getJsonfile();

			command = command.replace("{path.pdf}", pathpdf);
			command = command.replace("{path.swf}", jsFile.getParent().replace('\\', '/'));
			command = command.replace("{pdffile}", pdfname);
			if (param.isSplitmode()) {
				command = command.replace("%", param.getPage().toString());
			}

			if (!ProcessUtils.exec(command)) {
				logger.error("文档转换失败, 请确认文件转换工具pdf2json己经安装和当前用户对swf输出目录拥有读写权限");
				write("文档转换失败, 请确认文件转换工具pdf2json己经安装和当前用户对swf输出目录拥有读写权限", response);
				return;
			}

		}

		if (jsFile.exists()) {

			if (propertySource.isAllowcache()) {
				setCacheHeaders(response);
			}

			if (!propertySource.isAllowcache() || (propertySource.isAllowcache() && endOrRespond(request, response))) {
				response.setContentType("text/javascript");

				OutputStream out = null;
				try {
					out = response.getOutputStream();
					if (StringUtils.equals("json", param.getFormat())) {
						FileUtils.copyFile(jsFile, out);
					} else if (StringUtils.equals("jsonp", param.getFormat())) {
						out.write((StringUtils.defaultString(param.getCallback(), "") + "(").getBytes());
						FileUtils.copyFile(jsFile, out);
						out.write((");").getBytes());
					}
				} finally {
					IOUtils.closeQuietly(out);
				}
			}
		} else {
			logger.error("SWF file :{}不存在.", jsFile.getName());
			write("SWF file:" + jsFile.getName() + "不存在.", response);
		}
	}

	private boolean isNotConverted(File pdfFile, File jsFile) {

		return !jsFile.exists() || pdfFile.lastModified() > jsFile.lastModified();
	}

}
