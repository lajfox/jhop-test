package com.techstar.example.elfinder.web.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.util.FsServiceUtils;
import com.techstar.modules.flexpaper.FlexpaperPropertySource;

@Controller
@RequestMapping("cuPlayer")
public class CuPlayerController extends FlexpaperPropertySource {

	private static final Logger logger = LoggerFactory.getLogger(CuPlayerController.class);

	@RequestMapping
	public String jwplayer() {
		return "cuplayer/cuplayer";
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
