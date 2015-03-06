package com.techstar.modules.springframework.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.springframework.data.jpa.domain.Filters;
import com.techstar.modules.springframework.data.jpa.domain.Page;
import com.techstar.modules.springframework.data.jpa.domain.QLSpecification;
import com.techstar.modules.springframework.data.jpa.domain.SingleSearch;

/**
 * 
 * 
 * @author sundoctor
 * 
 */

public class JqgridQLSpecificationArgumentResolver implements WebArgumentResolver {

	private static final JsonMapper BINDER = JsonMapper.nonDefaultMapper();

	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

		if (methodParameter.getParameterType().equals(QLSpecification.class)) {

			QLSpecification spec = new QLSpecification();
			ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);

			Page page = new Page();
			WebRequestDataBinder binder = new WebRequestDataBinder(page);
			binder.bind(webRequest);

			spec.setPage(page);

			// 数据类型，以下查询需要知道字段的数据类型
			String datatype = servletRequest.getParameter("sorttypes");
			if (StringUtils.isNotEmpty(datatype)) {
				@SuppressWarnings("unchecked")
				Map<String, String> dataTypeMap = BINDER.fromJson(datatype, Map.class);
				spec.setDataTypeMap(dataTypeMap);
			}

			// 单条件查询
			SingleSearch singleSearch = new SingleSearch();
			binder = new WebRequestDataBinder(singleSearch);
			binder.bind(webRequest);
			spec.setSingleSearch(singleSearch);

			// 高级搜索multipleSearch:true
			String filters = servletRequest.getParameter("filters");
			Filters filter = this.fromJson(filters);
			spec.setFilters(filter);

			// 集成工具栏搜索
			String fields = servletRequest.getParameter("searchfields");
			if (StringUtils.isNotEmpty(fields)) {
				String fieldss[] = fields.split(",");
				Map<String, String> map = new HashMap<String, String>();
				for (String param : fieldss) {
					String value = servletRequest.getParameter(param);
					if (StringUtils.isNotEmpty(StringUtils.trim(value))) {
						map.put(param.trim(), value);
					}
				}
				spec.setToolbarSearchMap(map);
			}

			// 是否分页
			String pagination = servletRequest.getParameter("pagination");
			if (StringUtils.isNotEmpty(pagination)) {
				spec.setPagination(BooleanUtils.toBoolean(pagination));
			}

			// 是否自动查询总记录数
			String autoCount = servletRequest.getParameter("autoCount");
			if (StringUtils.isNotEmpty(autoCount)) {
				spec.setPagination(BooleanUtils.toBoolean(autoCount));
			}

			return spec;
		}

		return UNRESOLVED;
	}

	private Filters fromJson(final String filters) {
		if (StringUtils.isEmpty(filters)) {
			return null;
		} else {
			return BINDER.fromJson(filters, Filters.class);
		}
	}

}
