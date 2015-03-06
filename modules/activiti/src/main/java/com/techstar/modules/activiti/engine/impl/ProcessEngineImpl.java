package com.techstar.modules.activiti.engine.impl;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

import com.techstar.modules.activiti.engine.CustomIdentityService;
import com.techstar.modules.activiti.spring.SpringProcessEngineConfiguration;

@Deprecated
public class ProcessEngineImpl extends org.activiti.engine.impl.ProcessEngineImpl {

	protected CustomIdentityService customIdentityService;

	public ProcessEngineImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
		super(processEngineConfiguration);
		if (processEngineConfiguration instanceof SpringProcessEngineConfiguration) {
			this.customIdentityService = ((SpringProcessEngineConfiguration) processEngineConfiguration)
					.getCustomIdentityService();
		}
	}

	public CustomIdentityService getCustomIdentityService() {
		return customIdentityService;
	}

}
