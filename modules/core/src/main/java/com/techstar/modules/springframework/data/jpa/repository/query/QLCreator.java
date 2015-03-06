package com.techstar.modules.springframework.data.jpa.repository.query;

import static com.techstar.modules.mybatis.util.SQLUtils.getAfterGroupByInsertPoint;
import static com.techstar.modules.mybatis.util.SQLUtils.getMaxParamIndex;
import static com.techstar.modules.mybatis.util.SQLUtils.getOrders;
import static com.techstar.modules.mybatis.util.SQLUtils.hasGroupByORDistinct;
import static com.techstar.modules.mybatis.util.SQLUtils.hasOrderBy;
import static com.techstar.modules.mybatis.util.SQLUtils.removeOrders;
import static com.techstar.modules.springframework.data.jpa.repository.query.MyQueryUtils.createCountQueryFor;
import static org.springframework.data.jpa.repository.query.QueryUtils.detectAlias;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.techstar.modules.mybatis.query.SqlQuery;
import com.techstar.modules.mybatis.util.SQLUtils;
import com.techstar.modules.springframework.data.jpa.domain.Filters;
import com.techstar.modules.springframework.data.jpa.domain.QLSpecification;
import com.techstar.modules.springframework.data.jpa.domain.Rule;
import com.techstar.modules.springframework.data.jpa.domain.SingleSearch;

public abstract class QLCreator<T> extends SqlQuery {

	protected static final Logger logger = LoggerFactory.getLogger(QLCreator.class);

	private QLSpecification spec;
	private String qlString;
	private StringBuilder sbql;
	private boolean isNativeQuery;
	private String countQuery;

	protected StringBuilder getSbql() {
		return sbql;
	}

	protected void setSpec(QLSpecification spec) {
		this.spec = spec;
	}

	protected void setQlString(String qlString) {
		this.qlString = qlString;
	}

	public void setNativeQuery(boolean isNativeQuery) {
		this.isNativeQuery = isNativeQuery;
	}

	public void setCountQuery(String countQuery) {
		this.countQuery = countQuery;
	}

	public static interface Builder<T> {
		QLCreator<T> build();
	}

	protected void build() {

		Assert.hasText(qlString);

		String ql = SQLUtils.getLineSql(qlString);
		String orders = getOrders(ql);// 获取sql中的order by语句
		ql = removeOrders(ql);// 删除sql中的order by语句

		String groupbys = null;
		int idx = getAfterGroupByInsertPoint(ql);
		if (idx > 0) {
			groupbys = ql.substring(idx);
			ql = ql.substring(0, idx);
		}
		sbql = new StringBuilder(ql.length() + 100);
		sbql.append(ql);

		toolbarSearch(sbql);
		singleSerach(sbql);
		advancedSearch(sbql);
		defSearch(sbql);

		addGroupBys(sbql, groupbys);
		addOrders(sbql, orders);
	}

