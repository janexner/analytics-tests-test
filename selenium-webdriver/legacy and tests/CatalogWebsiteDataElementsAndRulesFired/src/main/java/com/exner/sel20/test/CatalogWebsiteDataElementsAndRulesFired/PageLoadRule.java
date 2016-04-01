package com.exner.sel20.test.CatalogWebsiteDataElementsAndRulesFired;

public class Rule {
	private final RuleType ruleType;
	private final String name;
	private final boolean hasFired;
	private final boolean active;
	
	public Rule(RuleType ruleType, String name, boolean hasFired, boolean active) {
		this.ruleType = ruleType;
		this.name = name;
		this.hasFired = hasFired;
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

	public boolean isHasFired() {
		return hasFired;
	}
}
