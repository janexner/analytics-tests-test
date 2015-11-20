package com.exner.sel20.test.test1;

import java.util.ArrayList;
import java.util.Iterator;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

	public static void main(String[] args) {
		// Create a new instance of the Firefox driver
		// Notice that the remainder of the code relies on the interface,
		// not the implementation.
		WebDriver driver = new ChromeDriver();

		// And now use this to visit Google
		driver.get("http://www.jan-exner.de");
		// Alternatively the same thing can be done like this
		// driver.navigate().to("http://www.google.com");

		// Find the text input element by its name
		// WebElement element = driver.findElement(By.name("q"));

		// Check the title of the page
		System.out.println("Page title is: " + driver.getTitle());

		// Google's search is rendered dynamically with JavaScript.
		// Wait for the page to load, timeout after 10 seconds
		// (new WebDriverWait(driver, 10)).until(new
		// ExpectedCondition<Boolean>() {
		// public Boolean apply(WebDriver d) {
		// return d.getTitle().toLowerCase().startsWith("cheese!");
		// }
		// });

		try {
			// testing JS injection
			
			// step 1 - check a Data Element
			String deValue = (String) ((JavascriptExecutor) driver).executeScript("return _satellite.getVar('Page Name');");
			System.out.println("Content of %Page Name% DE is: " + deValue);
			
			// step 2 - find which rules have fired
			int numRulesFired = 0;
			ArrayList<ArrayList<String>> elements = (ArrayList<ArrayList<String>>) ((JavascriptExecutor) driver)
					.executeScript("return _satellite.Logger.getHistory();");
			for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
				ArrayList<String> logElement = (ArrayList<String>) iterator.next();
				String logMessage = logElement.get(1);
				if (logMessage.endsWith("fired.")) {
					numRulesFired++;
				}
			}
			System.out.println("A total of " + numRulesFired + " rules fired!");
			// assertion goes here!
		} catch (ClassCastException cce) {
			System.err.println("CCE: " + cce.getLocalizedMessage());
		}

		// Close the browser
		driver.quit();
	}

}
