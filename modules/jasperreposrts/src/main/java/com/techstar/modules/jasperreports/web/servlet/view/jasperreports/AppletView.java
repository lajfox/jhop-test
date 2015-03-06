package com.techstar.modules.jasperreports.web.servlet.view.jasperreports;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.AbstractView;

import com.techstar.modules.utils.FreeMarkerUtils;

public class AppletView extends AbstractView {

	private Properties parameterMappings;

	public void setParameterMappings(Properties parameterMappings) {
		this.parameterMappings = parameterMappings;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		prepareParameter(model, request);

		String reportUrl = MapUtils.getString(model, "report_url");
		Assert.hasText(reportUrl, "report url不能为空!");
		String url = request.getParameter("jr_url");
		Assert.hasText(url, "参数错误，参数jr_url不能为空!");

		String queryString = request.getQueryString();
		model.put("report_url", reportUrl + "?" + queryString);

		String viewer = FreeMarkerUtils.crateHTML(this.getClass(), model,
				"/com/techstar/modules/jasperreports/web/servlet/view/jasperreports", "viewer.ftl");

		response.setContentType(getContentType());
		Writer writer = response.getWriter();
		writer.write(viewer);
		writer.flush();
		IOUtils.closeQuietly(writer);
	
	}

	private void prepareParameter(final Map<String, Object> model, final HttpServletRequest request) {
		model.put("contentType", this.getContentType());
		model.put("ctx", request.getContextPath());

		Map<String, ?> attributesMap = this.getAttributesMap();
		if (MapUtils.isNotEmpty(attributesMap) && parameterMappings != null) {
			String key, paramName, paramValue = null;
			Iterator<String> iterator = attributesMap.keySet().iterator();
			while (iterator.hasNext()) {
				key = iterator.next();
				paramName = parameterMappings.getProperty(key);
				if (StringUtils.isNotEmpty(paramName)) {
					paramValue = request.getParameter(paramName);
					if (StringUtils.isNotEmpty(paramValue)) {
						model.put(key, paramValue);
					}
				}
			}
		}
	}

}
