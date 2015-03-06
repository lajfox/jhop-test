package com.techstar.modules.springframework.data.jpa.repository.query;

import static com.techstar.modules.commons.beanutils.ConvertUtils.convert2;
import static com.techstar.modules.commons.beanutils.ConvertUtils.converts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.techstar.modules.springframework.data.jpa.domain.QLSpecification;

public class ArrayQLCreator extends QLCreator<Object[]> {

	private static final Object[] emptys = new Object[] {};
	private List<Object> values;

	private ArrayQLCreator() {

	}

	public static class Builder implements QLCreator.Builder<Object[]> {
		private ArrayQLCreator creator = new ArrayQLCreator();

		public Builder(boolean isNativeQuery, String countQuery, QLSpecification spec, String qlString) {
			this(isNativeQuery, countQuery, spec, qlString, null);
		}

		public Builder(boolean isNativeQuery, String countQuery, QLSpecification spec, String qlString, Object[] values) {
			creator.setSpec(spec);
			creator.setQlString(qlString);
			creator.setCountQuery(countQuery);
			creator.setNativeQuery(isNativeQuery);

			creator.values = new ArrayList<Object>();
			if (ArrayUtils.isNotEmpty(values)) {
				for (Object value : values) {
					creator.values.add(value);
				}
			}

		}

		@Override
		public QLCreator<Object[]> build() {
			creator.build();
			return creator;
		}
	}

	@Override
	public Object[] getValues() {
		return CollectionUtils.isEmpty(values) ? emptys : values.toArray(new Object[values.size()]);
	}

	@Override
	protected int buildSql(final Map<String, String> dataTypeMap, final String op, final String field,
			final String value, final int index) {

		Assert.notNull(values);

		condition(getSbql(), field, op);

		int idx = index;
		if (isNotNNAndNu(op)) {
			idx = index + 1;
			getSbql().append(idx);

			if (StringUtils.equals(IN, op) || StringUtils.equals(NI, op)) {
				getSbql().append(")");
			}
			
			getSbql().append(" ");

			Class<?> javaType = getJavaType(dataTypeMap, field);
			Assert.notNull(javaType, "设置的datatype参数里没有找到" + field + "属性");

			// 设置参数
			if (StringUtils.equals(IN, op) || StringUtils.equals(NI, op)) {
				Object[] objs = converts(value, javaType);
				values.add(Arrays.asList(objs));
			} else {
				values.add(convert2(getValue(op, value, javaType), javaType));
			}
		}

		return idx;
	}

}
