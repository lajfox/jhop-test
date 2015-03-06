package com.techstar.modules.springframework.web;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.PageableDefaults;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

import com.techstar.modules.springframework.data.jpa.domain.Page;

public class JqgridPageableArgumentResolver implements WebArgumentResolver {

	private static final Pageable DEFAULT_PAGE_REQUEST = new PageRequest(0, 10);

	private Pageable fallbackPagable = DEFAULT_PAGE_REQUEST;

	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

		if (methodParameter.getParameterType().equals(Pageable.class)) {

			Page page = new Page();
			WebRequestDataBinder binder = new WebRequestDataBinder(page);
			binder.bind(webRequest);

			Pageable request = null;
			if (page.getPage() <= 0) {
				// 取默认值，用于测试数据
				request = getDefaultFromAnnotationOrFallback(methodParameter);
			} else {
				Sort so = null;
				if (StringUtils.isNotEmpty(page.getSidx()) && StringUtils.isNotEmpty(page.getSord())) {
					if (StringUtils.contains(page.getSidx(), ",")) {
						String[] sidxs = page.getSidx().split(",");
						
						List<Order> orders = new ArrayList<Order>(sidxs.length);
						for (String str : sidxs) {
							if (StringUtils.contains(str, "asc") || StringUtils.contains(str, "desc")) {
								String[] strs = str.trim().split(" ");
								if (StringUtils.isNotEmpty(strs[0].trim())) {
									orders.add( buildOrder(strs[1].trim(), strs[0].trim()));
								}
							} else {
								if (StringUtils.isNotEmpty(str.trim())) {
									orders.add( buildOrder(page.getSord(), str.trim()));
								}
							}
							
						}
						so = new Sort(orders);
					} else {
						if (StringUtils.isNotEmpty(page.getSidx())) {
							so = new Sort(buildOrder(page.getSord(), page.getSidx()));
						}
					}
				} 
				request = new PageRequest(page.getPage() - 1, page.getRows(), so);
			}

			return request;
		}

		return UNRESOLVED;
	}

	private Order buildOrder(final String sord, final String sidx) {
		if (StringUtils.equalsIgnoreCase("asc", sord)) {
			return new Order(Sort.Direction.ASC, sidx);
		} else {
			return new Order(Sort.Direction.DESC, sidx);
		}
	}



	private Pageable getDefaultFromAnnotationOrFallback(MethodParameter methodParameter) {

		// search for PageableDefaults and MyPageableDefaults annotation
		for (Annotation annotation : methodParameter.getParameterAnnotations()) {
			if (annotation instanceof PageableDefaults) {
				PageableDefaults defaults = (PageableDefaults) annotation;
				return new PageRequest(defaults.pageNumber(), defaults.value());
			}

	
		}

		// Construct request with fallback request to ensure sensible
		// default values. Create fresh copy as Spring will manipulate the
		// instance under the covers
		return new PageRequest(fallbackPagable.getPageNumber(), fallbackPagable.getPageSize(), fallbackPagable.getSort());
	}

}
