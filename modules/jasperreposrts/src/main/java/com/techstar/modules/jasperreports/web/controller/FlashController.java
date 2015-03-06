package com.techstar.modules.jasperreports.web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("flash")
public class FlashController {

	@RequestMapping("")
	public ModelAndView applet(Map<String, Object> model) {
		return new ModelAndView("flashView", model);
	}
}
