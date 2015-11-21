package com.exner.sel20.test.CatalogWebsiteDataElementsAndRulesFired;

import java.util.ArrayList;

public class PageInfoContainer {
	private final String pageURL;
	private final String name;
	private final ArrayList<DataElement> dataElements;
	private final ArrayList<Rule> rules;

	public PageInfoContainer(String name, String url,
			ArrayList<DataElement> dataElements, ArrayList<Rule> rules) {
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

	public ArrayList<DataElement> getDataElements() {
		return dataElements;
	}

	public ArrayList<Rule> getRules() {
		return rules;
	}
}
