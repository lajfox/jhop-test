package com.techstar.modules.mybatis.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

public final class SQLUtils {

	private static final Pattern DISTINCTPATTERN = Pattern.compile("\\s+distinct\\s+", Pattern.CASE_INSENSITIVE);

	private static final Pattern ORDERPATTERN = Pattern.compile("order\\s+by[\\w|\\W|\\s|\\S]*",
			Pattern.CASE_INSENSITIVE);

	private static final Pattern FROMPATTERN = Pattern.compile("\\s+from\\s+", Pattern.CASE_INSENSITIVE);
	private static final Pattern WHEREPATTERN = Pattern.compile("\\s+where\\s+", Pattern.CASE_INSENSITIVE);
	private static final Pattern PARAMPATTERN = Pattern.compile("(\\?{1})([\\d]*)", Pattern.CASE_INSENSITIVE);
	private static final Pattern GROUPPATTERN = Pattern.compile("\\s+group\\s+by\\s+", Pattern.CASE_INSENSITIVE);

	public static String getCountString(final String sql) {
		Assert.hasText(sql);

		String lineSql = getLineSql(sql);
		int formIndex = getAfterFormInsertPoint(lineSql);

		// 如果select中包含order by删之
		if (hasOrderBy(lineSql)) {
			lineSql = removeOrders(lineSql);
		}

		// 如果SELECT 中包含 DISTINCT 只能在外层包含COUNT
		if (hasGroupByORDistinct(lineSql)) {
			return new StringBuffer(lineSql.length() + 20).append("select count(1) coun from (").append(lineSql)
					.append(" ) t").toString();
		} else {
			return new StringBuffer(lineSql.length() + 20).append("select count(1) coun ")
					.append(lineSql.substring(formIndex)).toString();
		}
	}

	/**
	 * 将SQL语句变成一条语句，并且每个单词的间隔都是1个空格
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 如果sql是NULL返回空，否则返回转化后的SQL
	 */
	public static String getLineSql(String sql) {
		return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
	}

	/**
	 * 得到最后一个Order By的插入点位置
	 * 
	 * @return 返回最后一个Order By插入点的位置
	 */
	public static int getLastOrderInsertPoint(final String sql) {
		int orderIndex = sql.toLowerCase().lastIndexOf("order by");
		return orderIndex;
	}

	/**
	 * 得到SQL第一个正确的FROM的的插入点
	 */
	public static int getAfterFormInsertPoint(final String sql) {

		Matcher matcher = FROMPATTERN.matcher(sql);
		while (matcher.find()) {
			int fromStartIndex = matcher.start(0);
			String text = sql.substring(0, fromStartIndex);
			if (isBracketCanPartnership(text)) {
				return fromStartIndex;
			}
		}
		return 0;
	}

	/**
	 * 得到SQL第一个正确的where的的插入点
	 */
	public static int getAfterWhereInsertPoint(final String sql) {

		Matcher matcher = WHEREPATTERN.matcher(sql);
		while (matcher.find()) {
			int fromStartIndex = matcher.start(0);
			String text = sql.substring(0, fromStartIndex);
			if (isBracketCanPartnership(text)) {
				return fromStartIndex;
			}
		}
		return 0;
	}

	public static boolean hasWhere(final String sql) {
		return getAfterWhereInsertPoint(sql) > 0;
	}

	/**
	 * 得到SQL第一个正确的group by 的的插入点
	 */
	public static int getAfterGroupByInsertPoint(final String sql) {

		Matcher matcher = GROUPPATTERN.matcher(sql);
		while (matcher.find()) {
			int fromStartIndex = matcher.start(0);
			String text = sql.substring(0, fromStartIndex);
			if (isBracketCanPartnership(text)) {
				return fromStartIndex;
			}
		}
		return 0;
	}

	public static boolean hasGroupBy(final String sql) {
		return getAfterGroupByInsertPoint(sql) > 0;
	}

	/**
	 * 得到SQL第一个正确的distinc的的插入点
	 */
	public static int getAfterDistinctInsertPoint(final String sql) {

		Matcher matcher = DISTINCTPATTERN.matcher(sql);
		while (matcher.find()) {
			int fromStartIndex = matcher.start(0);
			String text = sql.substring(0, fromStartIndex);
			if (isBracketCanPartnership(text)) {
				return fromStartIndex;
			}
		}
		return 0;
	}

	public static boolean hasDistinct(final String sql) {
		return getAfterDistinctInsertPoint(sql) > 0;
	}

	public static boolean hasGroupByORDistinct(final String sql) {
		return hasGroupBy(sql) || hasDistinct(sql);
	}

	/**
	 * 判断括号"()"是否匹配,并不会判断排列顺序是否正确
	 * 
	 * @param text
	 *            要判断的文本
	 * @return 如果匹配返回TRUE,否则返回FALSE
	 */
	public static boolean isBracketCanPartnership(final String text) {
		if (text == null || (getIndexOfCount(text, '(') != getIndexOfCount(text, ')'))) {
			return false;
		}
		return true;
	}

	/**
	 * 得到一个字符在另一个字符串中出现的次数
	 * 
	 * @param text
	 *            文本
	 * @param ch
	 *            字符
	 */
	public static int getIndexOfCount(String text, char ch) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			count = (text.charAt(i) == ch) ? count + 1 : count;
		}
		return count;
	}

	public static boolean hasOrderBy(final String sql) {
		Matcher m = ORDERPATTERN.matcher(sql);
		return m.find();
	}

	public static boolean hasParam(final String sql) {
		Matcher m = PARAMPATTERN.matcher(sql);
		return m.find();
	}

	public static int getMaxParamIndex(final String sql) {

		int index = 0;
		Matcher m = PARAMPATTERN.matcher(sql);
		while (m.find()) {
			String idx = m.group(2);
			if (StringUtils.isNotEmpty(idx) && NumberUtils.toInt(idx) > index) {
				index = NumberUtils.toInt(idx);
			}
		}

		return index;
	}

	public static String getOrders(final String sql) {
		String orders = null;
		Matcher m = ORDERPATTERN.matcher(sql);
		while (m.find()) {
			orders = m.group();
		}
		return orders;
	}

	public static String removeOrders(final String sql) {
		Matcher m = ORDERPATTERN.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
