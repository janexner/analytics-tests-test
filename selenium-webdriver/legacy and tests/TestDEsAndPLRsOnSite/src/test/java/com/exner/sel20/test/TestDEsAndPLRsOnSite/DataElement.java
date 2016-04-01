package com.exner.sel20.test.TestDEsAndPLRsOnSite;

public class DataElement {
	private final String name;
	private final String value;
	private final boolean active;
	
	public DataElement(String name, String value, boolean active) {
		this.name = name;
		this.value = value;
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
