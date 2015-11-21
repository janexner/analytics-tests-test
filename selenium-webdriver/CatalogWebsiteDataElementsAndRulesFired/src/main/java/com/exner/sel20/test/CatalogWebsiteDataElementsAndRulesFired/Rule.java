package com.exner.sel20.test.CatalogWebsiteDataElementsAndRulesFired;

public class Rule {
	private final RuleType ruleType;
	private final String name;
	private final boolean hasFired;
	
	public Rule(RuleType ruleType, String name, boolean hasFired) {
		this.ruleType = ruleType;
		this.name = name;
		this.hasFired = hasFired;
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
