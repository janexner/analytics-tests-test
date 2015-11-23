package com.exner.sel20.test.CatalogWebsiteDataElementsAndRulesFired;

import java.util.List;

public class PageInfoContainer {
	private final String pageURL;
	private final String name;
	private final List<DataElement> dataElements;
	private final List<Rule> rules;

	public PageInfoContainer(String name, String url,
			List<DataElement> dataElements, List<Rule> rules) {
		this.name = name;
		this.pageURL = url;
		this.dataElements = dataElements;
		this.rules = rules;
	}

	public String getName() {
		return name;
	}

	public String getPageURL() {
		return pageURL;
	}

	public List<DataElement> getDataElements() {
		return dataElements;
	}

	public List<Rule> getRules() {
		return rules;
	}
}
