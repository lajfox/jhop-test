package com.techstar.modules.springframework.web;

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
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.springframework.data.jpa.domain.Filters;
import com.techstar.modules.springframework.data.jpa.domain.QLSpecification;
import com.techstar.modules.springframework.data.jpa.domain.QuerySpecification;
import com.techstar.modules.springframework.data.jpa.domain.Rule;
import com.techstar.modules.springframework.data.jpa.domain.SingleSearch;
import com.techstar.modules.springframework.data.jpa.repository.query.ArrayQLCreator;
import com.techstar.modules.springframework.data.jpa.repository.query.MapQLCreator;
import com.techstar.modules.springframework.data.jpa.repository.query.QLCreator;
import com.techstar.modules.springframework.web.util.WebUtils;

/**
 * JPQL、SQL分页查询／查询WebArgumentResolver，拦截QuerySpecification参数
 * 
 * @author sundoctor
 * 
 */

public class JqgridQuerySpecificationArgumentResolver extends QLWebArgumentResolver {

	private static final JsonMapper BINDER = JsonMapper.nonDefaultMapper();

	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

		if (methodParameter.getParameterType().equals(QuerySpecification.class)) {

			QLSpecification spec = new QLSpecification();
			ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);

			// 数据类型，以下查询需要知道字段的数据类型
			String datatype = servletRequest.getParameter("sorttypes");
			if (StringUtils.isNotEmpty(datatype)) {
				@SuppressWarnings("unchecked")
				Map<String, String> dataTypeMap = BINDER.fromJson(datatype, Map.class);
				spec.setDataTypeMap(dataTypeMap);
			}

			// 单条件查询
			SingleSearch singleSearch = new SingleSearch();
			WebRequestDataBinder binder = new WebRequestDataBinder(singleSearch);
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

				spec.setDefFilters(filter);
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

			return new MyQuerySpecification(spec);
		}

		return UNRESOLVED;
	}

	private static class MyQuerySpecification implements QuerySpecification {

		private QLSpecification spec;

		public MyQuerySpecification(QLSpecification spec) {
			this.spec = spec;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> QLCreator<T> build(final boolean isNativeQuery, final boolean isNamedParameter,
				final String countQuery, final String queryString) {
			if (isNamedParameter) {
				QLCreator.Builder<Map<String, Object>> builder = new MapQLCreator.Builder(isNativeQuery, countQuery,
						spec, queryString);
				return (QLCreator<T>) builder.build();
			} else {
				QLCreator.Builder<Object[]> builder = new ArrayQLCreator.Builder(isNativeQuery, countQuery, spec,
						queryString);
				return (QLCreator<T>) builder.build();
			}
		}

	}

	private Filters fromJson(final String filters) {
		if (StringUtils.isEmpty(filters)) {
			return null;
		} else {
			return BINDER.fromJson(filters, Filters.class);
		}
	}

}
