package com.techstar.modules.elfinder.controller.executor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

import com.techstar.modules.elfinder.domain.Param;

public abstract class AbstractDatabaseJsonCommandExecutor extends AbstractDatabaseCommandExecutor {

	@Override
	final public void execute(Param param, HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext) throws Exception {
		JSONObject json = new JSONObject();

		String userAgent = request.getHeader("User-Agent");
		double ieversion = 9;
		boolean isie = StringUtils.contains(userAgent, "MSIE");
		if (isie) {
			ieversion = NumberUtils.toDouble(StringUtils.trim(StringUtils.substringBetween(userAgent, "MSIE", ";")));
			logger.info("IE:{},版本:{}", isie, ieversion);
		}

		if (isie && ieversion < 9) {
			response.setContentType("text/html; charset=UTF-8");
		} else {
			response.setContentType("application/json; charset=UTF-8");
		}

		try (PrintWriter writer = response.getWriter();){
			execute(param, request, servletContext, json);
			
			json.write(writer);
			writer.flush();
			//writer.close();
		} catch (IOException e) {
			logger.error("execute error:{}", e);
			json.put("error", e.getMessage());
		}
	}

	protected abstract void execute(Param param, HttpServletRequest request, ServletContext servletContext,
			JSONObject json) throws Exception;

}