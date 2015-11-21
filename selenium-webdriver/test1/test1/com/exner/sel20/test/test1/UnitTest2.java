package com.exner.sel20.test.test1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class UnitTest2 {
	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		this.driver = new ChromeDriver();
	}

	@After
	public void tearDown() throws Exception {
		this.driver.quit();
	}

	@Test
	public void test() {
		this.driver.get("http://www.jan-exner.de/");
		assertEquals("jan-exner.de | Home", this.driver.getTitle());
	}

}
