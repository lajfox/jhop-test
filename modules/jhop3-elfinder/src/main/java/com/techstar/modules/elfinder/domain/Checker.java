package com.techstar.modules.elfinder.domain;


public class Checker {

	private CheckerType type;

	public Checker(CheckerType type) {
		this.type = type;
	}

	public boolean isRead() {
		return this.type != null && this.type != CheckerType.LOCKED
				&& (this.type == CheckerType.READONLY || this.type == CheckerType.READANDWIRTE);
	}

	public boolean isWrite() {
		return this.type != null && this.type != CheckerType.LOCKED && this.type == CheckerType.READANDWIRTE;
	}

	public boolean isLocked() {
		return this.type == null
				|| (this.type != null && (this.type == CheckerType.LOCKED || this.type == CheckerType.READONLY));
	}

}
