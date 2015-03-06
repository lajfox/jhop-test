package com.techstar.modules.elfinder.impl;

import java.util.List;

import com.techstar.modules.elfinder.domain.ElfinderChecker;
import com.techstar.modules.elfinder.service.DbSecurityChecker;

public class DefaultDbSecurityChecker implements DbSecurityChecker {

	@Override
	public List<ElfinderChecker> getCheckers() {
		return null;
	}

	@Override
	public boolean isChecker() {
		return false;
	}
}
