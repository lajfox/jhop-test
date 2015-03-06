package com.techstar.modules.elfinder.controller.executors;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.elfinder.controller.executor.AbstractLocalCommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.FsItemEx;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;

public class FileLocalCommandExecutor extends AbstractLocalCommandExecutor implements CommandExecutor {
	@Override
	public void execute(FsService fsService, Param param, HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext) throws Exception {
		String target = param.getTarget();
		boolean download = BooleanUtils.toBoolean(param.getDownload());
		FsItemEx fsi = super.findItem(fsService, target);
		String mime = fsi.getMimeType();

		response.setCharacterEncoding("utf-8");
		response.setContentType(mime);

		String fileName = fsi.getName();
		if (download || StringUtils.equals("application/oct-stream", mime)) {

			response.setContentType(mime);
			response.setHeader("Content-Disposition",
					"attachments; " + getAttachementFileName(fileName, request.getHeader("USER-AGENT")));
			response.setHeader("Content-Transfer-Encoding", "binary");
		} else {
			response.setHeader("Content-Disposition",
					"inline; " + getAttachementFileName(fileName, request.getHeader("USER-AGENT")));
		}

		response.setContentLength((int) fsi.getSize());
		try (OutputStream out = response.getOutputStream();
		// InputStream is = fsi.openInputStream();
		) {
			FileUtils.copyFile(fsi.getFile(), out);
			// IOUtils.copy(is, out);
			// out.flush();//OutputStream的flush是个空方法体，调用flush画蛇添足
		}
	}

	private String getAttachementFileName(String fileName, String userAgent) throws UnsupportedEncodingException {
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();

			if (userAgent.indexOf("msie") != -1) {
				return "filename=\"" + URLEncoder.encode(fileName, "UTF8") + "\"";
			}

			// Opera浏览器只能采用filename*
			if (userAgent.indexOf("opera") != -1) {
				return "filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF8");
			}
			// Safari浏览器，只能采用ISO编码的中文输出
			if (userAgent.indexOf("safari") != -1) {
				return "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
			}
			// Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
			if (userAgent.indexOf("applewebkit") != -1) {
				return "filename=\"" + MimeUtility.encodeText(fileName, "UTF8", "B") + "\"";
			}
			// FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
			if (userAgent.indexOf("mozilla") != -1) {
				return "filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF8");
			}
		}

		return "filename=\"" + URLEncoder.encode(fileName, "UTF8") + "\"";
	}
}
