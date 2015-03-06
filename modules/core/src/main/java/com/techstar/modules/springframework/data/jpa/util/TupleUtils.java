package com.techstar.modules.springframework.data.jpa.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public final class TupleUtils {

	public static Map<String, Object> build(Tuple tuple) {
		if (tuple == null) {
			return null;
		}

		List<TupleElement<?>> elements = tuple.getElements();
		if (CollectionUtils.isEmpty(elements)) {
			return null;
		}

		String alias = null;
		int i = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		for (TupleElement<?> element : elements) {
			alias = element.getAlias();
			if (StringUtils.isEmpty(alias)) {
				alias = "key" + i;
			}
			map.put(alias, tuple.get(element));
			i++;
		}
		return map;
	}

	public static List<Map<String, Object>> build(List<Tuple> tuples) {
		if (CollectionUtils.isEmpty(tuples)) {
			return null;
		} else {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(tuples.size());
			for (Tuple tuple : tuples) {
				list.add(build(tuple));
			}
			return list;
		}
	}
}
