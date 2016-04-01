package com.exner.sel20.test.TestDEsAndPLRsOnSite;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class SiteTest {
	private static final Logger LOGGER = Logger.getLogger(SiteTest.class
			.getSimpleName());

	@Test
	public void testSite() {
		// load test description from JSON
		String jsonFileName = System.getProperty("test.description.file");
		JSONParser parser = new JSONParser();
		try {
			LOGGER.info("Loading test description...");
			JSONObject testDescription = (JSONObject) parser
					.parse(new BufferedReader(new FileReader(jsonFileName)));
			LOGGER.info("done.");
			JSONArray pagesToTest = (JSONArray) testDescription.get("pages");
			for (Iterator<JSONObject> iterator = pagesToTest.iterator(); iterator
					.hasNext();) {
				JSONObject page = iterator.next();
				boolean pageActive = true;
				Object activeTest = page.get("active");
				if (null != activeTest) {
					pageActive = (Boolean) activeTest;
				}
				boolean hasActiveFlag = page.containsKey("active");
				if (pageActive || !hasActiveFlag) {
					JSONArray des = (JSONArray) page.get("dataElements");
					Set<DataElement> dataElementList = new HashSet<DataElement>();
					for (Iterator<JSONObject> iterator2 = des.iterator(); iterator2
							.hasNext();) {
						JSONObject deJSON = iterator2.next();
						String name = (String) deJSON.get("name");
						String value = (String) deJSON.get("value");
						boolean active = (Boolean) deJSON.get("active");
						DataElement de = new DataElement(name, value, active);
						dataElementList.add(de);
					}
					JSONArray plrs = (JSONArray) page.get("pageLoadRules");
					Set<Rule> pageLoadRuleList = new HashSet<Rule>();
					for (Iterator<JSONObject> iterator2 = plrs.iterator(); iterator2
							.hasNext();) {
						JSONObject plrJSON = (JSONObject) iterator2.next();
						String name = (String) plrJSON.get("name");
						boolean active = (Boolean) plrJSON.get("active");
						Rule plr = new Rule(RuleType.PLR, name, active);
						pageLoadRuleList.add(plr);
					}
					String pageTitle = (String) page.get("name");
					String urlString = (String) page.get("url");
					URL url = new URL(urlString);
					PageTestDefinition ptd = new PageTestDefinition(url,
							pageTitle, dataElementList, pageLoadRuleList);
					// now test this page
					LOGGER.info("Now testing page " + pageTitle);
					PageTester tester = new PageTester(ptd);
					tester.runTest();
					LOGGER.info("done.");
				} else {
					LOGGER.info("Not testing page " + (String) page.get("name")
							+ ".");
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.severe("Unable to open test description: "
					+ e.getLocalizedMessage());
		} catch (IOException e) {
			LOGGER.severe("Unable to open test description: "
					+ e.getLocalizedMessage());
		} catch (ParseException e) {
			LOGGER.severe("Test description format is not ok: "
					+ e.getLocalizedMessage());
		}
	}
}
