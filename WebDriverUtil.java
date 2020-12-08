package com.ford.fc.glo.autotest.integration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

/**
 * Class containing useful WebDriver utility functions
 */
public class WebDriverUtil {
	private FrameworkDriver driver;

	/**
	 * Constructor to initialize the {@link WebDriverUtil} object
	 * 
	 * @param driver
	 *            The {@link WebDriver} object
	 */
	public WebDriverUtil(FrameworkDriver driver) {
		this.driver = driver;
	}

	/**
	 * Function to pause the execution for the specified time period
	 * 
	 * @param milliSeconds
	 *            The wait time in milliseconds
	 */
	public void waitFor(long lngMilliSeconds) {
		try {
			Thread.sleep(lngMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Function to wait until the page loads completely
	 * 
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 */
	public void waitUntilPageLoaded(long lngTimeOutInSeconds) {
		WebElement oldPage = driver.findElement(By.tagName("html"));

		(new WebDriverWait(driver.getWebDriver(), lngTimeOutInSeconds)).until(ExpectedConditions.stalenessOf(oldPage));

	}

	/**
	 * Function to wait until the page readyState equals 'complete'
	 * 
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 */
	public void waitUntilPageReadyStateComplete(long lngTimeOutInSeconds) {
		ExpectedCondition<Boolean> pageReadyStateComplete = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};

		(new WebDriverWait(driver.getWebDriver(), lngTimeOutInSeconds)).until(pageReadyStateComplete);
	}

	/**
	 * Function to wait until the specified element is located
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 */
	public void waitUntilElementLocated(By by, long lngTimeOutInSeconds) {
		(new WebDriverWait(driver.getWebDriver(), lngTimeOutInSeconds))
				.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	/**
	 * Function to wait until the specified element is visible
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 */
	public void waitUntilElementVisible(By by, long lngTimeOutInSeconds) {
		(new WebDriverWait(driver.getWebDriver(), lngTimeOutInSeconds))
				.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	/**
	 * Function to wait until the specified element is enabled
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 */
	public void waitUntilElementEnabled(By by, long lngTimeOutInSeconds) {
		(new WebDriverWait(driver.getWebDriver(), lngTimeOutInSeconds)).until(ExpectedConditions.elementToBeClickable(by));
	}

	/**
	 * Function to wait until the specified element is disabled
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds
	 *            The wait timeout in seconds
	 */
	public void waitUntilElementDisabled(By by, long lngTimeOutInSeconds) {
		(new WebDriverWait(driver.getWebDriver(), lngTimeOutInSeconds))
				.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(by)));
	}

	/**
	 * Function to select the specified value from a listbox
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the listbox
	 * @param item
	 *            The value to be selected within the listbox
	 */
	public void selectListItem(By by, String strItem) {
		Select dropDownList = new Select(driver.findElement(by));
		dropDownList.selectByVisibleText(strItem);
	}

	/**
	 * Function to do a mouseover on top of the specified element
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 */
	public void mouseOver(By by) {
		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(driver.findElement(by)).build().perform();
	}

	/**
	 * Function to verify whether the specified object exists within the current
	 * page
	 * 
	 * @param by
	 *            The {@link WebDriver} locator used to identify the element
	 * @return Boolean value indicating whether the specified object exists
	 */
	public Boolean objectExists(By by) {
		return !driver.findElements(by).isEmpty();
	}

	/**
	 * Function to verify whether the specified text is present within the
	 * current page
	 * 
	 * @param textPattern
	 *            The text to be verified
	 * @return Boolean value indicating whether the specified test is present
	 */
	public Boolean isTextPresent(String strTextPattern) {
		return driver.findElement(By.cssSelector("BODY")).getText().matches(strTextPattern);
	}

	/**
	 * Function to check if an alert is present on the current page
	 * 
	 * @param timeOutInSeconds
	 *            The number of seconds to wait while checking for the alert
	 * @return Boolean value indicating whether an alert is present
	 */
	public Boolean isAlertPresent(long lngTimeOutInSeconds) {
		try {
			new WebDriverWait(driver.getWebDriver(), lngTimeOutInSeconds).until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (TimeoutException ex) {
			return false;
		}
	}
}