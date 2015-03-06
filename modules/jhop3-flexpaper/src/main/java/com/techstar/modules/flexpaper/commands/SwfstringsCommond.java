package com.techstar.modules.flexpaper.commands;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.flexpaper.command.AbstractSwftoolsCommand;
import com.techstar.modules.flexpaper.domain.QueryParam;
import com.techstar.modules.flexpaper.util.ConverUtils;
import com.techstar.modules.flexpaper.util.ProcessUtils;

public class SwfstringsCommond extends AbstractSwftoolsCommand {
	
	@Override
	protected void execute(final QueryParam param, final HttpServletRequest request,
			final HttpServletResponse response, final ServletContext servletContext) throws IOException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		File pdfFile = param.getSrcFile();
		String pdfname = pdfFile.getName();// pdf文件名称
		File swfFile = ConverUtils.getConverFile(param,propertySource.getPathswf(),"swf");

		int pagecount = this.getPagecount(swfFile.getParentFile(), pdfname);

		String text = this.findText(param, pagecount);
		OutputStream out = response.getOutputStream();
		out.write(text.getBytes());
		IOUtils.closeQuietly(out);

	}

	private String findText(final QueryParam param, int pagecount) {

		if (StringUtils.isEmpty(StringUtils.trim(param.getSearchterm()))) {
			return "[{\"page\":-1, \"position\":-1}]";
		}

		int page = param.getPage();
		File swfFile = ConverUtils.getConverFile(param,propertySource.getPathswf(),"swf");
		try {

			String command = propertySource.getExtracttext();
			command = command.replace("{swffile}", swfFile.getAbsolutePath());

			String output = ProcessUtils.execs(command);
			int pos = -1;
			if (output != null)
				pos = output.toLowerCase().indexOf(param.getSearchterm().toLowerCase());
			if (pos > 0) {
				return "[{\"page\":" + page + ", \"position\":" + pos + "}]";
			} else {
				if (page < pagecount) {
					page++;
					param.setPage(page);
					return findText(param, pagecount);
				} else {
					return "[{\"page\":-1, \"position\":-1}]";
				}
			}
		} catch (Exception ex) {
			return ex.toString();
		}
	}

	private int getPagecount(final File swfDir, final String pdfFileName) {

		File files[] = swfDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getName().startsWith(pdfFileName);
			}

		});

		return ArrayUtils.isEmpty(files) ? 0 : files.length;
	}


}
