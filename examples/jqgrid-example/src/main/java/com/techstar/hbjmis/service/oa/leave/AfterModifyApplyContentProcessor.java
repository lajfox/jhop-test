package com.techstar.hbjmis.service.oa.leave;

import static com.techstar.modules.activiti.util.DelegateTaskUtils.getDate;
import static com.techstar.modules.activiti.util.DelegateTaskUtils.getString;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hbjmis.entity.oa.Leave;

/**
 * 调整请假内容处理器
 * 
 * @author ZengWenfeng
 */
@Component
@Transactional
public class AfterModifyApplyContentProcessor implements TaskListener {

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

		leave.setLeaveType(getString(delegateTask, "leaveType"));
		leave.setStartTime(getDate(delegateTask, "startTime"));
		leave.setEndTime(getDate(delegateTask, "endTime"));
		leave.setReason(getString(delegateTask, "reason"));

		leaveService.save(leave);
	}

}
