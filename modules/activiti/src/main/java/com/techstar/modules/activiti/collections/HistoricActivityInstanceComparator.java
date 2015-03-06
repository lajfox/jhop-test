package com.techstar.modules.activiti.collections;

import java.util.Comparator;

import org.activiti.engine.history.HistoricActivityInstance;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * HistoricActivityInstance排序
 * 
 * @author sundoctor
 * 
 */
public class HistoricActivityInstanceComparator implements Comparator<HistoricActivityInstance> {

	private static final Comparator<HistoricActivityInstance> comparator = new HistoricActivityInstanceComparator();

	private HistoricActivityInstanceComparator() {

	}

	public static Comparator<HistoricActivityInstance> getInstanse() {
		return comparator;
	}

	@Override
	public int compare(HistoricActivityInstance o1, HistoricActivityInstance o2) {

		if (NumberUtils.toLong(o1.getId()) == NumberUtils.toLong(o2.getId())) {
			return 0;
		} else if (NumberUtils.toLong(o1.getId()) > NumberUtils.toLong(o2.getId())) {
			return 1;
		} else {
			return -1;
		}
	}

}
