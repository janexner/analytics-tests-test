package com.exner.sel20.test.TestClickAndReactions;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

public class TestRun {
	WebDriver driver = null;
	JavascriptExecutor js = null;

	@Before
	public void setup() {
		driver = new PhantomJSDriver();
		driver.get("http://www.jan-exner.de/astro");
		js = (JavascriptExecutor) driver;
	}

	@Test
	public void test() {
		// wait time after simulated click
		String waitTimeString = System.getProperty("test.wait.time");
		long waitTime = Long.parseLong(waitTimeString);
		System.out.println("wait time = " + waitTime);
		
		List<WebElement> figures = driver.findElements(By
				.cssSelector("div.item figure"));
		assertTrue("there should be some figures/images on this page",
				figures.size() > 0);

		// click on image 3
		Actions click = new Actions(driver);
		click = new Actions(driver);
		click.click(figures.get(2));
		click.perform();

		// wait for tracking call...
		// TODO bogus! the page has been tracked, so this will always be true
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(new Predicate<WebDriver>() {
			public boolean apply(WebDriver driver) {
				boolean res = (Boolean) ((JavascriptExecutor) driver)
						.executeScript("return typeof s_i_jexnertest !== 'undefined'");
				return res;
			}
		});
		
		// so just wait
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			// whatever
		}

		// check DTM log for firing of rule "fancyBoxFotoLoad"
		@SuppressWarnings("unchecked")
		ArrayList<ArrayList<String>> logEntries = (ArrayList<ArrayList<String>>) ((JavascriptExecutor) driver)
				.executeScript("return _satellite.Logger.getHistory()");
		boolean hasDCRFiredfancyBoxFotoLoadFired = false;
		for (Iterator<ArrayList<String>> iterator = logEntries.iterator(); iterator
				.hasNext();) {
			ArrayList<String> arrayList = (ArrayList<String>) iterator.next();
			String logMessage = arrayList.get(1);
			System.out.println("log message: " + logMessage);
			if (logMessage.contains("fancyBoxFotoLoad")) {
				hasDCRFiredfancyBoxFotoLoadFired = true;
				break;
			}
		}
		assertTrue("DCR 'fancyBoxFotoLoad' should have fired",
				hasDCRFiredfancyBoxFotoLoadFired);
	}

	@After
	public void tearDown() {
		if (null != driver) {
			driver.close();
		}
	}
}
