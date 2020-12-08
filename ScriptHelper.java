package com.ford.fc.glo.autotest.integration;

import org.openqa.selenium.WebDriver;

/**
 * Wrapper class for common framework objects, to be used across the entire test
 * case and dependent libraries
 */
public class ScriptHelper {

	private final FrameworkDataTable dataTable;
	private final SeleniumReport report;
	private FrameworkDriver frameworkDriver;
	private WebDriverUtil driverUtil;

	/**
	 * Constructor to initialize all the objects wrapped by the
	 * {@link ScriptHelper} class
	 * 
	 * @param dataTable
	 *            The {@link FrameworkDataTable} object
	 * @param report
	 *            The {@link SeleniumReport} object
	 * @param driver
	 *            The {@link WebDriver} object
	 * @param driverUtil
	 *            The {@link WebDriverUtil} object
	 */

	public ScriptHelper(FrameworkDataTable dataTable, SeleniumReport report, FrameworkDriver frameworkDriver,
			WebDriverUtil driverUtil) {
		this.dataTable = dataTable;
		this.report = report;
		this.frameworkDriver = frameworkDriver;
		this.driverUtil = driverUtil;
	}

	/**
	 * Function to get the {@link FrameworkDataTable} object
	 * 
	 * @return The {@link FrameworkDataTable} object
	 */
	public FrameworkDataTable getDataTable() {
		return dataTable;
	}

	/**
	 * Function to get the {@link SeleniumReport} object
	 * 
	 * @return The {@link SeleniumReport} object
	 */
	public SeleniumReport getReport() {
		return report;
	}

	/**
	 * Function to get the {@link FrameworkDriver} object
	 * 
	 * @return The {@link FrameworkDriver} object
	 */
	public FrameworkDriver getFrameworkDriver() {
		return frameworkDriver;
	}

	/**
	 * Function to get the {@link WebDriverUtil} object
	 * 
	 * @return The {@link WebDriverUtil} object
	 */
	public WebDriverUtil getDriverUtil() {
		return driverUtil;
	}

}