package com.techstar.hbjmis.web.oa.order;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.hbjmis.entity.oa.Order;
import com.techstar.hbjmis.service.oa.order.OrderService;
import com.techstar.modules.activiti.service.EntityWorkflowService;
import com.techstar.modules.activiti.web.controller.DynamicFormController;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.security.entity.User;

/**
 * 订单控制器，动态表单演示
 * 
 * @author ZengWenfeng
 */
@Controller
@RequestMapping(value = "/oa/order")
public class OrderController extends DynamicFormController<Order, String> {

	@Autowired
	OrderService OrderService;

	@Override
	public EntityWorkflowService<Order, String> getWorkflowService() {
		return this.OrderService;
	}

	@RequestMapping("search")
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Order> spec) {
		Page<Order> page = this.OrderService.findAll(spec, pageable);
		return new PageResponse<Order>(page);
	}

	@Override
	protected void editBefore(final Order Order, final String oper) {
		if (StringUtils.equals("add", oper)) {
			Order.setId(null);
			ShiroUser user = SubjectUtils.getPrincipal();
			Order.setUserName(user.getName());
			Order.setCreateTime(new Date());
		}
	}

	@RequestMapping("user/search")
	public @ResponseBody
	Results usersearch() {
		List<User> list = this.OrderService.findBy(User.class, "idNot", SubjectUtils.getPrincipal(ShiroUser.class)
				.getId());
		return new Response<User>(list);
	}

}
