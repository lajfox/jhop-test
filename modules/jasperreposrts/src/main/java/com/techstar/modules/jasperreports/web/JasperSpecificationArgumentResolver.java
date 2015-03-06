package com.techstar.modules.jasperreports.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.techstar.modules.commons.beanutils.ConvertUtils;
import com.techstar.modules.jasperreports.PropertyType;
import com.techstar.modules.jasperreports.domain.Specification;
import com.techstar.modules.jasperreports.web.servlet.support.RequestContext;
import com.techstar.modules.springframework.web.util.WebUtils;

public class JasperSpecificationArgumentResolver implements WebArgumentResolver {

	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

		if (methodParameter.getParameterType().equals(Specification.class)) {

			ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);
			String jrurl = servletRequest.getParameter(RequestContext.URLKEY);
			Assert.isTrue(StringUtils.isNotEmpty(jrurl), "参数错误，参数:" + RequestContext.URLKEY + "不能为空！");

			return new MySpecification(servletRequest);
		}

		return UNRESOLVED;
	}

	private static class MySpecification implements Specification {

		private ServletRequest request;

		public MySpecification(ServletRequest request) {
			this.request = request;
		}

		@Override
		public Map<String, Object> getModel() {
			Map<String, Object> model = new HashMap<String, Object>();
			processJR(request, model);
			processFilter(request, model);
			return model;
		}

		private void processJR(final ServletRequest servletRequest, final Map<String, Object> model) {

			Map<String, Object> jrMap = WebUtils.getParametersStartingWith(servletRequest, RequestContext.JRKEY);
			Assert.notEmpty(jrMap, "参数错误，" + RequestContext.JRKEY + "参数不能为空！");

			Object value = null;
			Entry<String, Object> entry = null;
			Iterator<Entry<String, Object>> iterator = jrMap.entrySet().iterator();
			while (iterator.hasNext()) {
				entry = iterator.next();
				value = entry.getValue();
				if (value != null) {
					model.put(RequestContext.JRKEY + entry.getKey(), value);
				}
			}

		}

		private void processFilter(final ServletRequest servletRequest, final Map<String, Object> model) {

			Map<String, Object> map = WebUtils.getParametersStartingWith(servletRequest, "filter_");
			if (MapUtils.isNotEmpty(map)) {
				String key, propertyTypeCode, propertyName = null;
				Object value = null;
				Object vals[] = null;
				String strs[] = null;
				Entry<String, Object> entry = null;
				Class<?> clazz = null;
				Iterator<Entry<String, Object>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					entry = it.next();
					key = entry.getKey();
					value = entry.getValue();
					if (value != null) {
						propertyTypeCode = StringUtils.substringBefore(key, "_");
						propertyName = StringUtils.substringAfter(key, "_");

						Assert.isTrue(StringUtils.isNotEmpty(propertyTypeCode) && StringUtils.isNotEmpty(propertyName),
								"filter名称" + key + "没有按规则编写,无法得到属性名称.");

						clazz = Enum.valueOf(PropertyType.class, propertyTypeCode).getValue();
						if (value.getClass().isArray()) {
							strs = (String[]) value;
							vals = new Object[strs.length];
							int i = 0;
							for (String str : strs) {
								vals[i] = ConvertUtils.convert2(str, clazz);
							}
							model.put(propertyName, vals);
						} else {
							model.put(propertyName, ConvertUtils.convert2(value.toString(), clazz));
						}
					}
				}
			}
		}

	}
}
