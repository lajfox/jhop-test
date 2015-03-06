package com.techstar.hbjmis.service.oa.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hbjmis.entity.oa.Order;
import com.techstar.hbjmis.repository.jpa.OrderDao;
import com.techstar.modules.activiti.service.EntityWorkflowServiceImpl;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

/**
 * 订单管理
 * 
 * @author HenryYan
 */
@Component
@Transactional(readOnly = true)
public class OrderService extends EntityWorkflowServiceImpl<Order, String> {

	@Autowired
	private OrderDao orderDao;

	@Override
	protected MyJpaRepository<Order, String> getMyJpaRepository() {
		return orderDao;
	}
}
