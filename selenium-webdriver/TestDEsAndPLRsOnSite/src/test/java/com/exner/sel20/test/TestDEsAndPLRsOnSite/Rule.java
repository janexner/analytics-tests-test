package com.exner.sel20.test.TestDEsAndPLRsOnSite;

public class Rule {
	private final RuleType ruleType;
	private final String name;
	private final boolean active;

	public Rule(RuleType ruleType, String name, boolean active) {
		this.ruleType = ruleType;
		this.name = name;
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public String getName() {
		return name;
	}
}
