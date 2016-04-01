package com.exner.sel20.test.test3;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestDataElements {
	private String baseUrl;
	private WebDriver driver;
	JavascriptExecutor js;
	private ScreenshotHelper screenshotHelper;

	@Before
	public void openBrowser() {
		baseUrl = System.getProperty("webdriver.base.url");
		if (null == baseUrl) {
			// fallback
			baseUrl = "http://www.jan-exner.de/";
		}
		driver = new ChromeDriver();
		driver.get(baseUrl);
		js = (JavascriptExecutor) driver;
		screenshotHelper = new ScreenshotHelper();
	}

	@After
	public void saveScreenshotAndCloseBrowser() throws IOException {
		screenshotHelper.saveScreenshot("screenshot.png");
		driver.quit();
	}

	@Test
	public void testDataElementValues() {
		try {
			List<ArrayList<String>> dataElementsAndValues = getDataElementsAndValues();
			for (Iterator<ArrayList<String>> iterator = dataElementsAndValues
					.iterator(); iterator.hasNext();) {
				ArrayList<String> arrayList = (ArrayList<String>) iterator
						.next();
				String dataElementName = arrayList.get(0);
				if (arrayList.size() == 1) {
					// this Data Element should be empty
					assertEquals("Value of %" + dataElementName
							+ "% should be empty",
							"",
							getValueOfDataElement(dataElementName));
				} else {
					// the second element is the expected value for this Data
					// Element
					assertEquals("Value of %" + dataElementName
							+ "% should be: " + arrayList.get(1),
							arrayList.get(1),
							getValueOfDataElement(dataElementName));
				}
			}
		} catch (FileNotFoundException e) {
			fail("Failed: " + e.getLocalizedMessage());
		}
	}

	private String getValueOfDataElement(String dataElementName) {
		Object result = js.executeScript("return _satellite.getVar('"
				+ dataElementName + "');");
		return (String) result;
	}

	public List<ArrayList<String>> getDataElementsAndValues()
			throws FileNotFoundException {
		List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		Scanner scanner = new Scanner(new File("test_dataelement_values.csv"));
		scanner.useDelimiter("\r\n");
		while (scanner.hasNext()) {
			String line = scanner.next();
			if (!line.startsWith("#")) {
				String[] values = line.split(",");
				result.add(new ArrayList<String>(Arrays.asList(values)));
			}
		}
		scanner.close();
		return result;
	}

	private class ScreenshotHelper {
		public void saveScreenshot(String screenshotFileName)
				throws IOException {
			File screenshot = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(screenshotFileName));
		}
	}
}
