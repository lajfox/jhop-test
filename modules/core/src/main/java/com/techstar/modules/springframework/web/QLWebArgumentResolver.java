package com.techstar.modules.springframework.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.support.WebArgumentResolver;

public abstract class QLWebArgumentResolver implements WebArgumentResolver {

	private static final Map<String, String> OPMAP = new HashMap<String, String>();
	private static final String[] OPI = new String[] { "Is", "Equals", "Not", "IsNot", "LessThan", "IsLessThan",
			"LessThanEqual", "IsLessThanEqual", "GreaterThan", "IsGreaterThan", "GreaterThanEqual",
			"IsGreaterThanEqual", "Before", "IsBefore", "After", "IsAfter", "Like", "NotLike", "StartingWith",
			"StartsWith", "IsStartingWith", "EndingWith", "IsEndingWith", "EndsWith", "IsContaining", "Containing",
			"Contains", "Null", "IsNull", "NotNull", "IsNotNull","In","IsIn","NotIn","IsNotIn" };
	static {
		// ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
		// ['equal','not equal', 'less', 'less or equal','greater','greater or
		// equal',
		// 'begins with','does not begin with','is in','is not in','ends with',
		// 'does not end with','contains','does not contain']

		OPMAP.put("Is", "eq");
		OPMAP.put("Equals", "eq");

		OPMAP.put("Not", "ne");
		OPMAP.put("IsNot", "ne");

		OPMAP.put("LessThan", "lt");
		OPMAP.put("IsLessThan", "lt");

		OPMAP.put("LessThanEqual", "le");
		OPMAP.put("IsLessThanEqual", "le");

		OPMAP.put("GreaterThan", "gt");
		OPMAP.put("IsGreaterThan", "gt");

		OPMAP.put("GreaterThanEqual", "ge");
		OPMAP.put("IsGreaterThanEqual", "ge");

		OPMAP.put("Before", "bw");
		OPMAP.put("IsBefore", "bw");

		OPMAP.put("After", "ew");
		OPMAP.put("IsAfter", "ew");

		OPMAP.put("Like", "cn");
		OPMAP.put("NotLike", "nc");

		OPMAP.put("StartingWith", "bw");
		OPMAP.put("StartsWith", "bw");
		OPMAP.put("IsStartingWith", "bw");

		OPMAP.put("EndingWith", "ew");
		OPMAP.put("IsEndingWith", "ew");
		OPMAP.put("EndsWith", "ew");

		OPMAP.put("IsContaining", "cn");
		OPMAP.put("Containing", "cn");
		OPMAP.put("Contains", "cn");

		OPMAP.put("In", "in");
		OPMAP.put("IsIn", "in");

		OPMAP.put("NotIn", "ni");
		OPMAP.put("IsNotIn", "ni");

		OPMAP.put("Null", "nu");
		OPMAP.put("IsNull", "nu");

		OPMAP.put("NotNull", "nn");
		OPMAP.put("IsNotNull", "nn");

	}

	protected String[] getFieldAndOp(final String key) {
		String resutls[] = new String[] { null, "eq" };

		String next = null;
		for (String str : OPI) {
			if (key.endsWith(str)) {
				next = str;
				break;
			}
		}
		if (StringUtils.isEmpty(next)) {
			resutls[0] = key;
		} else {
			int idx = key.indexOf(next);

			resutls[0] = key.substring(0, idx);
			resutls[1] = key.substring(idx);
			if (StringUtils.isEmpty(resutls[1])) {
				resutls[1] = "eq";
			} else {
				resutls[1] = OPMAP.get(resutls[1]);
			}
		}

		return resutls;
	}
}
