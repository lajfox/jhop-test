package com.techstar.modules.mybatis.query;

import static com.techstar.modules.mybatis.util.SQLUtils.hasWhere;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class SqlQuery {

	// ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
	// ['equal','not equal', 'less', 'less or equal','greater','greater or
	// equal',
	// 'begins with','does not begin with','is in','is not in','ends with',
	// 'does not end with','contains','does not contain']
	protected static final String EQ = "eq", NE = "ne", LT = "lt", LE = "le", GT = "gt", GE = "ge", BW = "bw",
			BN = "bn", IN = "in", NI = "ni", EW = "ew", EN = "en", CN = "cn", NC = "nc", NU = "nu", NN = "nn";

	private static final Map<String, String> OPMAP = new HashMap<String, String>();
	static {

		OPMAP.put(EQ, "=");
		OPMAP.put(NE, "!=");
		OPMAP.put(LT, "<");
		OPMAP.put(LE, "<=");
		OPMAP.put(GT, ">");
		OPMAP.put(GE, ">=");
		OPMAP.put(BW, "like");
		OPMAP.put(BN, "not like");
		OPMAP.put(IN, "in");
		OPMAP.put(NI, "not in");
		OPMAP.put(EW, "like");
		OPMAP.put(EN, "not like");
		OPMAP.put(CN, "like");
		OPMAP.put(NC, "not like");
		OPMAP.put(NU, "is null");
		OPMAP.put(NN, "is not null");
	}

	private static final Map<String, Class<?>> javaTypeMap = new HashMap<String, Class<?>>();
	static {
		javaTypeMap.put("int", Integer.class);
		javaTypeMap.put("integer", Integer.class);
		javaTypeMap.put("short", Short.class);
		javaTypeMap.put("long", Long.class);
		javaTypeMap.put("float", Float.class);
		javaTypeMap.put("double", Double.class);
		javaTypeMap.put("number", Number.class);
		javaTypeMap.put("date", Date.class);
		javaTypeMap.put("datetime", Date.class);
		javaTypeMap.put("string", String.class);
	}

	/**
	 * 取得字段的数据类型相对应的Java Type
	 * 
	 * @param dataTypeMap
	 * @param property
	 * @return
	 */
	protected final Class<?> getJavaType(Map<String, String> dataTypeMap, final String property) {
		String type = StringUtils.defaultString(getDataType(dataTypeMap, property), "string");
		return javaTypeMap.get(type);
	}

	/**
	 * 取得字段的数据类型
	 * 
	 * @param dataTypeMap
	 * @param property
	 * @return
	 */
	protected final String getDataType(Map<String, String> dataTypeMap, final String property) {
		return MapUtils.isEmpty(dataTypeMap) ? null : dataTypeMap.get(property);
	}

	protected final String getSqlOper(final String oper) {
		return OPMAP.get(oper);
	}

	protected final void where(final StringBuilder sbSql) {
		if (hasWhere(sbSql.toString())) {
			sbSql.append(" and ");
		} else {
			sbSql.append(" where ");
		}
	}

	protected final void condition(final StringBuilder sbSql, final String field, final String op) {
		if (StringUtils.equals(NU, op)) {
			sbSql.append(field).append(" is null ");
		} else if (StringUtils.equals(NN, op)) {
			sbSql.append(field).append(" is not null ");
		} else if (StringUtils.equals(IN, op) || StringUtils.equals(NI, op)) {
			sbSql.append(field).append(" ");
			sbSql.append(getSqlOper(op)).append(" ( ?");
		} else {
			sbSql.append(field).append(" ");
			sbSql.append(getSqlOper(op)).append(" ?");
		}
	}

	/**
	 * 如查是like条件，给值前后增加%
	 * 
	 * @param oper
	 * @param value
	 * @return
	 */
	protected final String getValue(final String oper, final String value, Class<?> javaType) {

		if (StringUtils.equals(oper, CN) || StringUtils.equals(oper, NC)) {
			// 包含于、不包含于
			stringType(javaType);
			return "%" + value + "%";
		} else if (StringUtils.equals(oper, BW) || StringUtils.equals(oper, BN)) {
			// 开始于、不开始于
			stringType(javaType);
			return value + "%";
		} else if (StringUtils.equals(oper, EW) || StringUtils.equals(oper, EN)) {
			// 结束于、不结束于
			stringType(javaType);
			return "%" + value;
		} else {
			return value;
		}
	}

	protected final boolean isNotNNAndNu(final String oper) {
		return !StringUtils.equals(NU, oper) && !StringUtils.equals(NN, oper);
	}

	private final void stringType(Class<?> javaType) {
		if (!String.class.isAssignableFrom(javaType)) {
			throw new RuntimeException("like 条件必须为String类型，实际类型为：" + javaType.getName());
		}
	}
}
