package com.techstar.modules.activiti.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.modules.activiti.util.ProcessDefinitionCache;
import com.techstar.modules.springframework.data.jpa.domain.Results;

@Controller
@RequestMapping(value = "activity")
public class ActivityController {

	private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);
	
	//@Autowired
	//@Qualifier("workflowService")
	//private WorkflowService workflowService;

	/**
	 * 获取当前处理节点名称
	 * 
	 * @param processDefinitionId
	 *            流程定义ＩＤ
	 * @param activityId
	 *            　当前处理节点ＩＤ
	 * @return 当前处理节点名称
	 */
	@RequestMapping("name/{processDefinitionId}/{processInstanceId}/{activityId}")
	public @ResponseBody
	Results name(@PathVariable("processDefinitionId") String processDefinitionId,
			@PathVariable("processInstanceId") String processInstanceId, @PathVariable("activityId") String activityId) {

		return new Results(ProcessDefinitionCache.getActivityName(processDefinitionId, processInstanceId, activityId));
	}

}
