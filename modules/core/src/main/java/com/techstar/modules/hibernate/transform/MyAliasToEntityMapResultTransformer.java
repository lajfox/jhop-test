package com.techstar.modules.hibernate.transform;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.BasicTransformerAdapter;

public class MyAliasToEntityMapResultTransformer extends BasicTransformerAdapter implements Serializable {

	private static final long serialVersionUID = -5024846299181250179L;

	private MyAliasToEntityMapResultTransformer() {
	}

	public Object transformTuple(Object tuple[], String aliases[]) {

		if (ArrayUtils.isNotEmpty(tuple) && tuple.length == 1 && ArrayUtils.isNotEmpty(aliases) && aliases.length == 1) {
			return tuple[0];
		}

		StringBuilder sb = null;
		String as[] = null;
		int j = 0;
		Map<String, Object> result = new HashMap<String, Object>(tuple.length);
		for (int i = 0; i < tuple.length; i++) {
			String alias = aliases[i];
			if (StringUtils.isNotEmpty(alias)) {
				if (alias.endsWith("_")) {
					alias = alias.substring(0, alias.length() - 1);
				}

				alias = alias.toLowerCase();
				if (StringUtils.contains(alias, "_")) {
					as = alias.split("_");
					sb = new StringBuilder();
					j = 0;
					for (String a : as) {
						sb.append(j == 0 ? a : StringUtils.capitalize(a));
						j++;
					}
					alias = sb.toString();
				}

				result.put(alias, tuple[i]);
			}
		}

		return MapUtils.isEmpty(result) ? tuple : result;
	}

	private Object readResolve() {
		return INSTANCE;
	}

	public static final MyAliasToEntityMapResultTransformer INSTANCE = new MyAliasToEntityMapResultTransformer();

}
