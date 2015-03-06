package com.techstar.modules.activiti.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.techstar.modules.activiti.engine.impl.bpmn.diagram.CustomProcessDiagramGenerator;
import com.techstar.modules.activiti.service.WorkflowService;
import com.techstar.modules.activiti.util.WorkflowUtils;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.utils.Identities;

@Controller
@RequestMapping(value = "workflow/historic")
public class HistoricController {

	@Autowired
	@Qualifier("workflowService")
	private WorkflowService workflowService;

	@Autowired
	private ProcessEngineConfigurationImpl processEngineConfiguration;

	@RequestMapping("diagram")
	public String opendiagram() {
		return "workflow/diagram";
	}

	/**
	 * 标记高亮流程图
	 * 
	 * @param processInstanceId
	 *            流程实例ＩＤ
	 * @param executionId
	 *            　执行实例ＩＤ
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping("diagram/{processDefinitionKey}")
	public void generateDiagram(@PathVariable("processDefinitionKey") String processDefinitionKey,
			@RequestParam(value = "processDefinitionId", required = false) String processDefinitionId,
			@RequestParam(value = "processInstanceId", required = false) String processInstanceId,
			@RequestParam(value = "executionId", required = false) String[] executionId, HttpServletResponse response)
			throws IOException {

		InputStream imageStream = null;
		if (StringUtils.isEmpty(processInstanceId)) {
			// 没有流程实例时，通过processDefinitionKey取得流程图
			ProcessDefinition processDefinition = workflowService.getRepositoryService().createProcessDefinitionQuery()
					.processDefinitionKey(processDefinitionKey).active().latestVersion().singleResult();
			if (processDefinition != null) {
				String diagramResourceName = processDefinition.getDiagramResourceName();
				imageStream = workflowService.getRepositoryService().getResourceAsStream(
						processDefinition.getDeploymentId(), diagramResourceName);
			}
		} else {

			BpmnModel bpmnModel = workflowService.getRepositoryService().getBpmnModel(processDefinitionId);

			List<HistoricActivityInstance> activityInstances = workflowService.getHistoryService()
					.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
					.orderByHistoricActivityInstanceStartTime().asc().list();

			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) workflowService
					.getRepositoryService()).getDeployedProcessDefinition(processDefinitionId);

			List<String> flowIds = WorkflowUtils.getHighLightedFlows(processDefinition, activityInstances);// 获取流程走过的线

			List<String> hisActivitiIds = new ArrayList<String>();
			for (HistoricActivityInstance hai : activityInstances) {
				hisActivitiIds.add(hai.getActivityId());// 获取流程走过的节点
			}

			List<String> curActivitiIds = Lists.<String> newArrayList(), ids = null;
			if (ArrayUtils.isNotEmpty(executionId)) {
				for (String id : executionId) {
					ids = workflowService.getRuntimeService().getActiveActivityIds(id);// 当前处理节点
					if (CollectionUtils.isNotEmpty(ids)) {
						curActivitiIds.addAll(ids);
					}
				}
				hisActivitiIds.removeAll(curActivitiIds);// 去掉当前处理节点
			}

			Context.setProcessEngineConfiguration(processEngineConfiguration);

			imageStream = CustomProcessDiagramGenerator.generatePngDiagram(bpmnModel, hisActivitiIds, curActivitiIds,
					flowIds);

		}

		OutputStream out = null;
		response.setContentType("image/png");
		try {
			out = response.getOutputStream();
			IOUtils.copy(imageStream, out);
		} finally {
			IOUtils.closeQuietly(imageStream);
			IOUtils.closeQuietly(out);
			if (StringUtils.isNotEmpty(processInstanceId)) {
				Context.removeProcessEngineConfiguration();
			}
		}

	}

	@RequestMapping("")
	public String historicActivityInstanceTabs(Model model) {
		return "workflow/historic";
	}

	@RequestMapping("list")
	public String historicActivityInstanceList(Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		return "workflow/historicList";
	}

	@RequestMapping("search/{processInstanceId}")
	public @ResponseBody
	Results getHistoricActivityInstance(@PathVariable("processInstanceId") String processInstanceId,
			@RequestParam(value = "actionVariableNames", required = false) List<String> actionVariableNames,
			@RequestParam(value = "reasonVariableNames", required = false) List<String> reasonVariableNames) {
		List<HistoricActivityInstance> activityInstances = workflowService.findHistoricActivityInstances(
				processInstanceId, actionVariableNames, reasonVariableNames);
		return new Response<HistoricActivityInstance>(activityInstances);
	}

}
