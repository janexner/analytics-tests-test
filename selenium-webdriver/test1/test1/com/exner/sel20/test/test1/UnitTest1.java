package com.exner.sel20.test.test1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class UnitTest1 {
	private WebDriver _driver;

	@Before
	public void setUp() throws Exception {
		this._driver = new FirefoxDriver();
	}

	@After
	public void tearDown() throws Exception {
		this._driver.quit();
	}

	@Test
	public void test() {
		this._driver.get("http://www.jan-exner.de/");
		assertEquals("jan-exner.de | Home", this._driver.getTitle());
	}

}
