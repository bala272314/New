package com.ford.fc.glo.autotest.integration;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Class to encapsulate the reporting settings in the framework
 */
public class ReportSettings {
	private final String strReportPath;
	private final String strReportName;

	private String strProjectName;
	private int intLogLevel;
	private String strDateFormatString;

	private boolean blnGenerateExcelReports;
	private boolean blnGenerateHtmlReports;

	private boolean blnTakeScreenshotFailedStep;
	private boolean blnTakeScreenshotPassedStep;

	private boolean blnLinkScreenshotsToTestLog;
	private boolean blnLinkTestLogsToSummary;

	private boolean blnConsolidateScreenshotsInWordDoc;
	private boolean blnConsolidateScreenshotsInPDF;

	private boolean blnIsMobileExecution;

	/**
	 * Constructor to initialize the report settings
	 * 
	 * @param reportPath
	 *            The report path
	 * @param reportName
	 *            The report name
	 */
	public ReportSettings(String strReportPath, String strReportName) {
		boolean reportPathExists = new File(strReportPath).isDirectory();
		if (!reportPathExists) {
			throw new FrameworkException(
					"The given report path does not exist!");
		}
		this.strReportPath = strReportPath;
		this.strReportName = strReportName;

		// Set default values for all the report settings
		strProjectName = "";
		intLogLevel = 4;
		blnGenerateExcelReports = true;
		blnGenerateHtmlReports = true;
		blnTakeScreenshotFailedStep = true;
		blnTakeScreenshotPassedStep = false;
		blnLinkScreenshotsToTestLog = true;
		blnLinkTestLogsToSummary = true;
		blnConsolidateScreenshotsInWordDoc = false;
		blnConsolidateScreenshotsInPDF = false;
		strDateFormatString = "dd-MMM-yyyy hh:mm:ss a";
		blnIsMobileExecution = false;
	}

	/**
	 * Function to get the absolute path where the report is to be stored
	 * 
	 * @return The report path
	 */
	public String getReportPath() {
		return strReportPath;
	}

	/**
	 * Function to get the name of the report
	 * 
	 * @return The report name
	 */
	public String getReportName() {
		return strReportName;
	}

	/**
	 * Function to get the name of the project being automated
	 * 
	 * @return The project name
	 */
	public String getProjectName() {
		return strProjectName;
	}

	/**
	 * Function to set the name of the project being automated
	 * 
	 * @param projectName
	 *            The project name
	 */
	public void setProjectName(String strProjectName) {
		this.strProjectName = strProjectName;
	}

	/**
	 * Function to get the logging level of the reports. Log levels range
	 * between 0 to 5, with 0 being minimal reporting and 5 being highly
	 * detailed reporting
	 * 
	 * @return The log level
	 */
	public int getLogLevel() {
		return intLogLevel;
	}

	/**
	 * Function to set the logging level of the reports. Log levels range
	 * between 0 to 5, with 0 being minimal reporting and 5 being highly
	 * detailed reporting
	 * 
	 * @param logLevel
	 *            The log level
	 */
	public void setLogLevel(int intLogLevel) {
		this.intLogLevel = intLogLevel;

		if (intLogLevel < 0) {
			this.intLogLevel = 0;
		}

		if (intLogLevel > 5) {
			this.intLogLevel = 5;
		}
	}

	/**
	 * Function to get a string indicating the format for the date/time to be
	 * used within the report
	 * 
	 * @return The date/time formatting string
	 * @see SimpleDateFormat
	 */
	public String getDateFormatString() {
		return strDateFormatString;
	}

	/**
	 * Function to set a string indicating the format for the date/time to be
	 * used within the report
	 * 
	 * @param dateFormatString
	 *            The date/time formatting string
	 * @see SimpleDateFormat
	 */
	public void setDateFormatString(String strDateFormatString) {
		this.strDateFormatString = strDateFormatString;
	}

	/**
	 * Function to get a Boolean value indicating whether Excel reports should
	 * be generated
	 * 
	 * @return Boolean value indicating whether Excel reports should be
	 *         generated
	 */
	public boolean shouldGenerateExcelReports() {
		return blnGenerateExcelReports;
	}

	/**
	 * Function to set a Boolean value indicating whether Excel reports should
	 * be generated
	 * 
	 * @param generateExcelReports
	 *            Boolean value indicating whether Excel reports should be
	 *            generated
	 */
	public void setGenerateExcelReports(boolean blnGenerateExcelReports) {
		this.blnGenerateExcelReports = blnGenerateExcelReports;
	}

	/**
	 * Function to get a Boolean value indicating whether HTML reports should be
	 * generated
	 * 
	 * @return Boolean value indicating whether HTML reports should be generated
	 */
	public boolean shouldGenerateHtmlReports() {
		return blnGenerateHtmlReports;
	}

	/**
	 * Function to set a Boolean value indicating whether HTML reports should be
	 * generated
	 * 
	 * @param generateHtmlReports
	 *            Boolean value indicating whether HTML reports should be
	 *            generated
	 */
	public void setGenerateHtmlReports(boolean blnGenerateHtmlReports) {
		this.blnGenerateHtmlReports = blnGenerateHtmlReports;
	}

