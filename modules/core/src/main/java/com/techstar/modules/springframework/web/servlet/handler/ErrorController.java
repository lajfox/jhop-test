package com.techstar.modules.springframework.web.servlet.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techstar.modules.springframework.data.jpa.domain.Results;

@Controller
@RequestMapping("/static/errors")
public class ErrorController {

	@RequestMapping("")
	public void msg(HttpServletResponse response) throws IOException {
		Results results = ErrorConext.get();
		response.setStatus(results.getStatusCode());
		response.sendError(results.getStatusCode(), results.getMessage());
	}
}
