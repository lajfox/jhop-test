package com.techstar.modules.activiti.collections;

import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * 流程历史轨迹数据过虑
 * 
 * @author sundoctor
 * 
 */
public final class HistoricActivityPredicate implements Predicate {

	private static final String GATEWAY = "Gateway";
	private static final Predicate predicate = new HistoricActivityPredicate();

	private HistoricActivityPredicate() {
	}

	@Override
	public boolean evaluate(Object object) {
		HistoricActivityInstance activityInstance = (HistoricActivityInstance) object;
		return !activityInstance.getActivityType().endsWith(GATEWAY);
	}

	public static Predicate getInstance() {
		return predicate;
	}

	public static List<HistoricActivityInstance> select(final List<HistoricActivityInstance> activityInstances) {
		return select(activityInstances, predicate);
	}

	@SuppressWarnings("unchecked")
	public static List<HistoricActivityInstance> select(final List<HistoricActivityInstance> activityInstances,
			final Predicate predicate) {
		return (List<HistoricActivityInstance>) CollectionUtils.select(activityInstances, predicate);
	}

}
