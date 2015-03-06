package com.techstar.modules.springframework.web;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.PageableDefaults;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.mybatis.domain.MyRowBounds;
import com.techstar.modules.springframework.data.jpa.domain.Filters;
import com.techstar.modules.springframework.data.jpa.domain.Page;
import com.techstar.modules.springframework.data.jpa.domain.Rule;
import com.techstar.modules.springframework.data.jpa.domain.SingleSearch;
import com.techstar.modules.springframework.web.util.WebUtils;

/**
 * MyBaits分页／查询WebArgumentResolver，拦截MyRowBounds参数
 * 
 * @author sundoctor
 * 
 */

public class JqgridRowBoundsArgumentResolver  extends QLWebArgumentResolver {

	private static final MyRowBounds DEFAULT_ROWBOUNDS = new MyRowBounds(0, 10);
	private MyRowBounds fallbackRowBounds = DEFAULT_ROWBOUNDS;
	private static final JsonMapper BINDER = JsonMapper.nonDefaultMapper();

	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

		if (methodParameter.getParameterType().equals(MyRowBounds.class)) {

			MyRowBounds rowBounds = null;
			ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);

			Page page = new Page();
			WebRequestDataBinder binder = new WebRequestDataBinder(page);
			binder.bind(webRequest);

			if (page.getPage() <= 0) {
				// 取默认值，用于测试数据
				rowBounds = getDefaultFromAnnotationOrFallback(methodParameter);
			} else {
				rowBounds = new MyRowBounds(page.getPage() - 1, page.getRows());
			}
			rowBounds.setPage(page);

			// 数据类型，以下查询需要知道字段的数据类型
			String datatype = servletRequest.getParameter("sorttypes");
			if (StringUtils.isNotEmpty(datatype)) {
				@SuppressWarnings("unchecked")
				Map<String, String> dataTypeMap = BINDER.fromJson(datatype, Map.class);
				rowBounds.setDataTypeMap(dataTypeMap);
			}

			// 单条件查询
			SingleSearch singleSearch = new SingleSearch();
			binder = new WebRequestDataBinder(singleSearch);
			binder.bind(webRequest);
			rowBounds.setSingleSearch(singleSearch);

			// 高级搜索multipleSearch:true
			String filters = servletRequest.getParameter("filters");
			Filters filter = this.fromJson(filters);
			rowBounds.setFilters(filter);

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
				rowBounds.setToolbarSearchMap(map);
			}
			
			// 自定义查询
			Map<String, Object> defSearchMap = WebUtils.getParametersStartingWith(servletRequest, "sDs_");
			if (MapUtils.isNotEmpty(defSearchMap)) {

				filter = new Filters();
				filter.setGroupOp("AND");
				List<Rule> rules = new ArrayList<Rule>();
				filter.setRules(rules);

				String filedAndOp[] = null;
				Rule rule = null;
				Entry<String, Object> entry = null;
				Iterator<Entry<String, Object>> it = defSearchMap.entrySet().iterator();
				while (it.hasNext()) {
					entry = it.next();
					rule = new Rule();

					filedAndOp = getFieldAndOp(entry.getKey());

					rule.setData(entry.getValue() == null ? null : entry.getValue().toString());
					rule.setField(filedAndOp[0]);
					rule.setOp(filedAndOp[1]);
					rules.add(rule);
				}

				rowBounds.setDefFilters(filter);
			}			

			// 是否分页
			String pagination = servletRequest.getParameter("pagination");
			if (StringUtils.isNotEmpty(pagination)) {
				rowBounds.setPagination(BooleanUtils.toBoolean(pagination));
			}

			// 是否自动查询总记录数
			String autoCount = servletRequest.getParameter("autoCount");
			if (StringUtils.isNotEmpty(autoCount)) {
				rowBounds.setPagination(BooleanUtils.toBoolean(autoCount));
			}

			return rowBounds;
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

	private MyRowBounds getDefaultFromAnnotationOrFallback(MethodParameter methodParameter) {

		// search for PageableDefaults and MyPageableDefaults annotation
		for (Annotation annotation : methodParameter.getParameterAnnotations()) {
			if (annotation instanceof PageableDefaults) {
				PageableDefaults defaults = (PageableDefaults) annotation;
				return new MyRowBounds(defaults.pageNumber(), defaults.value());
			}
		}

		// Construct request with fallback request to ensure sensible
		// default values. Create fresh copy as Spring will manipulate the
		// instance under the covers
		return new MyRowBounds(fallbackRowBounds.getOffset(), fallbackRowBounds.getLimit());
	}

}
