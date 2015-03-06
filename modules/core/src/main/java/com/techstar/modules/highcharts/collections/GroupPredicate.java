package com.techstar.modules.highcharts.collections;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.commons.beanutils.PropertyUtils;

public class GroupPredicate<E> implements Predicate {

	private String group;
	private String key;

	public GroupPredicate(String group, String key) {
		this.group = group;
		this.key = key;
	}

	@Override
	public boolean evaluate(Object object) {
		return StringUtils.equals(group, PropertyUtils.getString(object, key));
	}

	@SuppressWarnings("unchecked")
	public Collection<E> select(final Collection<E> list) {
		return CollectionUtils.select(list, this);
	}

}
