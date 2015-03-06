package com.techstar.modules.activiti.collections;

import java.util.List;

import org.activiti.engine.history.HistoricDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * 流程历史详细变量数据过虑
 * 
 * @author sundoctor
 * 
 */
public final class HistoricDetailPredicate implements Predicate {

	private String activityInstanceId;

	public HistoricDetailPredicate(String activityInstanceId) {
		this.activityInstanceId = activityInstanceId;
	}

	@Override
	public boolean evaluate(Object object) {
		HistoricDetail historicDetail = (HistoricDetail) object;
		return StringUtils.equals(historicDetail.getActivityInstanceId(), activityInstanceId);
	}

	@SuppressWarnings("unchecked")
	public List<HistoricDetail> select(final List<HistoricDetail> details, final Predicate predicate) {
		return (List<HistoricDetail>) CollectionUtils.select(details, predicate);
	}

}
