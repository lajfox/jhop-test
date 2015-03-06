package com.techstar.modules.activiti.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.techstar.modules.activiti.domain.TaskItem;
import com.techstar.modules.jackson.mapper.JsonMapper;

public class TaskItemUtils {

	private static final JsonMapper JSONMAPPER = JsonMapper.nonEmptyMapper();

	@SuppressWarnings("unchecked")
	public static List<TaskItem> fromJson(final String tasks) {
		JavaType type = JSONMAPPER.createCollectionType(List.class, TaskItem.class);
		return StringUtils.isEmpty(tasks) ? null : (List<TaskItem>) JSONMAPPER.fromJson(tasks, type);
	}

	public static String getActivityNames(final String tasks) {

		List<TaskItem> list = fromJson(tasks);
		return getActivityNames(list);

	}

	public static String getActivityNames(List<TaskItem> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else {
			Set<String> set = new HashSet<String>();
			for (TaskItem item : list) {
				if (StringUtils.isNotEmpty(item.getName())) {
					set.add(item.getName());
				}
			}
			return CollectionUtils.isEmpty(set) ? null : StringUtils.join(set, ",");
		}

	}

	public static String getAssignees(final String tasks) {
		List<TaskItem> list = fromJson(tasks);
		return getAssignees(list);

	}

	public static String getAssignees(List<TaskItem> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else {
			Set<String> set = new HashSet<String>();
			for (TaskItem item : list) {
				if (StringUtils.isNotEmpty(item.getAssignee())) {
					set.add(item.getAssignee());
				}
				if(CollectionUtils.isNotEmpty(item.getCandidateUsers())){
					set.addAll(item.getCandidateUsers());
				}
				if(CollectionUtils.isNotEmpty(item.getCandidateGroups())){
					set.addAll(item.getCandidateGroups());
				}
			}
			return CollectionUtils.isEmpty(set) ? null : StringUtils.join(set, ",");
		}

	}

}
