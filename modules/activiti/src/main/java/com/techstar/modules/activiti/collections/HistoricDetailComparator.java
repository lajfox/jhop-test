package com.techstar.modules.activiti.collections;

import java.util.Comparator;

import org.activiti.engine.history.HistoricDetail;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * HistoricDetail排序，按id倒排
 * 
 * @author sundoctor
 * 
 */
public class HistoricDetailComparator implements Comparator<HistoricDetail> {

	@Override
	public int compare(HistoricDetail arg0, HistoricDetail arg1) {

		if (NumberUtils.toLong(arg0.getId()) == NumberUtils.toLong(arg1.getId())) {
			return 0;
		} else if (NumberUtils.toLong(arg0.getId()) < NumberUtils.toLong(arg1.getId())) {
			return 1;
		} else {
			return -1;
		}
	}

}
