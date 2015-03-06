package com.techstar.sys.utils;

import java.util.Collections;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;
import com.techstar.sys.entity.SystemDicItem;

public final class DicItemCacheUtils {

	@SuppressWarnings("unchecked")
	public static List<SystemDicItem> get(final Cache cache, final String key) {
		Element element = cache.get(key);
		return element == null ? null : (List<SystemDicItem>) element.getObjectValue();
	}

	public static List<SystemDicItem> put(final Cache cache, final String key, List<SystemDicItem> items) {
		if (CollectionUtils.isNotEmpty(items)) {
			Element element = new Element(key, items);
			cache.put(element);
		}
		return items;
	}

	public static List<SystemDicItem> put(final Cache cache, final String key, SystemDicItem item) {
		List<SystemDicItem> items = get(cache, key);
		if (items == null) {
			items = Lists.<SystemDicItem> newArrayList();
		} else {
			if (items.contains(item)) {
				items.remove(item);
			}
		}
		items.add(item);

		Collections.sort(items);

		Element element = new Element(key, items);
		cache.put(element);

		return items;
	}

	public static void remove(final Cache cache, final String key) {
		cache.remove(key);
	}

	public static List<SystemDicItem> remove(final Cache cache, final String key, SystemDicItem item) {
		List<SystemDicItem> items = get(cache, key);
		if (items != null) {
			items.remove(item);
			Element element = new Element(key, items);
			cache.put(element);
		}

		return items;
	}
}
