package com.techstar.modules.elfinder.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.techstar.modules.elfinder.controller.executor.CommandExecutionContext;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutorFactory;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsServiceFactory;

@Controller("elfinderConnectorController")
@RequestMapping("elfinder/connector")
public class ConnectorController implements ServletContextAware {

	private static Logger logger = LoggerFactory.getLogger(ConnectorController.class);

	@Resource(name = "commandExecutorFactory")
	private CommandExecutorFactory _commandExecutorFactory;

	@Resource(name = "fsServiceFactory")
	private FsServiceFactory _fsServiceFactory;

	private ServletContext servletContext;

	@RequestMapping
	public void connector(Param param,
			@RequestParam(value = "upload[]", required = false) CommonsMultipartFile upload[],
			@RequestParam(value = "targets[]", required = false) String targets[], HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		param.setTargets_(targets);
		param.setUploadFiles(upload);

		CommandExecutor ce = _commandExecutorFactory.get(param.getCmd());

		if (ce == null) {
			logger.error("unknown command:{}", param.getCmd());
			throw new FsException(String.format("unknown command: %s", param.getCmd()));
		}

		try {
			final HttpServletRequest finalRequest = request;
			final Param finalParam = param;
			ce.execute(new CommandExecutionContext() {

				@Override
				public FsService getFileService() {
					return _fsServiceFactory.getFileService(finalRequest, servletContext);
				}

				@Override
				public HttpServletRequest getRequest() {
					return finalRequest;
				}

				@Override
				public HttpServletResponse getResponse() {
					return response;
				}

				@Override
				public ServletContext getServletContext() {
					return servletContext;
				}

				@Override
				public Param getParam() {
					return finalParam;
				}

			});
		} catch (Exception e) {
			logger.error("unknown error:", e);
			throw new FsException("unknown error", e);
		}
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}