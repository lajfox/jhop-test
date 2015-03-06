package com.techstar.example.elfinder.web.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.util.FsServiceUtils;
import com.techstar.modules.flexpaper.FlexpaperPropertySource;
import com.techstar.modules.flexpaper.command.CommandContext;
import com.techstar.modules.flexpaper.command.SwftoolsCommand;
import com.techstar.modules.flexpaper.command.SwftoolsCommandFactory;
import com.techstar.modules.flexpaper.domain.QueryParam;
import com.techstar.modules.flexpaper.jodconverter.web.WebappContext;
import com.techstar.modules.flexpaper.util.ConverUtils;
import com.techstar.modules.flexpaper.util.PdfUtils;

@Controller
@RequestMapping("flexPaper")
public class FlexPaperController  implements ServletContextAware {

	private static final Logger logger = LoggerFactory.getLogger(FlexPaperController.class);

	private ServletContext servletContext;
	@Autowired
	private SwftoolsCommandFactory swftoolsCommandFactory;
	@Autowired
	@Qualifier("flexpaperPropertySource")
	protected FlexpaperPropertySource propertySource;

	@RequestMapping
	public String doc(Model model, QueryParam param, HttpServletRequest request) throws IOException {
		FsService fsService = (FsService) request.getSession().getAttribute("AAA");
		FsItemEx fsi = FsServiceUtils.findItem(fsService, param.getTarget());
		model.addAttribute("doc", fsi.getName());

		if (FilenameUtils.isExtension(fsi.getName(), "pdf")) {

			param.setVolumeId(fsi.getVolumeId());
			param.setSrcFile(fsi.getFile());
			param.setPage(1);

			File pdfFile = param.getSrcFile();
			File swfFile = ConverUtils.getConverFile(param,propertySource.getPathswf(),"swf");

			if (swfFile.exists() && swfFile.lastModified() > pdfFile.lastModified()) {
				model.addAttribute("totalPages", getPagecount(swfFile.getParentFile(), pdfFile.getName()));
			} else {
				model.addAttribute("totalPages", PdfUtils.getNumberOfPages(fsi.getFile()));
			}
		}

		return "flexpaper/split_document";
	}

	@RequestMapping("view")
	public void view(QueryParam param, HttpServletRequest request, final HttpServletResponse response)
			throws IOException {

		FsService fsService = (FsService) request.getSession().getAttribute("AAA");
		FsItemEx fsi = FsServiceUtils.findItem(fsService, param.getTarget());
		
		param.setVolumeId(fsi.getVolumeId());
		param.setOfficeFile(fsi.getFile());
		
		if (FilenameUtils.isExtension(fsi.getName(), "pdf")) {
			param.setSrcFile(fsi.getFile());
		} else {
			param.setSrcFile(fsi.getFile());
			param.setSrcFile(ConverUtils.getConverFile(param,propertySource.getPathswf(),"pdf"));
		}

		SwftoolsCommand swftoolsCommand = swftoolsCommandFactory.get(param.getFormat());

		final HttpServletRequest finalRequest = request;
		final HttpServletResponse finalResponse = response;
		final QueryParam finalParam = param;
		swftoolsCommand.execute(new CommandContext() {

			@Override
			public HttpServletRequest getRequest() {
				return finalRequest;
			}

			@Override
			public HttpServletResponse getResponse() {
				return finalResponse;
			}

			@Override
			public ServletContext getServletContext() {
				return servletContext;
			}

			@Override
			public QueryParam getQueryParam() {
				return finalParam;
			}

		});
		
		if(!FilenameUtils.isExtension(fsi.getName(), "pdf")){
			param.getSrcFile().delete();
		}
	}

	@RequestMapping("office2pdf")
	public @ResponseBody
	String officeConverter(QueryParam param, HttpServletRequest request) throws IOException {

		FsService fsService = (FsService) request.getSession().getAttribute("AAA");
		FsItemEx fsi = FsServiceUtils.findItem(fsService, param.getTarget());

		param.setVolumeId(fsi.getVolumeId());
		param.setSrcFile(fsi.getFile());

		File officeFile = param.getSrcFile();
		File pdfFile = ConverUtils.getConverFile(param,propertySource.getPathswf(),"pdf");

		param.setPage(1);
		param.setSrcFile(pdfFile);
		File swfFile = ConverUtils.getConverFile(param,propertySource.getPathswf(),"swf");

		if (swfFile.exists() && swfFile.lastModified() > officeFile.lastModified()) {
			return Integer.toString(getPagecount(swfFile.getParentFile(), pdfFile.getName()));
		} else if (pdfFile.exists() && pdfFile.lastModified() > officeFile.lastModified()) {
			return Integer.toString(PdfUtils.getNumberOfPages(pdfFile));
		} else {
			OfficeDocumentConverter converter = WebappContext.get(servletContext).getDocumentConverter();

			String outputExtension = FilenameUtils.getExtension(pdfFile.getName());
			DocumentFormat outputFormat = converter.getFormatRegistry().getFormatByExtension(outputExtension);

			converter.convert(officeFile, pdfFile, outputFormat);

			return Integer.toString(PdfUtils.getNumberOfPages(pdfFile));
		}

	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
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
