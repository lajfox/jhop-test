package com.techstar.hbjmis.service.oa.leave;

import static com.techstar.modules.activiti.util.DelegateTaskUtils.getDate;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hbjmis.entity.oa.Leave;

/**
 * 销假后处理器
 * <p>
 * 设置销假时间
 * </p>
 * <p>
 * 使用Spring代理，可以注入Bean，管理事物
 * </p>
 * 
 * @author ZengWenfeng
 */
@Component
@Transactional
public class ReportBackEndProcessor implements TaskListener {

	private static final long serialVersionUID = 1L;

	@Autowired
	LeaveService leaveService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.
	 * delegate.DelegateTask)
	 */
	public void notify(DelegateTask delegateTask) {
		String processInstanceId = delegateTask.getProcessInstanceId();

		ProcessInstance processInstance = leaveService.getWorkflowService().getRuntimeService()
				.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

		Leave leave = leaveService.findOne(processInstance.getBusinessKey());
		leave.setRealityStartTime(getDate(delegateTask, "realityStartTime"));
		leave.setRealityEndTime(getDate(delegateTask, "realityEndTime"));

		leaveService.save(leave);
	}

}
