package com.techstar.modules.springframework.data.jpa.repository.query;

import static com.techstar.modules.commons.beanutils.ConvertUtils.convert2;
import static com.techstar.modules.commons.beanutils.ConvertUtils.converts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.techstar.modules.springframework.data.jpa.domain.QLSpecification;

public final class MapQLCreator extends QLCreator<Map<String, Object>> {

	private Map<String, Object> values;

	private MapQLCreator() {

	}

	public static class Builder implements QLCreator.Builder<Map<String, Object>> {
		private MapQLCreator creator = new MapQLCreator();

		public Builder(boolean isNativeQuery, String countQuery, QLSpecification spec, String qlString) {
			this(isNativeQuery, countQuery, spec, qlString, null);
		}

		public Builder(boolean isNativeQuery, String countQuery, QLSpecification spec, String qlString,
				Map<String, Object> values) {
			creator.setSpec(spec);
			creator.setQlString(qlString);
			creator.setCountQuery(countQuery);
			creator.setNativeQuery(isNativeQuery);

			creator.values = MapUtils.isEmpty(values) ? new HashMap<String, Object>() : values;
		}

		public QLCreator<Map<String, Object>> build() {
			creator.build();
			return creator;
		}
	}

	@Override
	public Map<String, Object> getValues() {
		return this.values;
	}

	@Override
	protected final int buildSql(final Map<String, String> dataTypeMap, final String op, final String field,
			final String value, final int index) {

		Assert.notNull(values);

		condition(getSbql(), field, op);

		int idx = index;
		if (isNotNNAndNu(op)) {

			idx = index + 1;
			String params = "params" + idx;
			logger.info("params={}", params);

			getSbql().deleteCharAt(getSbql().length() - 1);
			getSbql().append(":").append(params);

			if (StringUtils.equals(IN, op) || StringUtils.equals(NI, op)) {
				getSbql().append(")");
			}

			getSbql().append(" ");

			Class<?> javaType = getJavaType(dataTypeMap, field);
			Assert.notNull(javaType, "设置的datatype参数里没有找到" + field + "属性");

			// 设置参数
			if (StringUtils.equals(IN, op) || StringUtils.equals(NI, op)) {
				Object[] objs = converts(value, javaType);
				values.put(params, Arrays.asList(objs));
			} else {
				values.put(params, convert2(getValue(op, value, javaType), javaType));
			}
		}

		return idx;

	}

}