	/**
	 * 工具栏搜索
	 * 
	 * @param spec
	 * @param params
	 *            参数
	 * @param sbql
	 *            sql
	 */
	private void toolbarSearch(final StringBuilder sbql) {

		Assert.notNull(spec);
		Assert.notNull(sbql);

		Map<String, String> map = spec.getToolbarSearchMap();
		if (MapUtils.isNotEmpty(map)
				&& (spec.getFilters() == null || CollectionUtils.isEmpty(spec.getFilters().getRules()))) {

			where(sbql);
			int index = getMaxParamIndex(sbql.toString());

			Map<String, String> dataTypeMap = spec.getDataTypeMap();
			String op = EQ;// 相等
			int i = 0;
			Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String value = entry.getValue();
				if (StringUtils.isNotEmpty(value)) {
					if (i != 0) {
						sbql.append(" and ");
					}

					Class<?> javaType = this.getJavaType(dataTypeMap, entry.getKey());
					if (String.class.isAssignableFrom(javaType)) {
						op = CN;// 包含于
					} else {
						op = EQ;// 相等
					}

					index = buildSql(dataTypeMap, op, entry.getKey(), value, index);

					i++;
				}
			}

			logger.info("sbql={}", sbql);
		}
	}

	/**
	 * 单条件搜索
	 * 
	 * @param spec
	 * @param params
	 * @param sbql
	 */
	private void singleSerach(final StringBuilder sbql) {

		Assert.notNull(spec);
		Assert.notNull(sbql);

		// 单条件搜索
		if (spec.hasSingle()) {

			SingleSearch single = spec.getSingleSearch();
			Map<String, String> dataTypeMap = spec.getDataTypeMap();
			Assert.notEmpty(dataTypeMap, "没有设置datatype参数");

			where(sbql);

			int index = getMaxParamIndex(sbql.toString());

			buildSql(dataTypeMap, single.getSearchOper(), single.getSearchField(), single.getSearchString(), index);

			logger.info("sbql={}", sbql);
		}

	}

	/**
	 * 高级查询
	 * 
	 * @param spec
	 * @param params
	 * @param sbql
	 */
	private void advancedSearch(final StringBuilder sbql) {

		Assert.notNull(spec);
		Assert.notNull(sbql);

		Filters filters = spec.getFilters();
		if (filters != null && CollectionUtils.isNotEmpty(filters.getRules())) {

			Map<String, String> dataTypeMap = spec.getDataTypeMap();
			Assert.notEmpty(dataTypeMap, "没有设置datatype参数");

			where(sbql);

			int index = getMaxParamIndex(sbql.toString());

			sbql.append(" ( ");
			advancedSearch(sbql, dataTypeMap, filters, index);
			sbql.append(" ) ");
		}

		logger.info("sbql={}", sbql);
	}
	
	/**
	 * 自定义查询
	 * 
	 * @param spec
	 * @param params
	 * @param sbql
	 */
	private void defSearch(final StringBuilder sbql) {

		Assert.notNull(spec);
		Assert.notNull(sbql);

		Filters filters = spec.getDefFilters();
		if (filters != null && CollectionUtils.isNotEmpty(filters.getRules())) {

			Map<String, String> dataTypeMap = spec.getDataTypeMap();
			Assert.notEmpty(dataTypeMap, "没有设置datatype参数");

			where(sbql);

			int index = getMaxParamIndex(sbql.toString());

			sbql.append(" ( ");
			advancedSearch(sbql, dataTypeMap, filters, index);
			sbql.append(" ) ");
		}

		logger.info("sbql={}", sbql);
	}	

	private void advancedSearch(final StringBuilder sbql, final Map<String, String> dataTypeMap, final Filters filters,
			final int index) {

		Assert.notNull(spec);
		Assert.notNull(sbql);

		if (filters != null) {
			int idx = index;
			List<Rule> rules = filters.getRules();
			if (CollectionUtils.isNotEmpty(rules)) {
				int i = 0;

				for (Rule rule : rules) {

					if (i != 0) {
						sbql.append(" ").append(filters.getGroupOp()).append(" ");
					}

					idx = buildSql(dataTypeMap, rule.getOp(), rule.getField(), rule.getData(), idx);

					i++;
				}

			}

			List<Filters> groups = filters.getGroups();
			if (CollectionUtils.isNotEmpty(groups)) {
				for (Filters fs : groups) {
					sbql.append(" ").append(filters.getGroupOp()).append(" (");
					advancedSearch(sbql, dataTypeMap, fs, idx);
					sbql.append(") ");
				}
			}
		}
	}

	public String getQuery() {
		return getSbql().toString();
	}

	public String getCountQuery() {
		if (StringUtils.isEmpty(countQuery)) {
			// 如果select中包含order by删之
			String lineSql = getQuery();
			if (hasOrderBy(lineSql)) {
				lineSql = removeOrders(lineSql);
			}

			String countSql = createCountQueryFor(lineSql);
			if (isNativeQuery) {
				if (!hasGroupByORDistinct(qlString)) {
					countSql = countSql.replaceFirst("\\(" + detectAlias(qlString) + "\\)", "(1) as _count_");
				}
			}
			return countSql;
		} else {
			return getQuery();
		}
	}

	public abstract T getValues();

	protected abstract int buildSql(final Map<String, String> dataTypeMap, final String op, final String field,
			final String value, final int index);

	/**
	 * 追加sql排序
	 * 
	 * @param originalSql
	 * @param orders
	 * @param myRowBounds
	 * @return
	 */
	private void addOrders(final StringBuilder sbql, final String orders) {
		if (StringUtils.isNotEmpty(orders)) {
			sbql.append(" ");
			sbql.append(orders);
		}

	}

	/**
	 * 追加sql group by
	 * 
	 * @param originalSql
	 * @param orders
	 * @param myRowBounds
	 * @return
	 */
	private void addGroupBys(final StringBuilder sbql, final String groupbys) {
		if (StringUtils.isNotEmpty(groupbys)) {
			sbql.append(" ");
			sbql.append(groupbys);
		}

	}

}
