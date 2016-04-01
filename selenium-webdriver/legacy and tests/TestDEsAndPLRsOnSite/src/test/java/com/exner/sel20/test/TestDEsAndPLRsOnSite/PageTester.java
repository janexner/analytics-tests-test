package com.exner.sel20.test.TestDEsAndPLRsOnSite;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class PageTester {
	private final PageTestDefinition pageTestDefinition;
	private final WebDriver driver;
	private final JavascriptExecutor js;

	public PageTester(PageTestDefinition pageTestDefinition) {
		this.driver = new PhantomJSDriver();
		this.pageTestDefinition = pageTestDefinition;
		js = (JavascriptExecutor) driver;
	}

	public void runTest() {
		// test page title
		driver.get(pageTestDefinition.getUrl().toString());
		assertEquals(
				"Page title should be " + pageTestDefinition.getPageTitle(),
				pageTestDefinition.getPageTitle(), driver.getTitle());
		// test availability of DTM
		Object dtmCheckResult = js
				.executeScript("if (typeof _satellite !== 'undefined') { return true } else { return false }");
		if (Boolean.class.isAssignableFrom(dtmCheckResult.getClass())) {
			assertTrue("DTM Library is loaded", (Boolean) dtmCheckResult);
		} else if (String.class.isAssignableFrom(dtmCheckResult.getClass())) {
			assertEquals("DTM Library is loaded", "true",
					(String) dtmCheckResult);
		}

		// test data element values
		Set<DataElement> des = pageTestDefinition.getDataElements();
		for (Iterator<DataElement> iterator = des.iterator(); iterator
				.hasNext();) {
			DataElement dataElement = (DataElement) iterator.next();
			if (dataElement.isActive()) {
				Object deValue = getValueOfDataElement(dataElement.getName());
				// TODO might need to take class into account here!
				String deValueString;
				if (String.class.isAssignableFrom(deValue.getClass())) {
					deValueString = (String) deValue;
				} else if (Long.class.isAssignableFrom(deValue.getClass())) {
					deValueString = ((Long) deValue).toString();
				} else {
					deValueString = "weird";
				}
				assertEquals("Value of %" + dataElement.getName()
						+ "% should be: " + dataElement.getValue(),
						dataElement.getValue(), deValueString);
			}
		}
		// test that all page load rules have fired that should have fired
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<String>> logEntries = (ArrayList<ArrayList<String>>) ((JavascriptExecutor) driver)
				.executeScript("return _satellite.Logger.getHistory()");
		List<String> rulesThatFired = new ArrayList<String>();
		for (Iterator<ArrayList<String>> iterator = logEntries.iterator(); iterator
				.hasNext();) {
			ArrayList<String> arrayList = (ArrayList<String>) iterator.next();
			String logMessage = arrayList.get(1);
			if (logMessage.startsWith("Rule ") && logMessage.endsWith("fired.")) {
				rulesThatFired.add(logMessage.replace("Rule \"", "").replace(
						"\" fired.", ""));
			}
		}
		int numberOfRulesFired = rulesThatFired.size();
		Set<Rule> pageLoadRules = pageTestDefinition.getPageLoadRules();
//		assertEquals(
//				"Number of PLRs on this page should be " + pageLoadRules.size(),
//				pageLoadRules.size(), numberOfRulesFired);
		int ruleMatches = 0;
		for (Iterator<Rule> iterator = pageLoadRules.iterator(); iterator
				.hasNext();) {
			Rule rule = (Rule) iterator.next();
			if (rule.isActive()) {
				assertTrue("PLR " + rule.getName() + " should have fired",
						rulesThatFired.contains(rule.getName()));
				ruleMatches++;
			}
		}
		assertTrue("Found all PLRs that should have fired",
				numberOfRulesFired >= ruleMatches);
		
		// close browser
		this.driver.close();
	}

	private Object getValueOfDataElement(String dataElementName) {
		Object result = js.executeScript("return _satellite.getVar('"
				+ dataElementName + "');");
		return result;
	}

}
