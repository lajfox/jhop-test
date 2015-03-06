package com.techstar.example.elfinder.log.aspectj;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techstar.example.elfinder.log.service.LogService;
import com.techstar.example.elfinder.log.service.ThreadPoolService;
import com.techstar.example.elfinder.log.thread.LogThread;
import com.techstar.modules.elfinder.controller.executor.CommandExecutionContext;

@Aspect
@Component
public class LogInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	@Autowired
	private ThreadPoolService threadPoolService;
	@Autowired
	private LogService logService;

	@Pointcut(value = "execution(* com.techstar.modules.elfinder.controller.executor.CommandExecutor.execute(..)) && args(commandExecutionContext)", argNames = "commandExecutionContext")
	public void afterPointCut(CommandExecutionContext commandExecutionContext) {
	}

	@After(value = "afterPointCut(commandExecutionContext)", argNames = "commandExecutionContext")
	public void after(CommandExecutionContext commandExecutionContext) {
		logger.info("after method begin...");
		threadPoolService.execute(new LogThread(logService, commandExecutionContext));
		logger.info("after method end...");
	}
}
