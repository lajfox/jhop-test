package com.techstar.modules.elfinder.domain;

public enum CheckerType {

	LOCKED(1), READONLY(4), READANDWIRTE(2);

	private int value;

	private CheckerType(int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static CheckerType valueOf(final int value) {
		switch (value) {
		case 1:
			return LOCKED;
		case 4:
			return READONLY;
		case 2:
			return READANDWIRTE;
		}

		return LOCKED;
	}

}
