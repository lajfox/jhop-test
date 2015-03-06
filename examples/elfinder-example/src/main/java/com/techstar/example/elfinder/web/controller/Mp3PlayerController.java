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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.example.elfinder.domain.mp3player.Mp3player;
import com.techstar.example.elfinder.domain.mp3player.Song;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.util.FsServiceUtils;
import com.techstar.modules.flexpaper.FlexpaperPropertySource;

@Controller
@RequestMapping("mp3Player")
public class Mp3PlayerController extends FlexpaperPropertySource {

	private static final Logger logger = LoggerFactory.getLogger(Mp3PlayerController.class);

	@RequestMapping
	public String jwplayer() {
		return "mp3player/mp3player";
	}

	@RequestMapping(value="songs.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody
	Mp3player songs(@RequestParam("target") String target, HttpServletRequest request,
			final HttpServletResponse response)  {

		FsService fsService = (FsService) request.getSession().getAttribute("AAA");
		FsItemEx fsi = FsServiceUtils.findItem(fsService, target);

		Mp3player mp3player = new Mp3player();
		Song song = new Song(request.getContextPath() + "/mp3Player/view?target=" + target, FilenameUtils.getBaseName(fsi.getName()));
		
		List<Song> songs = new ArrayList<Song>();
		songs.add(song);	
		mp3player.setSongs(songs);

		return mp3player;

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
