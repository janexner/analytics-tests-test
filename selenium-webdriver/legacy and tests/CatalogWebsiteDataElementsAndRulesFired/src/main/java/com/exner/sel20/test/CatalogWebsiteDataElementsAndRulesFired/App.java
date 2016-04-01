package com.exner.sel20.test.CatalogWebsiteDataElementsAndRulesFired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {
	private static final Logger LOGGER = Logger.getLogger(App.class
			.getSimpleName());

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// container for the resulting matrix
		ArrayList<PageInfoContainer> pages = new ArrayList<PageInfoContainer>();
		JSONArray pagesAsJSONArray = new JSONArray();
		Set<String> dataElementNames = new HashSet<String>();
		Set<String> ruleNames = new HashSet<String>();

		// and go!
		LOGGER.info("Reading list of URLs from file...");
		ArrayList<String> siteURLs = new ArrayList<String>();
		Scanner scanner = null;
		WebDriver driver = null;
		Writer out = null;
		try {
			scanner = new Scanner(new File("urls_all_pages.txt"));
			scanner.useDelimiter("\r\n");
			while (scanner.hasNext()) {
				String url = scanner.next();
				siteURLs.add(url);
			}
			LOGGER.info("Done reading URLs, found " + siteURLs.size());

			LOGGER.info("Opening browser...");
			driver = new ChromeDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;

			LOGGER.info("Crawling site now...");
			for (Iterator<String> iterator = siteURLs.iterator(); iterator
					.hasNext();) {
				String url = iterator.next();
				LOGGER.info("Loading page " + url + "...");
				driver.navigate().to(url);
				String pageName = driver.getTitle();
				LOGGER.fine("Reading Data Elements and values...");
				ArrayList<DataElement> dataElements = new ArrayList<DataElement>();
				JSONArray dataElementsAsJSON = new JSONArray();
				Map<String, Object> test = (Map<String, Object>) js
						.executeScript("if (typeof _satellite !== 'undefined') {return _satellite.dataElements} else {return null};");
				if (null != test) {
					Set<String> keys = test.keySet();
					for (Iterator<String> iterator2 = keys.iterator(); iterator2
							.hasNext();) {
						String deName = iterator2.next();
						Object deValue = js
								.executeScript("if (typeof _satellite !== 'undefined') {return _satellite.getVar('"
										+ deName + "')} else {return ''};");
						String deValueString;
						if (String.class.isAssignableFrom(deValue.getClass())) {
							deValueString = (String) deValue;
						} else if (Long.class.isAssignableFrom(deValue
								.getClass())) {
							deValueString = ((Long) deValue).toString();
						} else {
							deValueString = "weird";
						}
						DataElement de = new DataElement(deName, deValueString, true);
						dataElements.add(de);
						JSONObject deAsJSON = new JSONObject();
						deAsJSON.put("name", deName);
						deAsJSON.put("value", deValueString);
						deAsJSON.put("active", true);
						dataElementsAsJSON.add(deAsJSON);
						dataElementNames.add(deName);
					}
				}

				LOGGER.fine("Reading DTM log history...");
				ArrayList<Rule> rulesFired = new ArrayList<Rule>();
				JSONArray rulesFiredAsJSON = new JSONArray();
				ArrayList<ArrayList<String>> logEntries = (ArrayList<ArrayList<String>>) ((JavascriptExecutor) driver)
						.executeScript("if (typeof _satellite !== 'undefined') {return _satellite.Logger.getHistory()} else {return []};");
				for (Iterator<ArrayList<String>> iterator2 = logEntries
						.iterator(); iterator2.hasNext();) {
					ArrayList<String> logEntry = iterator2.next();
					String logMessage = logEntry.get(1);
					if (logMessage.startsWith("Rule")
							&& logMessage.endsWith("fired.")) {
						String ruleName = logMessage.replace("Rule \"", "")
								.replace("\" fired.", "");
						LOGGER.fine("Adding " + ruleName
								+ " to list of fired rules...");
						Rule rule = new Rule(RuleType.PLR, ruleName, true, true);
						rulesFired.add(rule);
						JSONObject ruleAsJSON = new JSONObject();
						ruleAsJSON.put("name", ruleName);
						ruleAsJSON.put("active", true);
						rulesFiredAsJSON.add(ruleAsJSON);
						ruleNames.add(ruleName);
					}
				}
				LOGGER.fine("Total rules fired: " + rulesFired.size());
				PageInfoContainer pageInfo = new PageInfoContainer(pageName,
						url, dataElements, rulesFired);
				pages.add(pageInfo);
				JSONObject pageInfoAsJSON = new JSONObject();
				pageInfoAsJSON.put("name", pageName);
				pageInfoAsJSON.put("url", url);
				pageInfoAsJSON.put("dataElements", dataElementsAsJSON);
				pageInfoAsJSON.put("pageLoadRules", rulesFiredAsJSON);
				pagesAsJSONArray.add(pageInfoAsJSON);
			}
			// list all pages
			System.out.println("Listing all pages:");
			for (Iterator<PageInfoContainer> iterator = pages.iterator(); iterator
					.hasNext();) {
				PageInfoContainer pageInfo = iterator.next();
				System.out.println("Page: " + pageInfo.getName());
				System.out.println("      " + pageInfo.getPageURL());
				ArrayList<DataElement> des = (ArrayList<DataElement>) pageInfo.getDataElements();
				System.out.println("    Data Elements: " + des.size());
				for (Iterator<DataElement> iterator2 = des.iterator(); iterator2
						.hasNext();) {
					DataElement dataElement = iterator2.next();
					System.out.println("        " + dataElement.getName()
							+ " => " + dataElement.getValue());
				}
				ArrayList<Rule> rules = (ArrayList<Rule>) pageInfo.getRules();
				System.out.println("    Rules that fired: " + rules.size());
				for (Iterator<Rule> iterator2 = rules.iterator(); iterator2
						.hasNext();) {
					Rule rule = iterator2.next();
					System.out.println("          " + rule.getName());
				}
			}
			// save this as JSON
			JSONObject resultJSON = new JSONObject();
			resultJSON.put("pages", pagesAsJSONArray);
			resultJSON.put("dataElements",
					convertStringSetToJSONArray(dataElementNames));
			resultJSON.put("pageLoadRules",
					convertStringSetToJSONArray(ruleNames));
			out = new BufferedWriter(new FileWriter("pageInfo.json"));
			resultJSON.writeJSONString(out);
			System.out.print("done.");
		} catch (FileNotFoundException e) {
			LOGGER.severe("Error: " + e.getLocalizedMessage());
		} catch (IOException e) {
			LOGGER.severe("Error writing JSON result: "
					+ e.getLocalizedMessage());
		} finally {
			if (null != scanner) {
				LOGGER.info("Closing scanner...");
				scanner.close();
			}
			if (null != driver) {
				driver.close();
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					LOGGER.warning("Unable to close writer: "
							+ e.getLocalizedMessage());
				}
			}
		}
		LOGGER.info("done.");
	}

	private static Object convertStringSetToJSONArray(
			Set<String> dataElementNames) {
		JSONArray result = new JSONArray();
		// loop over the Set
		for (Iterator<String> iterator = dataElementNames.iterator(); iterator
				.hasNext();) {
			String de = iterator.next();
			result.add(de);
		}
		return result;
	}
}
