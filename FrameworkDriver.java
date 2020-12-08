package com.ford.fc.glo.autotest.integration;

import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;

//import java.net.URL;
//import java.util.Map;
//import org.openqa.selenium.Capabilities;
//import java.util.logging.Level;
//import org.openqa.selenium.ScreenOrientation;
//import org.openqa.selenium.html5.Location;
//import org.openqa.selenium.interactions.Keyboard;
//import org.openqa.selenium.interactions.Mouse;
//import org.openqa.selenium.remote.CommandExecutor;
//import org.openqa.selenium.remote.ErrorHandler;
//import org.openqa.selenium.remote.ExecuteMethod;
//import org.openqa.selenium.remote.FileDetector;
//import org.openqa.selenium.remote.Response;
//import org.openqa.selenium.remote.SessionId;
//import com.google.gson.JsonObject;
//import org.openqa.selenium.OutputType;



public class FrameworkDriver {

	private SeleniumTestParameters testParameters;
	private SeleniumReport report;
	private WebDriver driver;

	public FrameworkDriver(WebDriver driver) {
		this.driver = driver;
	}

	public SeleniumTestParameters getTestParameters() {
		return testParameters;
	}

	public void setTestParameters(SeleniumTestParameters testParameters) {
		this.testParameters = testParameters;
	}

	public void setRport(SeleniumReport report) {
		this.report = report;
	}

	public WebDriver getWebDriver() {
		return driver;
	}

	// WebDriver Methods
	/**
	 * Function to close the driver Object {@link WebDriver}
	 */
	public void close() {
		driver.close();
	}

	/**
	 * Function to identity the Element
	 * 
	 * @param by
	 *            The locator used to identify the element {@link WebDriver}
	 */
	public boolean equals(Object obj) {
		return driver.equals(obj);
	}

	/**
	 * Function to Find the first {@link WebElement} using the given method.
	 * 
	 * @param by
	 *            The locator used to identify the element {@link WebDriver}
	 */
	public WebElement findElement(By strArg0) {
		return driver.findElement(strArg0);
	}

	/**
	 * Function to Find the first {@link WebElement}, also take screen shot and
	 * wait until the specified element is visible
	 * 
	 * @param by
	 *            The locator used to identify the element {@link WebDriver}
	 */
	public WebElement findElementnTakescreenShot(By strArg0) {
		WebElement element;
		if (driver.findElement(strArg0).isDisplayed() || isElementVisible(strArg0)) {
			report.updateTestLog("Action", "Action Performed Successfully", Status.PASS);
			element = driver.findElement(strArg0);
		} else {
			report.updateTestLog("Action", "Action Failed to Perform, please check the Locators", Status.FAIL);
			element = null;
		}
		return element;
	}

	/**
	 * Function to wait until the specified element is visible
	 * 
	 * @param by
	 *            The locator used to identify the element {@link WebDriver}
	 */
	public boolean isElementVisible(By strArg0) {
		boolean blnElementVisible = false;
		try {
			(new WebDriverWait(driver, 60)).until(ExpectedConditions.visibilityOfElementLocated(strArg0));
			blnElementVisible = true;

		} catch (TimeoutException ex) {
			blnElementVisible = false;
		}
		return blnElementVisible;
	}

	/**
	 * Function to take screen shot and update in report {@link WebDriver}
	 */
	public void capture() {
		report.updateTestLog("Screen Shot", "Screen Shot Captured Successfuly", Status.SCREENSHOT);
	}

	/**
	 * Function to Find all elements within the current page using the given
	 * mechanism
	 * 
	 * @param by
	 *            The locator used to identify the list of elements
	 *            {@link WebDriver}
	 */
	public List<WebElement> findElements(By strArg0) {
		return driver.findElements(strArg0);
	}

	/**
	 * Function to Load a new web page in the current browser window.
	 * {@link WebDriver}
	 */
	public void get(String strstrArg0) {
		driver.get(strstrArg0);
	}

	public Class<?> getClass_Driver() {
		return driver.getClass();
	}

	/**
	 * Function to Get a string representing the current URL that the browser is
	 * looking at. {@link WebDriver}
	 */
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	/**
	 * Function to Get the source of the last loaded page. {@link WebDriver}
	 */
	public String getPageSource() {
		return driver.getPageSource();
	}

	/**
	 * Function to get The title of the current page. {@link WebDriver}
	 */
	public String getTitle() {
		return driver.getTitle();
	}

	/**
	 * Function to Return an opaque handle to this window that uniquely
	 * identifies it within this driver instance {@link WebDriver}
	 */
	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	/**
	 * Function to Return a set of window handles which can be used to iterate
	 * over all open windows of this WebDriver instance by passing them to
	 * {@link switchTo} {@link WebDriver}
	 */
	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	public int hashCode() {
		return driver.hashCode();
	}

	/**
	 * Function to Gets the Option interface. {@link WebDriver}
	 */
	public Options manage() {
		return driver.manage();
	}

	/**
	 * Function to GetAn abstraction allowing the driver to access the browser's
	 * history and to navigate to a given URL. {@link WebDriver}
	 */
	public Navigation navigate() {
		return driver.navigate();
	}

	public void notify_Driver() {
		driver.notify();
	}

	public void notifyAll_Driver() {
		driver.notifyAll();
	}

	/**
	 * Function to Quit this driver, closing every associated window..
	 * {@link WebDriver}
	 */
	public void quit() {
		driver.quit();
	}

	/**
	 * Function to Send future commands to a different frame or window.
	 * {@link WebDriver}
	 */
	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	public String toString() {
		return driver.toString();
	}

	public void wait_Driver() throws InterruptedException {
		driver.wait();
	}

	public void wait_Driver(long lngTimeout) throws InterruptedException {
		driver.wait(lngTimeout);
	}

	public void wait_Driver(long lngTimeout, int intNanos) throws InterruptedException {
		driver.wait(lngTimeout, intNanos);
	}


	// added by venu
	public void hoverOnElement(By arg, By arg2) {
		Actions action = new Actions(driver);
		WebElement elem = findElement(arg);
		action.moveToElement(elem).moveToElement(findElement(arg2)).click().build().perform();
	}
}
