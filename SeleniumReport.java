package com.ford.fc.glo.autotest.integration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Class which extends the {@link Report} class with a Selenium specific
 * override for taking screenshots
 */
public class SeleniumReport extends Report {

	private FrameworkDriver driver;
	private final SeleniumTestParameters testParameters;

	/**
	 * Constructor to initialize the Report object
	 * 
	 * @param reportSettings
	 *            The {@link ReportSettings} object
	 * @param reportTheme
	 *            The {@link ReportTheme} object
	 * @param testParameters
	 */
	public SeleniumReport(ReportSettings reportSettings, ReportTheme reportTheme,
			SeleniumTestParameters testParameters) {
		super(reportSettings, reportTheme, testParameters);
		this.testParameters = testParameters;
	}

	/**
	 * Function to set the {@link FrameworkDriver} object
	 * 
	 * @param driver
	 *            The {@link FrameworkDriver} object
	 */

	public void setDriver(FrameworkDriver driver) {
		this.driver = driver;
	}

	@Override
	protected void takeScreenshot(String strScreenshotPath) {
		if (driver == null) {
			throw new FrameworkException("Report.driver is not initialized!");
		}
		File scrFile = null;
		switch (testParameters.getExecutionMode()) {
		case LOCAL:
		case REMOTE:
		case GRID:
		case PERFECTO:
			try {

				if ("HtmlUnitDriver".equals(driver.getWebDriver().getClass().getSimpleName())
						|| "class org.openqa.selenium.htmlunit.HtmlUnitDriver"
								.equals(driver.getWebDriver().getClass().getGenericSuperclass().toString())) {
					return; // Screenshots not supported in headless mode
				}

				if ("RemoteWebDriver".equals(driver.getWebDriver().getClass().getSimpleName())) {
					Capabilities capabilities = ((RemoteWebDriver) driver.getWebDriver()).getCapabilities();
					if ("htmlunit".equals(capabilities.getBrowserName())) {
						return; // Screenshots not supported in headless
								// mode
					}
					WebDriver augmentedDriver = new Augmenter().augment(driver.getWebDriver());
					scrFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
				} else {
					scrFile = ((TakesScreenshot) driver.getWebDriver()).getScreenshotAs(OutputType.FILE);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			break;
		}

		try {
			FileUtils.copyFile(scrFile, new File(strScreenshotPath), true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing screenshot to file");
		}

	}

}