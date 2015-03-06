package com.techstar.example.elfinder.log.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

@Component
public class ThreadPoolService {

	protected ScheduledExecutorService scheduler;

	@PostConstruct
	public void init() {
		scheduler = Executors.newScheduledThreadPool(8);
	}

	@PreDestroy
	public void destroy() {
		scheduler.shutdown();
	}

	public void execute(Runnable command) {
		scheduler.execute(command);
	}

	public void schedule(Runnable command) {
		scheduler.schedule(command, 1, TimeUnit.SECONDS);
	}

	public void schedule(Runnable command, long delay, TimeUnit unit) {
		scheduler.schedule(command, delay, unit);
	}

	public <T> void schedule(Callable<T> callable) {
		scheduler.schedule(callable, 1, TimeUnit.SECONDS);
	}

	public <T> void schedule(Callable<T> callable, long delay, TimeUnit unit) {
		scheduler.schedule(callable, delay, unit);
	}

	public <T> void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		scheduler.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public <T> void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		scheduler.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}
}
