package com.techstar.modules.springframework.web.servlet.handler;

import com.techstar.modules.springframework.data.jpa.domain.Results;

public final class ErrorConext {

	private static final ThreadLocal<Results> context = new ThreadLocal<Results>();

	private ErrorConext() {
	}

	public static Results get() {
		Results results = context.get();
		remove();
		return results;
	}

	public static void set(Results results) {
		context.set(results);
	}

	public static void remove() {
		context.remove();
	}
}
