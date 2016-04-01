package com.exner.sel20.test.test3;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestPageName {
	private String baseUrl;
	private WebDriver driver;
	private ScreenshotHelper screenshotHelper;

	@Before
	public void openBrowser() {
		baseUrl = System.getProperty("webdriver.base.url");
		driver = new ChromeDriver();
		driver.get(baseUrl);
		screenshotHelper = new ScreenshotHelper();
	}

	@After
	public void saveScreenshotAndCloseBrowser() throws IOException {
		screenshotHelper.saveScreenshot("screenshot.png");
		driver.quit();
	}
	
	@Test
	public void pageNameTest() {
		assertEquals("The page title should be jan-exner.de | Home",
				"jan-exner.de | Home", driver.getTitle());
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
