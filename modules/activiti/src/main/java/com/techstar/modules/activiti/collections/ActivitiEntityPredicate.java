package com.techstar.modules.activiti.collections;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

import com.techstar.modules.activiti.domain.ActivitiEntity;

public class ActivitiEntityPredicate implements Predicate {

	private String processInstanceId;

	public ActivitiEntityPredicate(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Override
	public boolean evaluate(Object object) {
		ActivitiEntity entity = (ActivitiEntity) object;
		return StringUtils.equals(entity.getProcessInstanceId(), processInstanceId);
	}

	@SuppressWarnings("unchecked")
	public ActivitiEntity select(final List<ActivitiEntity> entities, final Predicate predicate) {
		List<ActivitiEntity> list = (List<ActivitiEntity>) CollectionUtils.select(entities, predicate);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

}
