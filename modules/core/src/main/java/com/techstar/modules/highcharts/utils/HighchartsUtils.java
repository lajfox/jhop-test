package com.techstar.modules.highcharts.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.commons.beanutils.PropertyUtils;
import com.techstar.modules.highcharts.collections.GroupPredicate;
import com.techstar.modules.highcharts.domain.Category;
import com.techstar.modules.highcharts.domain.Highcharts;
import com.techstar.modules.highcharts.domain.Item;
import com.techstar.modules.highcharts.domain.Point;
import com.techstar.modules.highcharts.domain.Series;

public final class HighchartsUtils {

	private static final Highcharts EMPTYHIGHCHARTS = new Highcharts();
	static {
		EMPTYHIGHCHARTS.setSeries(new ArrayList<Series>());
		EMPTYHIGHCHARTS.setCategories(new ArrayList<String>());
	}

	/**
	 * 
	 * @param datas
	 * @param items
	 * @return
	 */
	public static <E> Highcharts convert(final Collection<E> datas, Item... items) {

		if (CollectionUtils.isNotEmpty(datas) && ArrayUtils.isNotEmpty(items)) {

			Highcharts highcharts = new Highcharts();
			List<Series> list = new ArrayList<Series>(items.length);

			List<Point> points = new ArrayList<Point>();
			Map<Point, Series> seriesMap = new HashMap<Point, Series>();
			Map<Point, List<Object>> dataMap = new HashMap<Point, List<Object>>();
			Map<Integer, String> categoriesMap = new LinkedHashMap<Integer, String>();
			String category = null;

			for (Item item : items) {
				seriesMap.put(item.getKey(), item.getSeries());
				dataMap.put(item.getKey(), new ArrayList<Object>());
				points.add(item.getKey());
			}

			Object[] objs = null;
			String color = null;
			int i = 0, j = 0;
			for (E obj : datas) {

				for (Point point : points) {
					point.valid();

					if (point.isTime()) {
						objs = new Object[] { PropertyUtils.getDate(obj, point.getProperties()[0]).getTime(),
								PropertyUtils.getProperty(obj, point.getProperties()[1]) };
						dataMap.get(point).add(objs);
					} else if (point.isCategories()) {

						category = PropertyUtils.getString(obj, point.getProperties()[0]);
						if (StringUtils.isNotEmpty(category)) {
							categoriesMap.put(i, category);
						}

						if (StringUtils.isNotEmpty(point.getColor())) {
							color = PropertyUtils.getString(obj, point.getColor());
						}

						dataMap.get(point).add(
								new Category<Object>(PropertyUtils.getProperty(obj, point.getProperties()[0]),
										PropertyUtils.getNumber(obj, point.getProperties()[1]), color));
					} else {
						j = 0;
						objs = new Object[point.getProperties().length];
						for (String property : point.getProperties()) {
							objs[j] = PropertyUtils.getProperty(obj, property);
							j++;
						}
						dataMap.get(point).add(objs);
					}
				}

				i++;

			}

			for (Point point : points) {
				seriesMap.get(point).setData(dataMap.get(point));
				list.add(seriesMap.get(point));
			}

			highcharts.setSeries(list);
			if (categoriesMap != null && categoriesMap.size() > 0) {
				highcharts.setCategories(categoriesMap.values());
			}

			return highcharts;
		} else {
			return EMPTYHIGHCHARTS;
		}

	}

	/**
	 * 
	 * @param datas
	 * @param groupKey
	 *            分组
	 * @param xkey
	 *            x轴
	 * @param yKey
	 *            y轴
	 * @return
	 */
	public static <E> Highcharts convert(final Collection<E> datas, final String groupKey, final String xkey,
			final String yKey) {
		return convert(datas, groupKey, xkey, yKey, null);

	}

	/**
	 * 
	 * @param datas
	 * @param groupKey
	 *            分组
	 * @param xkey
	 *            x轴
	 * @param yKey
	 *            y轴
	 * @param chartType
	 *            chart type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> Highcharts convert(final Collection<E> datas, final String groupKey, final String xkey,
			final String yKey, final String chartType) {

		if (CollectionUtils.isNotEmpty(datas)) {

			Highcharts charts = new Highcharts();

			if (CollectionUtils.isNotEmpty(datas)) {
				List<Series> seriesList = new ArrayList<Series>();

				GroupPredicate<E> predicate = null;
				Collection<E> filterList = null;
				List<Category<Object>> dataList = null;
				Series series = null;
				Object name = null;
				String type = null;
				List<Object> xs = null;
				Iterator<Object> iterator = null;

				List<Object> categories = getXs(datas, xkey);
				List<String> groups = getGroups(datas, groupKey);

				for (String group : groups) {
					predicate = new GroupPredicate<E>(group, groupKey);
					filterList = predicate.select(datas);
					if (CollectionUtils.isNotEmpty(filterList)) {
						dataList = new ArrayList<Category<Object>>();
						xs = new ArrayList<Object>();

						for (E e : filterList) {

							type = null;
							if (StringUtils.isNotEmpty(chartType)) {
								type = PropertyUtils.getString(e, chartType);
							}

							name = PropertyUtils.getProperty(e, xkey);
							xs.add(name);
							dataList.add(new Category<Object>(name, PropertyUtils.getNumber(e, yKey)));
						}

						// 补全x轴数据
						if (xs.size() < categories.size()) {
							// 差值
							iterator = CollectionUtils.subtract(categories, xs).iterator();
							while (iterator.hasNext()) {
								name = iterator.next();
								int j = categories.indexOf(name);
								dataList.add(j, new Category<Object>(name, null));
							}
						}

						series = new Series(group);
						if (StringUtils.isNotEmpty(type)) {
							series.setType(type);
						}

						series.setData(dataList);
						seriesList.add(series);
					}
				}

				charts.setSeries(seriesList);
			}

			return charts;
		} else {
			return EMPTYHIGHCHARTS;
		}

	}

	private static <E> List<Object> getXs(final Collection<E> list, final String key) {
		Object group = null;
		List<Object> groups = new ArrayList<Object>(list.size());
		for (E e : list) {
			group = PropertyUtils.getProperty(e, key);
			if (group != null && !groups.contains(group)) {
				groups.add(group);
			}
		}
		return groups;
	}

	/**
	 * 
	 * 
	 * @param list
	 * @param key
	 * @return
	 */
	public static <E> List<String> getGroups(final Collection<E> list, final String key) {
		String group = null;
		List<String> groups = new ArrayList<String>(list.size());
		for (E e : list) {
			group = PropertyUtils.getString(e, key);
			if (StringUtils.isNotEmpty(group) && !groups.contains(group)) {
				groups.add(group);
			}
		}
		return groups;
	}

}
