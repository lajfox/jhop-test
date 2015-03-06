package com.techstar.modules.flexpaper.commands;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.flexpaper.command.AbstractSwftoolsCommand;
import com.techstar.modules.flexpaper.domain.QueryParam;
import com.techstar.modules.flexpaper.thread.Pdf2swfThread;
import com.techstar.modules.flexpaper.util.ConverUtils;
import com.techstar.modules.flexpaper.util.ProcessUtils;

public class Pdf2swfCommond extends AbstractSwftoolsCommand {

	protected ScheduledExecutorService scheduler ;
	
	public void init() {
		scheduler = Executors.newScheduledThreadPool(8);
	}
	
	public void destroy() {
		scheduler.shutdown();
	}

	@Override
	public boolean execute(final QueryParam param, final boolean upload) throws IOException {
		boolean return_var = false;
		File pdfFile = param.getSrcFile();
		File swfFile = ConverUtils.getConverFile(param, propertySource.getPathswf(), "swf");
		String pathpdf = pdfFile.getParent().replace('\\', '/');// 文件目录
		String pdfname = pdfFile.getName();// 文件名称
		String command = param.isSplitmode() ? propertySource.getSplitpages() : propertySource.getSingledoc();
		command = command.replace("{path.pdf}", pathpdf);
		command = command.replace("{path.swf}", swfFile.getParent().replace('\\', '/'));
		command = command.replace("{pdffile}", pdfname);
		
		if(FilenameUtils.isExtension(param.getOfficeFile().getName().toLowerCase(), "dwg")&&param.getOfficeFile().length()>1024*1024){
			command = command +" -s poly2bitmap ";
		}

		if (upload || !param.isSplitmode()) {
			return_var = ProcessUtils.exec(command);
			// 删除中openoffice生成的pdf文件
			if (!StringUtils.equals(param.getOfficeFile().getName(), param.getSrcFile().getName())) {
				FileUtils.deleteQuietly(param.getSrcFile());
			}
		} else {
			String pagecmd = command.replace("%", param.getPage().toString());
			pagecmd = pagecmd + " -p " + param.getPage();
			return_var = ProcessUtils.exec(pagecmd);
			if(param.getPage() != null && param.getPage() == 1){				
				scheduler.schedule(new Pdf2swfThread(this, param, true), 0, TimeUnit.SECONDS);
			}
		}
		return return_var;
	}

	@Override
	protected void execute(final QueryParam param, final HttpServletRequest request,
			final HttpServletResponse response, final ServletContext servletContext) throws IOException {

		File pdfFile = param.getSrcFile();
		String pdfname = pdfFile.getName();// 文件名称
		if (!param.getOfficeFile().exists()) {
			logger.error("文件:{}不存在！", pdfname);
			write("文件:" + pdfname + "不存在！", response);
			return;
		}

		File swfFile = ConverUtils.getConverFile(param, propertySource.getPathswf(), "swf");
		if (isNotConverted(param.getOfficeFile(), swfFile)) {
			boolean return_var = execute(param, false);
			if (!return_var) {
				logger.error("文档转换失败, 请确认文件转换工具swftools己经安装和当前用户对swf输出目录拥有读写权限");
				write("文档转换失败, 请确认文件转换工具swftools己经安装和当前用户对swf输出目录拥有读写权限", response);
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

	private boolean isNotConverted(File pdfFile, File swfFile) {
		return !swfFile.exists() || pdfFile.lastModified() > swfFile.lastModified();
	}
}
