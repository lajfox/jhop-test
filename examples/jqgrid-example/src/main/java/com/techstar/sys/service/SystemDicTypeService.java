package com.techstar.sys.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.MyJpaServiceImpl;
import com.techstar.sys.entity.SystemDicType;
import com.techstar.sys.repository.jpa.SystemDicTypeDao;

@Component
@Transactional(readOnly = true)
public class SystemDicTypeService extends MyJpaServiceImpl<SystemDicType, String> {

	private static Logger logger = LoggerFactory.getLogger(SystemDicTypeService.class);

	@Autowired
	private SystemDicTypeDao systemDicTypeDao;

	@Override
	protected MyJpaRepository<SystemDicType, String> getMyJpaRepository() {
		// TODO Auto-generated method stub
		return systemDicTypeDao;
	}

}
