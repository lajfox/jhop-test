package com.techstar.modules.activiti.dispatch;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author ZengWenfeng
 */

@ContextConfiguration("classpath:spring/activiti-context.xml")
public class DispatchTest extends SpringActivitiTestCase {

	@Deployment
	public void testDispatch1() {

		List<String> users = new ArrayList<String>();
		users.add("1000");
		users.add("2000");
		users.add("3000");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("dispatch1",
				CollectionUtil.singletonMap("countersignUsers", users));

		List<Task> tasks = taskService.createTaskQuery().list();
		assertEquals(3, tasks.size());

	}

	@Deployment
	public void testDispatch2() {

		List<String> users = new ArrayList<String>();
		users.add("1000");
		users.add("2000");
		users.add("3000");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("dispatch2",
				CollectionUtil.singletonMap("countersignUsers", users));

		List<Task> tasks = taskService.createTaskQuery().list();
		assertEquals(3, tasks.size());

	}

	@Deployment
	public void testDispatch3() {

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("dispatch3");

		List<Task> tasks = taskService.createTaskQuery().list();
		assertEquals(5, tasks.size());

		for (Task task : tasks) {
			List<IdentityLink> links = taskService.getIdentityLinksForTask(task.getId());
			if (CollectionUtils.isNotEmpty(links)) {			
				for (IdentityLink link : links) {
					if (StringUtils.isNotEmpty(link.getUserId())) {
						System.out.println(link.getUserId());
					}
					
					if (StringUtils.isNotEmpty(link.getGroupId())) {
						System.out.println(link.getGroupId());
					}					
				}

			}
		}

	}

}
