package com.techstar.example.elfinder.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.util.FsServiceUtils;
import com.techstar.modules.flexpaper.FlexpaperPropertySource;

@Controller
@RequestMapping("jplayer")
public class JplayerController extends FlexpaperPropertySource {

	private static final Logger logger = LoggerFactory.getLogger(JplayerController.class);

	@RequestMapping
	public String jwplayer(Model model, @RequestParam("target") String target, HttpServletRequest request) {
		FsService fsService = (FsService) request.getSession().getAttribute("AAA");
		FsItemEx fsi = FsServiceUtils.findItem(fsService, target);
		String mime = fsi.getMimeType();
		model.addAttribute("extension", FilenameUtils.getExtension(fsi.getName()));
		model.addAttribute("filename", FilenameUtils.getBaseName(fsi.getName()));

		List<String> extensions = new ArrayList<String>();
		extensions.add("oga");
		extensions.add("fla");
		extensions.add("webma");
		
		mime = FilenameUtils.isExtension(fsi.getName(), extensions) ? "audio" : StringUtils.substringBefore(mime, "/");
		
		return "jplayer/jplayer-" + mime;
	}

	@RequestMapping("view")
	public void view(@RequestParam("target") String target, HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		FsService fsService = (FsService) request.getSession().getAttribute("AAA");
		FsItemEx fsi = FsServiceUtils.findItem(fsService, target);

		response.setCharacterEncoding("utf-8");
		String mime = fsi.getMimeType();
		response.setContentType(mime);

		OutputStream out = null;
		response.setContentLength((int) fsi.getSize());
		try {
			out = response.getOutputStream();
			FileUtils.copyFile(fsi.getFile(), out);
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
		}

	}

}
