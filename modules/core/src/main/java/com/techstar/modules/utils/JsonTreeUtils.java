package com.techstar.modules.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.springframework.data.jpa.domain.JsonTree;

@SuppressWarnings({ "rawtypes", "unchecked" })
public final class JsonTreeUtils {

	private static final Predicate DEFAULT_PREDICATE = new MyPredicate();

	public static <T extends JsonTree> List<JsonTree> build(Collection<T> jsonTrees) {
		if (CollectionUtils.isEmpty(jsonTrees)) {
			return null;
		}

		List<JsonTree> trees = new ArrayList<JsonTree>(jsonTrees.size());
		List<JsonTree> parents = select(jsonTrees, DEFAULT_PREDICATE);

		build(jsonTrees, parents, trees);

		return trees;

	}

	private static <T extends JsonTree> void build(Collection<T> jsonTrees, List<JsonTree> parents, List<JsonTree> trees) {

		List<JsonTree> sublist = null;
		jsonTrees.removeAll(parents);

		Collections.sort(parents);
		for (JsonTree parent : parents) {
			trees.add(parent);

			if (CollectionUtils.isEmpty(jsonTrees)) {
				parent.setLeaf(true);// 没有子数据，将其设置为叶子
			} else {
				sublist = select(jsonTrees, new MyPredicate(parent.getId()));
				if (CollectionUtils.isEmpty(sublist)) {
					parent.setLeaf(true);// 没有找到子数据，将其设置为叶子
				} else {
					parent.setLeaf(false);
					build(jsonTrees, sublist, trees);
				}
			}
		}

	}

	private static <T extends JsonTree> List<JsonTree> select(Collection<T> list, Predicate predicate) {
		return (List<JsonTree>) CollectionUtils.select(list, predicate);
	}

	private static class MyPredicate implements Predicate {

		private final String parentid;

		public MyPredicate() {
			this.parentid = null;
		}

		public MyPredicate(String parentid) {
			this.parentid = parentid;
		}

		@Override
		public boolean evaluate(Object obj) {
			JsonTree p = (JsonTree) obj;
			if (parentid == null) {
				return p.getParent() == null;
			} else {
				return p.getParent() != null && StringUtils.equals(p.getParent().getId(), parentid);
			}
		}

	}
}
