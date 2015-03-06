package com.techstar.example.elfinder.web.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
@RequestMapping("jwplayer")
public class JwplayerController extends FlexpaperPropertySource {

	private static final Logger logger = LoggerFactory.getLogger(JwplayerController.class);

	@RequestMapping
	public String jwplayer(Model model, @RequestParam("target") String target, HttpServletRequest request) {
		FsService fsService = (FsService) request.getSession().getAttribute("AAA");
		FsItemEx fsi = FsServiceUtils.findItem(fsService, target);
		model.addAttribute("extension", FilenameUtils.getExtension(fsi.getName()));

		return "jwplayer/jwplayer";
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
