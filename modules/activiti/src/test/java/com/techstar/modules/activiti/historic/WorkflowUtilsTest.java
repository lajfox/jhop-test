package com.techstar.modules.activiti.historic;

import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.springframework.test.context.ContextConfiguration;

import com.techstar.modules.activiti.util.WorkflowUtils;

/**
 * @author ZengWenfeng
 */

@ContextConfiguration("classpath:spring/activiti-context.xml")
public class WorkflowUtilsTest extends SpringActivitiTestCase {

	@Deployment
	public void testGetHighLightedFlows() {

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("getHighLightedFlows");

		Task task = taskService.createTaskQuery().singleResult();
		assertEquals("Schedule meeting", task.getName());

		taskService.complete(task.getId(), CollectionUtil.singletonMap("istrue", false));

		task = taskService.createTaskQuery().singleResult();
		assertEquals("Schedule meeting", task.getName());

		taskService.complete(task.getId(), CollectionUtil.singletonMap("istrue", true));

		assertEquals(0, runtimeService.createProcessInstanceQuery().count());

		List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstance.getId()).orderByHistoricActivityInstanceStartTime().asc().list();

		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());

		List<String> flowIds = WorkflowUtils.getHighLightedFlows(processDefinition, activityInstances);// 获取流程走过的线

		assertEquals(5, flowIds.size());

	}

}
