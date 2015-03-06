package com.techstar.example.elfinder.log.thread;

import com.techstar.example.elfinder.log.service.LogService;
import com.techstar.modules.elfinder.controller.executor.CommandExecutionContext;

public class LogThread implements Runnable {

	private LogService logService;
	private CommandExecutionContext commandExecutionContext;

	public LogThread(LogService logService, CommandExecutionContext commandExecutionContext) {
		this.logService = logService;
		this.commandExecutionContext = commandExecutionContext;
	}

	@Override
	public void run() {
		this.logService.log(commandExecutionContext);

	}
}