	/**
	 * Function to get a Boolean value indicating whether a screenshot should be
	 * captured for failed steps
	 * 
	 * @return Boolean value indicating whether a screenshot should be captured
	 *         for failed steps
	 */
	public boolean shouldTakeScreenshotFailedStep() {
		return blnTakeScreenshotFailedStep;
	}

	/**
	 * Function to set a Boolean value indicating whether a screenshot should be
	 * captured for failed steps
	 * 
	 * @param takeScreenshotFailedStep
	 *            Boolean value indicating whether a screenshot should be
	 *            captured for failed steps
	 */
	public void setTakeScreenshotFailedStep(boolean blnTakeScreenshotFailedStep) {
		this.blnTakeScreenshotFailedStep = blnTakeScreenshotFailedStep;
	}

	/**
	 * Function to get a Boolean value indicating whether a screenshot should be
	 * captured for passed steps
	 * 
	 * @return Boolean value indicating whether a screenshot should be captured
	 *         for passed steps
	 */
	public boolean shouldTakeScreenshotPassedStep() {
		return blnTakeScreenshotPassedStep;
	}

	/**
	 * Function to set a Boolean value indicating whether a screenshot should be
	 * captured for passed steps
	 * 
	 * @param takeScreenshotPassedStep
	 *            Boolean value indicating whether a screenshot should be
	 *            captured for passed steps
	 */
	public void setTakeScreenshotPassedStep(boolean blnTakeScreenshotPassedStep) {
		this.blnTakeScreenshotPassedStep = blnTakeScreenshotPassedStep;
	}

	/**
	 * Function to get a Boolean value indicating whether any screenshot taken
	 * must be linked to the corresponding step within the test log
	 * 
	 * @return Boolean value indicating whether any screenshot taken must be
	 *         linked to the corresponding step within the test log
	 */
	public boolean shouldLinkScreenshotsToTestLog() {
		return blnLinkScreenshotsToTestLog;
	}

	/**
	 * Function to set a Boolean value indicating whether any screenshot taken
	 * must be linked to the corresponding step within the test log
	 * 
	 * @param linkScreenshotsToTestLog
	 *            Boolean value indicating whether any screenshot taken must be
	 *            linked to the corresponding step within the test log
	 */
	public void setLinkScreenshotsToTestLog(boolean blnLinkScreenshotsToTestLog) {
		this.blnLinkScreenshotsToTestLog = blnLinkScreenshotsToTestLog;
	}

	/**
	 * Function to get a Boolean value indicating whether the individual test
	 * logs must be linked to the result summary
	 * 
	 * @return Boolean value indicating whether the individual test logs must be
	 *         linked to the result summary
	 */
	public boolean shouldLinkTestLogsToSummary() {
		return blnLinkTestLogsToSummary;
	}

	/**
	 * Function to set a Boolean value indicating whether the individual test
	 * logs must be linked to the result summary
	 * 
	 * @param linkTestLogsToSummary
	 *            Boolean value indicating whether the individual test logs must
	 *            be linked to the result summary
	 */
	public void setLinkTestLogsToSummary(boolean blnLinkTestLogsToSummary) {
		this.blnLinkTestLogsToSummary = blnLinkTestLogsToSummary;
	}

	/**
	 * Function to get a Boolean value indicating whether all the screenshots
	 * must be consolidated into a Word document
	 * 
	 * @return Boolean value indicating whether all the screenshots must be
	 *         consolidated into a Word document
	 */
	public boolean shouldConsolidateScreenshotsInWordDoc() {
		return blnConsolidateScreenshotsInWordDoc;
	}

	/**
	 * Function to set a Boolean value indicating whether all the screenshots
	 * must be consolidated into a Word document
	 * 
	 * @param consolidateScreenshotsInWordDoc
	 *            Boolean value indicating whether all the screenshots must be
	 *            consolidated into a Word document
	 */
	public void setConsolidateScreenshotsInWordDoc(
			boolean blnConsolidateScreenshotsInWordDoc) {
		this.blnConsolidateScreenshotsInWordDoc = blnConsolidateScreenshotsInWordDoc;
	}

	/**
	 * Function to get a Boolean value indicating whether all the screenshots
	 * must be consolidated into a PDF document
	 * 
	 * @return Boolean value indicating whether all the screenshots must be
	 *         consolidated into a PDF document
	 */
	public boolean shouldConsolidateScreenshotsInPDF() {
		return blnConsolidateScreenshotsInPDF;
	}

	/**
	 * Function to set a Boolean value indicating whether all the screenshots
	 * must be consolidated into a PDF document
	 * 
	 * @param consolidateScreenshotsInPDF
	 *            Boolean value indicating whether all the screenshots must be
	 *            consolidated into a PDF document
	 */
	public void setConsolidateScreenshotsInPDF(
			boolean blnConsolidateScreenshotsInPDF) {
		this.blnConsolidateScreenshotsInPDF = blnConsolidateScreenshotsInPDF;
	}

	public void setisMobileExecution(boolean blnIsMobileExecution) {
		this.blnIsMobileExecution = blnIsMobileExecution;
	}

	public String getWidth() {
		String strWidth = "";
		if (this.blnIsMobileExecution) {
			strWidth = "250px";
		} else {
			strWidth = "400px";
		}
		return strWidth;
	}

	public String getHeight() {
		String strHeight = "";
		if (this.blnIsMobileExecution) {
			strHeight = "450px";
		} else {
			strHeight = "300px";
		}
		return strHeight;
	}

}