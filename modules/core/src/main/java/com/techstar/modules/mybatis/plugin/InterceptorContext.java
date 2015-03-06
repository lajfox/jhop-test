package com.techstar.modules.mybatis.plugin;

import org.hibernate.dialect.pagination.LimitHandler;

import com.techstar.modules.mybatis.domain.Page;

final class InterceptorContext {

	private static final ThreadLocal<Page<?>> PAGECONTEXT = new ThreadLocal<Page<?>>();
	private static final ThreadLocal<LimitHandler> LIMITCONTEXT = new ThreadLocal<LimitHandler>();
	private static final ThreadLocal<Integer> INDEXCONTEXT = new ThreadLocal<Integer>();

	private InterceptorContext() {
	}

	public static Page<?> getPage() {
		Page<?> page = PAGECONTEXT.get();
		removePage();
		return page;
	}

	public static void setPage(Page<?> page) {
		PAGECONTEXT.set(page);
	}

	public static void removePage() {
		PAGECONTEXT.remove();
	}

	public static LimitHandler getLimitHandler() {
		LimitHandler limitHandler = LIMITCONTEXT.get();
		removeLimitHandler();
		return limitHandler;
	}

	public static void setLimitHandler(LimitHandler limitHandler) {
		LIMITCONTEXT.set(limitHandler);
	}

	public static void removeLimitHandler() {
		LIMITCONTEXT.remove();
	}

	public static Integer getIndex() {
		Integer index = INDEXCONTEXT.get();
		removeIndex();
		return index;
	}

	public static void setIndex(Integer index) {
		INDEXCONTEXT.set(index);
	}

	public static void removeIndex() {
		INDEXCONTEXT.remove();
	}
}
