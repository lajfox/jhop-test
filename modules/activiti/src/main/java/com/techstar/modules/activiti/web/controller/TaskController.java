package com.techstar.modules.activiti.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.activiti.domain.TaskInfo;
import com.techstar.modules.activiti.domain.TaskItem;
import com.techstar.modules.activiti.service.WorkflowService;
import com.techstar.modules.activiti.util.TaskItemUtils;
import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.Results;

@Controller
@RequestMapping(value = "workflow/task")
public class TaskController {

	private static final JsonMapper JSONMAPPER = JsonMapper.nonEmptyMapper();

	@Autowired
	@Qualifier("workflowService")
	private WorkflowService workflowService;

	/**
	 * <p>
	 * 确定用户是否有权处理当前任务,
	 * </p>
	 * <p>
	 * statusCode=0无权处理，
	 * </p>
	 * <p>
	 * statusCode=1办理任务,
	 * </p>
	 * <p>
	 * statusCode=2签收任务
	 * </p>
	 * 
	 * @param tasks
	 *            　当前任务json串
	 * @return Results
	 */
	@RequestMapping(value = "decision", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results decision(@RequestParam("tasks") String tasks) {

		// 登录用户名
		String userId = SubjectUtils.getPrincipal(ShiroUser.class).getLoginName();

		List<TaskItem> items = TaskItemUtils.fromJson(tasks);

		Map<String, String> map = new HashMap<String, String>();

		for (TaskItem item : items) {
			if (StringUtils.equals(userId, StringUtils.trim(item.getAssignee()))) {

				map.put("taskinfo", JSONMAPPER.toJson(new TaskInfo(item.getId(), item.getExecutionId(), item
						.getTaskDefinitionKey(), item.getName(), item.getDelegationState(), item.getOwner(), item
						.getAssignee(), isClaim(item.getId(), item.isClaim()))));

				return new Results("办理任务", 1, map);
			}
		}

		for (TaskItem item : items) {
			if (StringUtils.equals(userId, StringUtils.trim(item.getOwner()))
					&& item.getDelegationState() == DelegationState.PENDING) {

				map.put("taskinfo", JSONMAPPER.toJson(new TaskInfo(item.getId(), item.getExecutionId(), item
						.getTaskDefinitionKey(), item.getName(), item.getDelegationState(), item.getOwner(), item
						.getAssignee(), isClaim(item.getId(), item.isClaim()))));

				return new Results("取消委派", 3, map);
			}
		}

		for (TaskItem item : items) {
			if (CollectionUtils.isNotEmpty(item.getCandidateUsers()) && item.getCandidateUsers().contains(userId)) {

				map.put("taskinfo", JSONMAPPER.toJson(new TaskInfo(item.getId(), item.getExecutionId(), item
						.getTaskDefinitionKey(), item.getName(), item.getDelegationState(), item.getOwner(), item
						.getAssignee(), isClaim(item.getId(), item.isClaim()))));
				return new Results("签收任务", 2, map);
			}
			if (CollectionUtils.isNotEmpty(item.getCandidateGroups())
					&& SubjectUtils.hasAnyRoles(item.getCandidateGroups())) {

				map.put("taskinfo", JSONMAPPER.toJson(new TaskInfo(item.getId(), item.getExecutionId(), item
						.getTaskDefinitionKey(), item.getName(), item.getDelegationState(), item.getOwner(), item
						.getAssignee(), isClaim(item.getId(), item.isClaim()))));
				return new Results("签收任务", 2, map);
			}
		}

		return new Results("没有权限处理", 0, map);
	}

	/**
	 * <p>
	 * 实时确定用户是否有权处理当前任务,
	 * </p>
	 * <p>
	 * statusCode=0无权处理，
	 * </p>
	 * <p>
	 * statusCode=1办理任务,
	 * </p>
	 * <p>
	 * statusCode=2签收任务
	 * </p>
	 * 
	 * @param processInstanceId
	 *            　流程实例ID,不能为空
	 * @return Results
	 */
	@RequestMapping(value = "realtime/decision/{processInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results realtimeDecision(@PathVariable("processInstanceId") String processInstanceId) {

		// 登录用户名
		String userId = SubjectUtils.getPrincipal(ShiroUser.class).getLoginName();

		Map<String, String> map = new HashMap<String, String>();

		List<Task> tasks = this.workflowService.getTaskService().createTaskQuery().processInstanceId(processInstanceId)
				.taskAssignee(userId).active().orderByTaskCreateTime().asc().list();
		if (CollectionUtils.isNotEmpty(tasks)) {
			Task task = tasks.get(0);

			map.put("taskinfo", JSONMAPPER.toJson(new TaskInfo(task.getId(), task.getExecutionId(), task
					.getTaskDefinitionKey(), task.getName(), task.getDelegationState(), task.getOwner(), task
					.getAssignee(), isClaim(task.getId()))));
			return new Results("办理任务", 1, map);
		}

		tasks = this.workflowService.getTaskService().createTaskQuery().processInstanceId(processInstanceId)
				.taskCandidateUser(userId).active().orderByTaskCreateTime().asc().list();
		if (CollectionUtils.isNotEmpty(tasks)) {
			Task task = tasks.get(0);
			map.put("taskinfo", JSONMAPPER.toJson(new TaskInfo(task.getId(), task.getExecutionId(), task
					.getTaskDefinitionKey(), task.getName(), task.getDelegationState(), task.getOwner(), task
					.getAssignee(), isClaim(task.getId()))));
			return new Results("签收任务", 2, map);
		}

		return new Results("没有权限处理", 0, map);
	}

	/**
	 * 判断当前任务节点是否为签收任务节点
	 * 
	 * @param taskId
	 * @return
	 */
	private boolean isClaim(final String taskId) {
		return isClaim(taskId, false);
	}

	/**
	 * 判断当前任务节点是否为签收任务节点
	 * 
	 * @param taskId
	 * @param claim
	 * @return
	 */
	private boolean isClaim(final String taskId, final boolean claim) {
		if (!claim) {
			// 任务历史的签收时间不为空时则是签收任务节点
			HistoricTaskInstance task = this.workflowService.getHistoryService().createHistoricTaskInstanceQuery()
					.taskId(taskId).singleResult();
			return task.getClaimTime() != null;
		}
		return claim;
	}

}
