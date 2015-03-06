package com.techstar.modules.web.filter;

/**
 * 为Response设置gzip压缩的Filter.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.techstar.modules.utils.GZipUtils;

public class GZipFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String acceptEncoding = getGZIPEncoding(httpRequest);
		if (acceptEncoding == null) {
			chain.doFilter(request, response);
		} else {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			CachedResponseWrapper wrapper = new CachedResponseWrapper(httpResponse);
			// 写入wrapper:
			chain.doFilter(request, wrapper);
			// 对响应进行处理，这里是进行GZip压缩:
			byte[] data = GZipUtils.gzip(wrapper.getResponseData());
			httpResponse.setHeader("Content-Encoding", acceptEncoding);
			httpResponse.setContentLength(data.length);
			ServletOutputStream output = response.getOutputStream();
			output.write(data);
			output.flush();
		}
	}

	public void destroy() {
	}

	private String getGZIPEncoding(HttpServletRequest request) {
		String acceptEncoding = request.getHeader("Accept-Encoding");
		if (acceptEncoding == null) {
			return null;
		} else {
			acceptEncoding = acceptEncoding.toLowerCase();
			if (acceptEncoding.indexOf("x-gzip") >= 0) {
				return "x-gzip";
			}
			if (acceptEncoding.indexOf("gzip") >= 0) {
				return "gzip";
			}
		}
		return null;
	}

	private static class CachedResponseWrapper extends HttpServletResponseWrapper {

		public static final int OUTPUT_NONE = 0;
		public static final int OUTPUT_WRITER = 1;
		public static final int OUTPUT_STREAM = 2;
		private int outputType = OUTPUT_NONE;
		private int status = SC_OK;
		private ServletOutputStream output = null;
		private PrintWriter writer = null;
		private ByteArrayOutputStream buffer = null;

		public CachedResponseWrapper(HttpServletResponse resp) throws IOException {
			super(resp);
			buffer = new ByteArrayOutputStream();
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			super.setStatus(status);
			this.status = status;
		}

		public void sendError(int status, String string) throws IOException {
			super.sendError(status, string);
			this.status = status;
		}

		public void sendError(int status) throws IOException {
			super.sendError(status);
			this.status = status;
		}

		public void sendRedirect(String location) throws IOException {
			super.sendRedirect(location);
			this.status = SC_MOVED_TEMPORARILY;
		}

		public PrintWriter getWriter() throws IOException {
			if (outputType == OUTPUT_STREAM)
				throw new IllegalStateException();
			else if (outputType == OUTPUT_WRITER)
				return writer;
			else {
				outputType = OUTPUT_WRITER;
				writer = new PrintWriter(new OutputStreamWriter(buffer, getCharacterEncoding()));
				return writer;
			}
		}

		public ServletOutputStream getOutputStream() throws IOException {
			if (outputType == OUTPUT_WRITER)
				throw new IllegalStateException();
			else if (outputType == OUTPUT_STREAM)
				return output;
			else {
				outputType = OUTPUT_STREAM;
				output = new WrappedOutputStream(buffer);
				return output;
			}
		}

		public void flushBuffer() throws IOException {
			if (outputType == OUTPUT_WRITER)
				writer.flush();
			if (outputType == OUTPUT_STREAM)
				output.flush();
		}

		public void reset() {
			outputType = OUTPUT_NONE;
			buffer.reset();
		}

		public byte[] getResponseData() throws IOException {
			flushBuffer();
			return buffer.toByteArray();
		}

		private static class WrappedOutputStream extends ServletOutputStream {
			private ByteArrayOutputStream buffer;

			public WrappedOutputStream(ByteArrayOutputStream buffer) {
				this.buffer = buffer;
			}

			@Override
			public void write(int b) throws IOException {
				buffer.write(b);
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setWriteListener(WriteListener writeListener) {

			}

		}
	}
}
