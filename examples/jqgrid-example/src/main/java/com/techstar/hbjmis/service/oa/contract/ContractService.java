package com.techstar.hbjmis.service.oa.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hbjmis.entity.oa.Contract;
import com.techstar.hbjmis.repository.jpa.ContractDao;
import com.techstar.modules.activiti.service.EntityWorkflowServiceImpl;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

/**
 * 合同管理
 * 
 * @author HenryYan
 */
@Component
@Transactional(readOnly = true)
public class ContractService extends EntityWorkflowServiceImpl<Contract, String> {

	@Autowired
	private ContractDao contractDao;

	@Override
	protected MyJpaRepository<Contract, String> getMyJpaRepository() {
		return contractDao;
	}
}
