package com.ford.fc.glo.autotest.integration;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
//import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


/**
 * Class to encapsulate all the reporting features of the framework
 */
public class Report {
	private static final String strEXCEL_RESULTS = "Excel Results";
//	private static final String strHTML_RESULTS = "HTML Results";
	private static final String strSCREENSHOTS = "Screenshots";

	private ReportSettings reportSettings;
	private ReportTheme reportTheme;

	private int intStepNumber;
	private int intNStepsPassed, intNStepsFailed;
	private int intNTestsPassed, intNTestsFailed;

	private List<ReportType> reportTypes = new ArrayList<ReportType>();

	private String strTestStatus;
	private String strFailureDescription;

	/**
	 * Constructor to initialize the Report
	 * 
	 * @param reportSettings
	 *            The {@link ReportSettings} object
	 * @param reportTheme
	 *            The {@link ReportTheme} object
	 */
	@SuppressWarnings("unused")
	private final SeleniumTestParameters testParameters;

	public Report(ReportSettings reportSettings, ReportTheme reportTheme, SeleniumTestParameters testParameters) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
		this.testParameters = testParameters;

		intNStepsPassed = 0;
		intNStepsFailed = 0;
		strTestStatus = "Passed";
	}

	/**
	 * Function to get the current status of the test being executed
	 * 
	 * @return the current status of the test being executed
	 */
	public String getTestStatus() {
		return strTestStatus;
	}

	/**
	 * Function to get the description of any failure that may occur during the
	 * script execution
	 * 
	 * @return The failure description (relevant only if the test fails)
	 */
	public String getFailureDescription() {
		return strFailureDescription;
	}

	public void initialize() {

		if (reportSettings.shouldGenerateExcelReports()) {
			new File(reportSettings.getReportPath() + Util.getFileSeparator() + strEXCEL_RESULTS).mkdir();

			ExcelReport excelReport = new ExcelReport(reportSettings, reportTheme);
			reportTypes.add(excelReport);
		}

		new File(reportSettings.getReportPath() + Util.getFileSeparator() + strSCREENSHOTS).mkdir();
	}

	/**
	 * Function to create a sub-folder within the Results folder
	 * 
	 * @param subFolderName
	 *            The name of the sub-folder to be created
	 * @return The {@link File} object representing the newly created sub-folder
	 */
	public File createResultsSubFolder(String strSubFolderName) {
		File resultsSubFolder = new File(reportSettings.getReportPath() + Util.getFileSeparator() + strSubFolderName);
		resultsSubFolder.mkdir();
		return resultsSubFolder;
	}

	/* TEST LOG FUNCTIONS */

	/**
	 * Function to initialize the test log
	 */
	public void initializeTestLog() {
		if ("".equals(reportSettings.getReportName())) {
			throw new FrameworkException("The report name cannot be empty!");
		}

		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).initializeTestLog();
		}
	}

	/**
	 * Function to add a heading to the test log
	 * 
	 * @param heading
	 *            The heading to be added
	 */
	public void addTestLogHeading(String strHeading) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogHeading(strHeading);
		}
	}

	/**
	 * Function to add sub-headings to the test log (4 sub-headings present per
	 * test log row)
	 * 
	 * @param subHeading1
	 *            The first sub-heading to be added
	 * @param subHeading2
	 *            The second sub-heading to be added
	 * @param subHeading3
	 *            The third sub-heading to be added
	 * @param subHeading4
	 *            The fourth sub-heading to be added
	 */
	public void addTestLogSubHeading(String strSubHeading1, String strSubHeading2, String strSubHeading3, String strSubHeading4) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSubHeading(strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4);
		}
	}

	/**
	 * Function to add the overall table headings to the test log (should be
	 * called first before adding the actual content into the test log; headings
	 * and sub-heading should be added before this)
	 */
	public void addTestLogTableHeadings() {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogTableHeadings();
		}
	}

	/**
	 * Function to add a section to the test log
	 * 
	 * @param section
	 *            The section to be added
	 */
	public void addTestLogSection(String strSection) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSection(strSection);
		}

		intStepNumber = 1;
	}

	/**
	 * Function to add a sub-section to the test log (should be called only
	 * within a previously created section)
	 * 
	 * @param subSection
	 *            The sub-section to be added
	 */
	public void addTestLogSubSection(String strSubSection) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogSubSection(strSubSection);
		}
	}

	/**
	 * Function to update the test log with the details of a particular test
	 * step
	 * 
	 * @param stepName
	 *            The test step name
	 * @param stepDescription
	 *            The description of what the test step does
	 * @param stepStatus
	 *            The status of the test step
	 */
	public void updateTestLog(String strStepName, String strStepDescription, Status strStepStatus) {
		handleStepInvolvingPassOrFail(strStepDescription, strStepStatus);

		if (strStepStatus.ordinal() <= reportSettings.getLogLevel()) {
			String strScreenshotName = handleStepInvolvingScreenshot(strStepName, strStepStatus);

			for (int i = 0; i < reportTypes.size(); i++) {
				reportTypes.get(i).updateTestLog(Integer.toString(intStepNumber), strStepName, strStepDescription, strStepStatus,
						strScreenshotName);
				/***** To Integrate with JIRA *****/
				/*
				 * if(stepStatus==Status.FAIL)
				 * RESTclient.defectLog(testParameters.getCurrentTestcase(),
				 * stepDescription, reportSettings.getReportPath() +
				 * Util.getFileSeparator() + SCREENSHOTS +
				 * Util.getFileSeparator() + screenshotName);
				 */
			}

			intStepNumber++;
		}
	}

	private void handleStepInvolvingPassOrFail(String strStepDescription, Status strStepStatus) {
		if (strStepStatus.equals(Status.FAIL)) {
			strTestStatus = "Failed";

			if (strFailureDescription == null) {
				strFailureDescription = strStepDescription;
			} else {
				strFailureDescription = strFailureDescription + "; " + strStepDescription;
			}

			intNStepsFailed++;
		} else if (strStepStatus.equals(Status.PASS)) {
			intNStepsPassed++;
		}
	}

	private String handleStepInvolvingScreenshot(String strStepName, Status strStepStatus) {
		String screenshotName = reportSettings.getReportName() + "_"
				+ Util.getCurrentFormattedTime(reportSettings.getDateFormatString()).replace(" ", "_").replace(":", "-")
				+ "_" + strStepName.replace(" ", "_") + ".png";

		if ((strStepStatus.equals(Status.FAIL) && reportSettings.shouldTakeScreenshotFailedStep())
				|| (strStepStatus.equals(Status.PASS) && reportSettings.shouldTakeScreenshotPassedStep())
				|| strStepStatus.equals(Status.SCREENSHOT)) {

			String screenshotPath = reportSettings.getReportPath() + Util.getFileSeparator() + strSCREENSHOTS
					+ Util.getFileSeparator() + screenshotName;
			if (screenshotPath.length() > 256) { // Max char limit for Windows
													// filenames
				screenshotPath = screenshotPath.substring(0, 256);
			}

			takeScreenshot(screenshotPath);
		}

		return screenshotName;
	}

	/**
	 * Function to take a screenshot
	 * 
	 * @param screenshotPath
	 *            The path where the screenshot should be saved
	 */
	protected void takeScreenshot(String strScreenshotPath) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Rectangle rectangle = new Rectangle(0, 0, screenSize.width, screenSize.height);
		Robot robot;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while creating Robot object (for taking screenshot)");
		}

		BufferedImage screenshotImage = robot.createScreenCapture(rectangle);
		File screenshotFile = new File(strScreenshotPath);

		try {
			ImageIO.write(screenshotImage, "jpg", screenshotFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing screenshot to .jpg file");
		}
	}

	/**
	 * Function to add a footer to the test log (The footer format is
	 * pre-defined - it contains the execution time and the number of
	 * passed/failed steps)
	 * 
	 * @param executionTime
	 *            The time taken to execute the test case
	 */
	public void addTestLogFooter(String executionTime) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addTestLogFooter(executionTime, intNStepsPassed, intNStepsFailed);
		}
	}

	/* RESULT SUMMARY FUNCTIONS */

	/**
	 * Function to initialize the result summary
	 */
	public void initializeResultSummary() {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).initializeResultSummary();
		}
	}

	/**
	 * Function to add a heading to the result summary
	 * 
	 * @param heading
	 *            The heading to be added
	 */
	public void addResultSummaryHeading(String strHeading) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryHeading(strHeading);
		}
	}

	/**
	 * Function to add sub-headings to the result summary (4 sub-headings
	 * present per result summary row)
	 * 
	 * @param subHeading1
	 *            The first sub-heading to be added
	 * @param subHeading2
	 *            The second sub-heading to be added
	 * @param subHeading3
	 *            The third sub-heading to be added
	 * @param subHeading4
	 *            The fourth sub-heading to be added
	 */
	public void addResultSummarySubHeading(String strSubHeading1, String strSubHeading2, String strSubHeading3,
			String strSubHeading4) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummarySubHeading(strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4);
		}
	}

	/**
	 * Function to add the overall table headings to the result summary
	 */
	public void addResultSummaryTableHeadings() {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryTableHeadings();
		}
	}

	/**
	 * Function to update the results summary with the status of the test
	 * instance which was executed
	 * 
	 * @param testParameters
	 *            The {@link TestParameters} object containing the details of
	 *            the test instance which was executed
	 * @param testReportName
	 *            The name of the test report file corresponding to the test
	 *            instance
	 * @param executionTime
	 *            The time taken to execute the test instance
	 * @param testStatus
	 *            The Pass/Fail status of the test instance
	 */
	public synchronized void updateResultSummary(TestParameters testParameters, String strTestReportName,
			String strExecutionTime, String strTestStatus) {
		if ("failed".equalsIgnoreCase(strTestStatus)) {
			intNTestsFailed++;
		} else if ("passed".equalsIgnoreCase(strTestStatus)) {
			intNTestsPassed++;
		} else if ("aborted".equalsIgnoreCase(strTestStatus)) {
			reportSettings.setLinkTestLogsToSummary(false);
		}

		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).updateResultSummary(testParameters, strTestReportName, strExecutionTime, strTestStatus);
		}
	}

	/**
	 * Function to add a footer to the result summary (The footer format is
	 * pre-defined - it contains the total execution time and the number of
	 * passed/failed tests)
	 * 
	 * @param totalExecutionTime
	 *            The total time taken to execute all the test cases
	 */
	public void addResultSummaryFooter(String strTotalExecutionTime) {
		for (int i = 0; i < reportTypes.size(); i++) {
			reportTypes.get(i).addResultSummaryFooter(strTotalExecutionTime, intNTestsPassed, intNTestsFailed);
		}
	}
}