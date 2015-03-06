package com.techstar.modules.jasperreports.web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.techstar.modules.jasperreports.domain.Specification;
import com.techstar.modules.jasperreports.web.servlet.support.RequestContext;

@Controller
@RequestMapping("jasper")
public class JasperreportsController {

	@RequestMapping("multi/{formatKey}")
	public ModelAndView multi(@PathVariable("formatKey") String formatKey, Specification spec) {
		Map<String, Object> model = spec.getModel();
		model.put(RequestContext.FORMATKEY, formatKey);
		return new ModelAndView("jasperReportsMultiFormatView", model);
	}

	@RequestMapping("applet")
	public ModelAndView applet(Map<String, Object> model) {
		return new ModelAndView("appletView", model);
	}

	/*
	 * @RequestMapping("multi2/{formatKey}") public ModelAndView
	 * multi2(@PathVariable("formatKey") String formatKey, HttpServletRequest
	 * request) {
	 * 
	 * String urlKey = KeyUtils.urlKey; String url =
	 * request.getParameter(urlKey);
	 * 
	 * String subUrlKey = KeyUtils.subUrlKey; String subUrl =
	 * request.getParameter(subUrlKey);
	 * 
	 * String fileNameKey = KeyUtils.fileNameKey; String fileName =
	 * request.getParameter(fileNameKey);
	 * 
	 * Map<String, Object> model = new HashMap<String, Object>();
	 * model.put(KeyUtils.formatKey, formatKey); model.put(urlKey, url);
	 * model.put(subUrlKey, subUrl); model.put(fileNameKey, fileName);
	 * 
	 * JasperReportsContext.setModel(model);
	 * 
	 * return new ModelAndView(url, model);
	 * 
	 * }
	 */

}
