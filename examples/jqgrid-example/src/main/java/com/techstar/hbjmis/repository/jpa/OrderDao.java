package com.techstar.hbjmis.repository.jpa;

import com.techstar.hbjmis.entity.oa.Order;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface OrderDao extends MyJpaRepository<Order, String>{

}
