package com.techstar.example.elfinder.log.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.techstar.modules.elfinder.controller.executor.CommandExecutionContext;
import com.techstar.modules.elfinder.domain.Param;

@Service
public class LogService {
	
	private static final Logger logger = LoggerFactory.getLogger(LogService.class);

	public void log(final CommandExecutionContext commandExecutionContext){
		//处理日志
		Param param = commandExecutionContext.getParam();
		logger.info("当前命令：{}",param.getCmd());
	}
}
