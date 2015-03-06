package com.techstar.modules.highcharts.export.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.fop.svg.PDFTranscoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author sundoctor
 * 
 */
@Controller
@RequestMapping("/highcharts/batik/export")
public class BatikExportController {

	@RequestMapping(method = RequestMethod.POST)
	public void exporter(@RequestParam("svg") String svg, @RequestParam("type") String type,
			@RequestParam(value = "filename", required = false) String filename, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		try (ServletOutputStream out = response.getOutputStream()) {

			if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(svg)) {

				svg = svg.replaceAll(":rect", "rect");
				String ext = null;
				Transcoder t = null;

				switch (type) {
				case "image/png":
					ext = "png";
					t = new PNGTranscoder();
					break;
				case "image/jpeg":
					ext = "jpg";
					t = new JPEGTranscoder();
					break;
				case "application/pdf":
					ext = "pdf";
					t = new PDFTranscoder();
					break;
				case "image/svg+xml":
					ext = "svg";
					break;
				}

				if (StringUtils.isNotEmpty(ext)) {

					response.addHeader("Content-Disposition", "attachment; filename=" + getFilename(filename) + "."
							+ ext);
					response.addHeader("Content-Type", type);

					if (null != t) {
						TranscoderInput input = new TranscoderInput(new StringReader(svg));
						TranscoderOutput output = new TranscoderOutput(out);
						try {
							t.transcode(input, output);
						} catch (TranscoderException e) {
							out.print("Problem transcoding stream. See the web logs for more details.");
							e.printStackTrace();
						}

					} else if (ext == "svg") {
						try (OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8")) {
							writer.append(svg);
							writer.flush();
						}
					}
				} else {
					out.print("Invalid type: " + type);
				}
			} else {
				out.println("Usage:\n\tParameter [svg]: The DOM Element to be converted.\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
			}
			out.flush();
		}
	}

	@ExceptionHandler(IOException.class)
	public ModelAndView handleIOException(Exception ex, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("highcharts/error");
		modelAndView.addObject("message", ex.getMessage());
		response.setStatus(500);
		return modelAndView;
	}

	@ExceptionHandler(ServletException.class)
	public ModelAndView handleServletException(Exception ex, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("highcharts/error");
		modelAndView.addObject("message", ex.getMessage());
		response.setStatus(500);
		return modelAndView;
	}

	private String getFilename(String name) {
		name = sanitize(name);
		return (name != null) ? name : "chart";
	}

	private String sanitize(String parameter) {
		if (parameter != null && !parameter.trim().isEmpty() && !(parameter.compareToIgnoreCase("undefined") == 0)) {
			return parameter.trim();
		}
		return null;
	}

}