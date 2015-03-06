package com.techstar.modules.activiti.engine.impl.persistence.entity;


import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.techstar.modules.springframework.test.SpringTransactionalTestCase;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml"})
public class CustomGroupEntityManagerTest extends SpringTransactionalTestCase {

	@Autowired
	private TaskService taskService ;

	@Test
	public void findGroupsByUser(){
		List<Task> unsignedTasks = taskService.createTaskQuery().taskCandidateUser("admin")
				.processDefinitionKey("getHighLightedFlows").active().orderByTaskId().desc().orderByTaskCreateTime()
				.desc().list();
	}
	
}
