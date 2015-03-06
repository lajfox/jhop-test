package com.techstar.hbjmis.service.oa.dispatch;

import org.activiti.engine.runtime.Execution;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techstar.modules.activiti.service.WorkflowService;

/**
 * 发文会签流程Service
 * 
 * @author ZengWenfeng
 */
@Service
public class DispatchWorkflowService {

	private static final Logger logger = LoggerFactory.getLogger(DispatchWorkflowService.class);

	@Autowired
	protected WorkflowService workflowService;

	/**
	 * 是否允许结束会签（多实例） 参数的含义请参考用户手册
	 * 
	 * @param nrOfInstances
	 *            实例总数
	 * @param nrOfActiveInstances
	 *            当前活跃的,也就是还没完成的,实例的个数。对于顺序的多实例,该值总是 1
	 * @param nrOfCompletedInstances
	 *            已经完成的实例的个数
	 * @param loopCounter
	 *            表示 for-each 循环中实例的索引
	 */
	public Boolean canComplete(Execution execution, Integer rate, Integer nrOfInstances, Integer nrOfActiveInstances,
			Integer nrOfCompletedInstances, Integer loopCounter) {
		String agreeCounterName = "agreeCounter";
		Object agreeCounter = workflowService.getRuntimeService().getVariable(execution.getId(), agreeCounterName);

		if (agreeCounter == null) {
			// 初始化计数器
			workflowService.getRuntimeService().setVariable(execution.getId(), agreeCounterName, 1);
		} else {
			// 计数器累加
			Integer integerCounter = (Integer) workflowService.getRuntimeService().getVariable(execution.getId(),
					agreeCounterName);
			workflowService.getRuntimeService().setVariable(execution.getId(), agreeCounterName, ++integerCounter);
		}

		logger.debug("execution: {}" + ToStringBuilder.reflectionToString(execution));
		logger.debug("rate={}, nrOfInstances={}, nrOfActiveInstances={}, nrOfComptetedInstances={}, loopCounter={}",
				new Object[] { rate, nrOfInstances, nrOfActiveInstances, nrOfCompletedInstances, loopCounter });

		// 计算通过的比例，以此决定是否结束会签
		Double completeRate = new Double(nrOfCompletedInstances) / nrOfInstances;
		boolean canComlete = completeRate * 100 >= rate;
		logger.debug("rate: {}, completeRate: {}, canComlete={}", new Object[] { rate, completeRate, canComlete });
		return canComlete;
	}

}
