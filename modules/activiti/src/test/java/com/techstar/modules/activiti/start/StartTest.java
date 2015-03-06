package com.techstar.modules.activiti.start;

import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:spring/activiti-context.xml")
public class StartTest extends SpringActivitiTestCase {

	@Deployment
	public void testStartWithGateway() {

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("startWithGateway",
				CollectionUtil.singletonMap("order", 0));
		assertEquals(0, runtimeService.createProcessInstanceQuery().count());
		
		processInstance = runtimeService.startProcessInstanceByKey("startWithGateway",
				CollectionUtil.singletonMap("order", 1));		
		Task task = taskService.createTaskQuery().singleResult();
		assertEquals("财务审计", task.getName());
	}
}
