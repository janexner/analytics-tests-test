package com.exner.sel20.test.TestDEsAndPLRsOnSite;

import java.net.URL;
import java.util.Set;

public class PageTestDefinition {
	private final URL url;
	private final String pageTitle;
	private final Set<DataElement> dataElements;
	private final Set<Rule> pageLoadRules;
	
	public PageTestDefinition(URL url, String pageTitle, Set<DataElement> dataElements, Set<Rule> pageLoadRules) {
		this.url = url;
		this.pageTitle = pageTitle;
		this.dataElements = dataElements;
		this.pageLoadRules = pageLoadRules;
	}

	public URL getUrl() {
		return url;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public Set<DataElement> getDataElements() {
		return dataElements;
	}

	public Set<Rule> getPageLoadRules() {
		return pageLoadRules;
	}
}
