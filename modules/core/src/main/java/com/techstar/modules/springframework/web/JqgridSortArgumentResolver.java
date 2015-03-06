package com.techstar.modules.springframework.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;

import com.techstar.modules.springframework.data.jpa.domain.Page;

public class JqgridSortArgumentResolver implements WebArgumentResolver {

	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {

		if (methodParameter.getParameterType().equals(Sort.class)) {

			Page page = new Page();
			WebRequestDataBinder binder = new WebRequestDataBinder(page);
			binder.bind(webRequest);

			Sort so = null;
			if (StringUtils.isNotEmpty(page.getSidx()) && StringUtils.isNotEmpty(page.getSord())) {
				if (StringUtils.contains(page.getSidx(), ",")) {
					String[] sidxs = page.getSidx().split(",");
					int i = 0;
					Order[] orders = new Order[sidxs.length];
					for (String str : sidxs) {
						if (StringUtils.contains(str, "asc") || StringUtils.contains(str, "desc")) {
							String[] strs = str.trim().split(" ");
							orders[i] = buildOrder(strs[1].trim(), strs[0].trim());
						} else {
							orders[i] = buildOrder(page.getSord(), str.trim());
						}
						i++;
					}
					so = new Sort(orders);
				} else {
					so = new Sort(buildOrder(page.getSord(), page.getSidx()));
				}
			}

			return so;
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



}
