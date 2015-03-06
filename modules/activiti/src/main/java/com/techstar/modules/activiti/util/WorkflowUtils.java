package com.techstar.modules.activiti.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.techstar.modules.activiti.collections.HistoricActivityInstanceComparator;

public final class WorkflowUtils {

	/**
	 * /获取流程走过的节点
	 * 
	 * @param processDefinitionEntity
	 *            流程定义
	 * @param historicActivityInstances
	 *            　历史流程节点
	 * @return 流程走过的节点集合
	 */
	public static List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances) {
		
		//HistoricActivityInstance 相邻两个节点的 startTime 可以相同，排序可能混乱，根据id重新排序
		Collections.sort(historicActivityInstances, HistoricActivityInstanceComparator.getInstanse());

		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId

		ActivityImpl activityImpl, sameActivityImpl1, sameActivityImpl2, pvmActivityImpl = null;
		List<ActivityImpl> sameStartTimeNodes = null;
		HistoricActivityInstance activityImpl1, activityImpl2 = null;
		List<PvmTransition> pvmTransitions = null;
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历

			activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息

			sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
			sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1)
					.getActivityId());// 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);

			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {

				activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
				activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点

				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {// 如果第一个节点和第二个节点开始时间相同保存
					sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {// 有不相同跳出循环
					break;
				}
			}

			pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions) {// 对所有的线进行遍历
				pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}

		}
		return highFlows;

	}
}
