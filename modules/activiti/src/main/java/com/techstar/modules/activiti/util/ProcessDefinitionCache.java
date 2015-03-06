package com.techstar.modules.activiti.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;

import com.google.common.collect.Maps;

/**
 * 流程定义缓存
 * 
 * @author ZengWenfeng
 */
public final class ProcessDefinitionCache {

	private static final Map<String, ProcessDefinition> map = Maps.<String, ProcessDefinition> newHashMap();
	private static final Map<String, List<ActivityImpl>> activities = Maps.<String, List<ActivityImpl>> newHashMap();
	private static final Map<String, ActivityImpl> singleActivity = Maps.<String, ActivityImpl> newHashMap();
	private static final Map<String, String> childActivityIdMap = new HashMap<String, String>();

	private static RepositoryService repositoryService = ContextLoader.getCurrentWebApplicationContext().getBean(
			RepositoryService.class);
	private static RuntimeService runtimeService = ContextLoader.getCurrentWebApplicationContext().getBean(
			RuntimeService.class);

	/**
	 * 根据流程定义ID从缓存中获取流程定义
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @return 流程定义
	 */
	public static ProcessDefinition get(String processDefinitionId) {
		ProcessDefinition processDefinition = map.get(processDefinitionId);
		if (processDefinition == null) {
			// 根据流程定义ID获取当前流程的流程定义
			processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(processDefinitionId);
			if (processDefinition != null) {
				put(processDefinitionId, processDefinition);
			}
		}
		return processDefinition;
	}

	/**
	 * 将流程定义存入缓存中
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @param processDefinition
	 *            流程定义
	 */
	public static void put(String processDefinitionId, ProcessDefinition processDefinition) {
		map.put(processDefinitionId, processDefinition);

		// 根据流程定义获得所有的节点
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) processDefinition;
		activities.put(processDefinitionId, pde.getActivities());

		for (ActivityImpl activityImpl : pde.getActivities()) {
			// 流程节点
			singleActivity.put(processDefinitionId + "_" + activityImpl.getId(), activityImpl);
		}
	}

	/**
	 * 获取当前流程实例的节点
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @param activityId
	 *            当前节点ID
	 * @return 流程实例的节点
	 */
	public static ActivityImpl getActivity(String processDefinitionId, String activityId) {
		ProcessDefinition processDefinition = get(processDefinitionId);
		if (processDefinition != null) {
			ActivityImpl activityImpl = singleActivity.get(processDefinitionId + "_" + activityId);
			if (activityImpl != null) {
				return activityImpl;
			}
		}
		return null;
	}

	/**
	 * 获取当前流程实例的节点名称
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @param activityId
	 *            当前节点ID
	 * @return 流程实例的节点名称
	 */
	public static String getActivityName(String processDefinitionId, String activityId) {

		ActivityImpl activity = getActivity(processDefinitionId, activityId);
		return activity == null ? null : ObjectUtils.toString(activity.getProperty("name"));
	}

	/**
	 * 获取当前流程实例的节点名称
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @param activityId
	 *            当前节点ID
	 * @return 流程实例的节点名称
	 */
	public static String getActivityName(String processDefinitionId, String processInstanceId, String activityId) {

		if (StringUtils.isEmpty(activityId) || StringUtils.equalsIgnoreCase("null", activityId)) {
			activityId = getChildActivityId(processInstanceId, activityId);
		}

		if (StringUtils.isEmpty(activityId)) {
			return null;
		} else {
			ActivityImpl activity = getActivity(processDefinitionId, activityId);
			return activity == null ? null : ObjectUtils.toString(activity.getProperty("name"));
		}
	}

	private static String getChildActivityId(String processInstanceId, String activityId) {

		if (StringUtils.isEmpty(activityId) || StringUtils.equalsIgnoreCase("null", activityId)) {
			activityId = childActivityIdMap.get(processInstanceId);
			if (StringUtils.isEmpty(activityId)) {

				List<Execution> list = runtimeService.createExecutionQuery().parentId(processInstanceId).listPage(0, 1);

				if (CollectionUtils.isNotEmpty(list)) {
					activityId = list.get(0).getActivityId();
					childActivityIdMap.put(processInstanceId, activityId);
				}
			}
		}

		return activityId;
	}

}
