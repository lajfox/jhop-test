package com.techstar.modules.flexpaper.command;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.techstar.modules.flexpaper.FlexpaperPropertySource;
import com.techstar.modules.flexpaper.domain.QueryParam;

public abstract class AbstractSwftoolsCommand  implements SwftoolsCommand {

	protected static final Logger logger = LoggerFactory.getLogger(AbstractSwftoolsCommand.class);

	@Autowired
	@Qualifier("flexpaperPropertySource")
	protected FlexpaperPropertySource propertySource;
	
	@Override
	public void execute(CommandContext context) throws IOException {
		execute(context.getQueryParam(), context.getRequest(), context.getResponse(), context.getServletContext());
	}

	protected abstract void execute(final QueryParam param, final HttpServletRequest request,
			final HttpServletResponse response, final ServletContext servletContext) throws IOException;
	
	@Override
	public boolean execute(final QueryParam param, final boolean upload) throws IOException {
		return false;
	}

	protected void setCacheHeaders(HttpServletResponse response) {
		response.setHeader("Cache-Control", "private, max-age=10800, pre-check=10800");
		response.setHeader("Pragma", "private");
		SimpleDateFormat RFC822DATEFORMAT = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
		response.setHeader("Expires", RFC822DATEFORMAT.format(new Date()));
	}

	protected boolean endOrRespond(HttpServletRequest request, HttpServletResponse response) {
		String mod = request.getHeader("If-Modified-Since");
		if (mod == null) {
			return true;
		}
		response.setHeader("Last-Modified", mod);
		return false;
	}

	protected int getStringHashCode(String string) {
		string = StringUtils.trim(string);
		if (StringUtils.isEmpty(string)) {
			return 0;
		}
		int hash = 0;
		for (int i = 0; i < string.length(); i++) {
			hash = 31 * hash + (int) string.charAt(i);
		}
		return hash;
	}

	protected void write(final String content, final HttpServletResponse response) throws IOException {
		OutputStream out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			out = response.getOutputStream();
			IOUtils.write(("Error:" + content).getBytes(), out);
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
		}
	}


}
