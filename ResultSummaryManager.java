package com.ford.fc.glo.autotest.integration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.ford.fc.glo.autotest.integration.ReportThemeFactory.Theme;

/**
 * Singleton class that manages the result summary creation during a batch
 * execution
 */
public class ResultSummaryManager {
	private SeleniumReport summaryReport;

	private ReportSettings reportSettings;
	private String strReportPath;

	private Date overallStartTime, overallEndTime;

	private Properties properties;
	private FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	private static final ResultSummaryManager RESULT_SUMMARY_MANAGER = new ResultSummaryManager();
	private SeleniumTestParameters testParameters;

	private ResultSummaryManager() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the singleton instance of the
	 * {@link ResultSummaryManager} object
	 * 
	 * @return Instance of the {@link ResultSummaryManager} object
	 */
	public static ResultSummaryManager getInstance() {
		return RESULT_SUMMARY_MANAGER;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Function to set the absolute path of the framework (to be used as a
	 * relative path)
	 */
	public void setRelativePath() {
		String strRelativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		if (strRelativePath.contains("supportlibraries")) {
			strRelativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(strRelativePath);
	}

	/**
	 * Function to initialize the test batch execution
	 * 
	 * @param runConfiguration
	 *            The run configuration to be executed
	 */
	public void initializeTestBatch(String strRunConfiguration) {
		overallStartTime = Util.getCurrentTime();

		properties = Settings.getInstance();

		frameworkParameters.setRunConfiguration(strRunConfiguration);
	}

	/**
	 * Function to initialize the summary report
	 * 
	 * @param nThreads
	 *            The number of parallel threads configured for the test batch
	 *            execution
	 */
	public void initializeSummaryReport(int intNThreads) {
		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory
				.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));

		summaryReport = new SeleniumReport(reportSettings, reportTheme, testParameters);

		summaryReport.initialize();
		summaryReport.initializeResultSummary();

		createResultSummaryHeader(intNThreads);
	}

	private void initializeReportSettings() {
		if (System.getProperty("ReportPath") != null) {
			strReportPath = System.getProperty("ReportPath");
		} else {
			strReportPath = TimeStamp.getInstance();
		}

		reportSettings = new ReportSettings(strReportPath, "");

		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.setGenerateExcelReports(Boolean.parseBoolean(properties.getProperty("ExcelReport")));
		reportSettings.setGenerateHtmlReports(Boolean.parseBoolean(properties.getProperty("HtmlReport")));
		reportSettings.setLinkTestLogsToSummary(true);
	}

	private void createResultSummaryHeader(int intNThreads) {
		summaryReport
				.addResultSummaryHeading(reportSettings.getProjectName() + " - Automation Execution Results Summary");
		summaryReport.addResultSummarySubHeading("Date & Time",
				": " + Util.getFormattedTime(overallStartTime, properties.getProperty("DateFormatString")), "OnError",
				": " + properties.getProperty("OnError"));
		summaryReport.addResultSummarySubHeading("Run Configuration", ": " + frameworkParameters.getRunConfiguration(),
				"No. of threads", ": " + intNThreads);

		summaryReport.addResultSummaryTableHeadings();
	}

	/**
	 * Function to set up the error log file within the test report
	 */
	public void setupErrorLog() {
		String strErrorLogFile = strReportPath + Util.getFileSeparator() + "ErrorLog.txt";
		try {
			System.setErr(new PrintStream(new FileOutputStream(strErrorLogFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while setting up the Error log!");
		}
	}

	/**
	 * Function to update the results summary with the status of the test
	 * instance which was executed
	 * 
	 * @param testParameters
	 *            The {@link SeleniumTestParameters} object containing the
	 *            details of the test instance which was executed
	 * @param testReportName
	 *            The name of the test report file corresponding to the test
	 *            instance
	 * @param executionTime
	 *            The time taken to execute the test instance
	 * @param testStatus
	 *            The Pass/Fail status of the test instance
	 */
	public void updateResultSummary(SeleniumTestParameters testParameters, String strTestReportName, String strExecutionTime,
			String strTestStatus) {
		summaryReport.updateResultSummary(testParameters, strTestReportName, strExecutionTime, strTestStatus);
	}

	/**
	 * Function to do the required wrap-up activities after completing the test
	 * batch execution
	 * 
	 * @param testExecutedInUnitTestFramework
	 *            Boolean variable indicating whether the test is executed in
	 *            JUnit/TestNG
	 */
	public void wrapUp(Boolean blnTestExecutedInUnitTestFramework) {
		overallEndTime = Util.getCurrentTime();
		String strTotalExecutionTime = Util.getTimeDifference(overallStartTime, overallEndTime);
		summaryReport.addResultSummaryFooter(strTotalExecutionTime);

		if (blnTestExecutedInUnitTestFramework && System.getProperty("ReportPath") == null) {
			File testNgResultSrc = new File(frameworkParameters.getRelativePath() + Util.getFileSeparator()
					+ properties.getProperty("TestNgReportPath") + Util.getFileSeparator()
					+ frameworkParameters.getRunConfiguration());
			File testNgResultCssFile = new File(frameworkParameters.getRelativePath() + Util.getFileSeparator()
					+ properties.getProperty("TestNgReportPath") + Util.getFileSeparator() + "testng.css");
			File testNgResultDest = summaryReport.createResultsSubFolder("TestNG Results");

			try {
				FileUtils.copyDirectoryToDirectory(testNgResultSrc, testNgResultDest);
				FileUtils.copyFileToDirectory(testNgResultCssFile, testNgResultDest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Function to launch the summary report at the end of the test batch
	 * execution
	 */
	public void launchResultSummary() {
		if (reportSettings.shouldGenerateHtmlReports()) {
			try {
				/**
				 * below logic is used for sending mails
				 */
				// sendReport(reportPath + "\\HTML Results\\Summary.Html");
				if (checkExceptionInErrorLogTxt()) {
					File f = new File(strReportPath + "\\ErrorLog.txt");
					java.awt.Desktop.getDesktop().edit(f);
				} else {
					File htmlFile = new File(strReportPath + "\\HTML Results\\Summary.Html");
					java.awt.Desktop.getDesktop().browse(htmlFile.toURI());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (reportSettings.shouldGenerateExcelReports()) {
			try {
				/**
				 * below logic is used for sending mails
				 */
				// sendReport(reportPath + "\\Excel Results\\Summary.xls");
				File excelFile = new File(strReportPath + "\\Excel Results\\Summary.xls");
				java.awt.Desktop.getDesktop().open(excelFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	private boolean checkExceptionInErrorLogTxt() throws IOException {
		boolean blnIsException = false;
		File file = new File(strReportPath + "\\ErrorLog.txt");

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String strLine = scanner.nextLine();
				if (strLine.contains("Exception")) {
					blnIsException = true;
					break;
				} else {
					blnIsException = false;
				}
			}
		} catch (FileNotFoundException e) {
		}
		return blnIsException;

	}

}