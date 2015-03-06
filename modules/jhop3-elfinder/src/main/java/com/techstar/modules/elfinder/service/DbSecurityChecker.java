package com.techstar.modules.elfinder.service;

import java.util.List;

import com.techstar.modules.elfinder.domain.ElfinderChecker;

public interface DbSecurityChecker {

	/**
	 * 用户操作权限
	 * 
	 * @return
	 */
	List<ElfinderChecker> getCheckers();

	/**
	 * 是否校验用户权限
	 * 
	 * @return
	 */
	boolean isChecker();

}