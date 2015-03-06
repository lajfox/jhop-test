package com.techstar.modules.elfinder.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.techstar.modules.elfinder.controller.executor.CommandExecutionContext;
import com.techstar.modules.elfinder.controller.executor.CommandExecutor;
import com.techstar.modules.elfinder.controller.executor.CommandExecutorFactory;
import com.techstar.modules.elfinder.domain.Param;
import com.techstar.modules.elfinder.impl.DefaultFsService;
import com.techstar.modules.elfinder.localfs.LocalFsVolume;
import com.techstar.modules.elfinder.service.FsService;
import com.techstar.modules.elfinder.service.FsServiceFactory;
import com.techstar.modules.elfinder.service.FsVolume;

public abstract class AbstractConnectorController implements ServletContextAware {

	private static final Logger logger = LoggerFactory.getLogger(AbstractConnectorController.class);
	private static final Map<String, DefaultFsService> FSMAP = new HashMap<String, DefaultFsService>();

	@Resource(name = "commandExecutorFactory")
	private CommandExecutorFactory _commandExecutorFactory;

	@Resource(name = "fsServiceFactory")
	private FsServiceFactory _fsServiceFactory;

	private ServletContext servletContext;

	protected abstract FsVolume[] getVolumes();

	protected abstract void beforeProcess(final Param param);

	protected abstract void saveFsService(HttpServletRequest httpServletRequest, FsService fsService);

	@RequestMapping
	public void connector(Param param,
			@RequestParam(value = "upload[]", required = false) CommonsMultipartFile upload[],
			@RequestParam(value = "targets[]", required = false) String targets[], HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		param.setTargets_(targets);
		param.setUploadFiles(upload);

		CommandExecutor ce = _commandExecutorFactory.get(param.getCmd());

		if (ce == null) {
			logger.error("unknown command:{}", param.getCmd());
			throw new FsException(String.format("unknown command: %s", param.getCmd()));
		}

		beforeProcess(param);

		final HttpServletRequest finalRequest = request;
		final HttpServletResponse finalResponse = response;
		final Param finalParam = param;
		// try {

		ce.execute(new CommandExecutionContext() {

			@Override
			public FsService getFileService() {

				FsVolume[] volumes = getVolumes();

				if (ArrayUtils.isEmpty(volumes)) {
					return _fsServiceFactory.getFileService(finalRequest, servletContext);
				} else {

					FsService oldFsService = _fsServiceFactory.getFileService(finalRequest, servletContext);

					// 原有配置文档目录
					FsVolume[] oldVolumes = oldFsService.getVolumes();
					oldVolumes = ArrayUtils.isEmpty(oldVolumes) ? volumes : ArrayUtils.addAll(oldVolumes, volumes);
					String key = getKey(oldVolumes);
					DefaultFsService newFsService = FSMAP.get(key);
					if (newFsService == null) {

						newFsService = new DefaultFsService();
						newFsService.setServiceConfig(oldFsService.getServiceConfig());
						newFsService.setVolumes(oldVolumes);

						FSMAP.put(key, newFsService);
					}

					saveFsService(getRequest(), newFsService);

					return newFsService;
				}
			}

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
			public Param getParam() {
				return finalParam;
			}

		});
		/*
		 * } catch (Exception e) { logger.error("unknown error:", e); throw new
		 * FsException("unknown error", e); }
		 */
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	private String getKey(final FsVolume[] volumes) {
		StringBuilder sb = new StringBuilder();
		for (FsVolume volume : volumes) {
			sb.append(((LocalFsVolume) volume).getRootDir().getAbsolutePath());
		}
		return sb.toString();
	}

}