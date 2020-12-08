package com.ford.fc.glo.autotest.integration;

/**
 * Class to encapsulate the Excel report generation functions of the framework
 */
class ExcelReport implements ReportType {
	private static final String strCOVER_PAGE = "Cover_Page";
	private static final String strTEST_LOG = "Test_Log";
	private static final String strRESULT_SUMMARY = "Result_Summary";

	private static final String strTEST_SCENARIO = "Test_Scenario";
	private static final String strTEST_CASE = "Test_Case";
	private static final String strTEST_INSTANCE = "Test_Instance";
	private static final String TEST_DESCRIPTION = "Test_Description";
	private static final String strADDITIONAL_DETAILS = "Additional_Details";
	private static final String strEXECUTION_TIME = "Execution_Time";
	private static final String strTEST_STATUS = "Test_Status";

	private ExcelDataAccess testLogAccess, resultSummaryAccess;

	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	private ExcelCellFormatting cellFormatting = new ExcelCellFormatting();

	private int intCurrentSectionRowNum = 0;
	private int intCurrentSubSectionRowNum = 0;

	/**
	 * Constructor to initialize the Excel report path and name
	 * 
	 * @param reportSettings
	 *            The {@link ReportSettings} object
	 * @param reportTheme
	 *            The {@link ReportTheme} object
	 */
	public ExcelReport(ReportSettings reportSettings, ReportTheme reportTheme) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;

		testLogAccess = new ExcelDataAccess(reportSettings.getReportPath() + Util.getFileSeparator() + "Excel Results",
				reportSettings.getReportName());

		resultSummaryAccess = new ExcelDataAccess(
				reportSettings.getReportPath() + Util.getFileSeparator() + "Excel Results", "Summary");
	}

	/* TEST LOG FUNCTIONS */

	@Override
	public void initializeTestLog() {
		testLogAccess.createWorkbook();
		testLogAccess.addSheet(strCOVER_PAGE);
		testLogAccess.addSheet(strTEST_LOG);

		initializeTestLogColorPalette();

		testLogAccess.setRowSumsBelow(false);
	}

	private void initializeTestLogColorPalette() {
		testLogAccess.setCustomPaletteColor((short) 0x8, reportTheme.getContentBackColor());
		testLogAccess.setCustomPaletteColor((short) 0x9, reportTheme.getContentForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xA, reportTheme.getsubHeadingBackColor());
		testLogAccess.setCustomPaletteColor((short) 0xB, reportTheme.getContentForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xC, reportTheme.getSectionBackColor());
		testLogAccess.setCustomPaletteColor((short) 0xD, reportTheme.getSectionForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xE, "#008000"); // Green
																		// (Pass)
		testLogAccess.setCustomPaletteColor((short) 0xF, "#FF0000"); // Red
																		// (Fail)
		testLogAccess.setCustomPaletteColor((short) 0x10, "#FF8000"); // Orange
																		// (Warning)
		testLogAccess.setCustomPaletteColor((short) 0x11, "#000000"); // Black
																		// (Done)
		testLogAccess.setCustomPaletteColor((short) 0x12, "#00FF80"); // Blue
																		// (Screenshot)
	}

	@Override
	public void addTestLogHeading(String strHeading) {
		testLogAccess.setDatasheetName(strCOVER_PAGE);
		int intRowNum = testLogAccess.getLastRowNum();
		if (intRowNum != 0) {
			intRowNum = testLogAccess.addRow();
		}

		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short) 12);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);

		testLogAccess.setValue(intRowNum, 0, strHeading, cellFormatting);
		testLogAccess.mergeCells(intRowNum, intRowNum, 0, 4);
	}

	@Override
	public void addTestLogSubHeading(String strSubHeading1, String strSubHeading2, String strSubHeading3, String strSubHeading4) {
		testLogAccess.setDatasheetName(strCOVER_PAGE);
		int intRowNum = testLogAccess.addRow();

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);

		testLogAccess.setValue(intRowNum, 0, strSubHeading1, cellFormatting);
		testLogAccess.setValue(intRowNum, 1, strSubHeading2, cellFormatting);
		testLogAccess.setValue(intRowNum, 2, "", cellFormatting);
		testLogAccess.setValue(intRowNum, 3, strSubHeading3, cellFormatting);
		testLogAccess.setValue(intRowNum, 4, strSubHeading4, cellFormatting);
	}

	@Override
	public void addTestLogTableHeadings() {
		testLogAccess.setDatasheetName(strTEST_LOG);

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);

		testLogAccess.addColumn("Step_No", cellFormatting);
		testLogAccess.addColumn("Step_Name", cellFormatting);
		testLogAccess.addColumn("Description", cellFormatting);
		testLogAccess.addColumn("Status", cellFormatting);
		testLogAccess.addColumn("Step_Time", cellFormatting);
	}

	@Override
	public void addTestLogSection(String strSection) {
		testLogAccess.setDatasheetName(strTEST_LOG);
		int intRowNum = testLogAccess.addRow();

		if (intCurrentSubSectionRowNum != 0) {
			// Group (outline) previous sub-section rows
			testLogAccess.groupRows(intCurrentSubSectionRowNum, intRowNum - 1);
		}

		if (intCurrentSectionRowNum != 0) {
			// Group (outline) the previous section rows
			testLogAccess.groupRows(intCurrentSectionRowNum, intRowNum - 1);
		}

		intCurrentSectionRowNum = intRowNum + 1;
		intCurrentSubSectionRowNum = 0;

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = false;
		cellFormatting.setBackColorIndex((short) 0xA);
		cellFormatting.setForeColorIndex((short) 0xB);

		testLogAccess.setValue(intRowNum, 0, strSection, cellFormatting);
		testLogAccess.mergeCells(intRowNum, intRowNum, 0, 4);
	}

	@Override
	public void addTestLogSubSection(String strSubSection) {
		testLogAccess.setDatasheetName(strTEST_LOG);
		int intRowNum = testLogAccess.addRow();

		if (intCurrentSubSectionRowNum != 0) {
			// Group (outline) previous sub-section rows
			testLogAccess.groupRows(intCurrentSubSectionRowNum, intRowNum - 1);
		}

		intCurrentSubSectionRowNum = intRowNum + 1;

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);

		testLogAccess.setValue(intRowNum, 0, " " + strSubSection, cellFormatting);
		testLogAccess.mergeCells(intRowNum, intRowNum, 0, 4);
	}

	@Override
	public void updateTestLog(String strStepNumber, String strStepName, String strStepDescription, Status strStepStatus,
			String strScreenShotName) {
		testLogAccess.setDatasheetName(strTEST_LOG);
		int intRowNum = testLogAccess.addRow();

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBackColorIndex((short) 0xC);

		boolean blnStepContainsScreenshot = processStatusColumn(strStepStatus);

		cellFormatting.blnCentred = true;
		cellFormatting.blnBold = true;
		int intColumnNum = testLogAccess.getColumnNum("Status", 0);
		testLogAccess.setValue(intRowNum, intColumnNum, strStepStatus.toString(), cellFormatting);

		cellFormatting.setForeColorIndex((short) 0xD);
		cellFormatting.blnBold = false;
		testLogAccess.setValue(intRowNum, "Step_No", strStepNumber, cellFormatting);
		testLogAccess.setValue(intRowNum, "Step_Time", Util.getCurrentFormattedTime(reportSettings.getDateFormatString()),
				cellFormatting);

		cellFormatting.blnCentred = false;
		testLogAccess.setValue(intRowNum, "Step_Name", strStepName, cellFormatting);

		if (blnStepContainsScreenshot) {
			if (reportSettings.shouldLinkScreenshotsToTestLog()) {
				testLogAccess.setHyperlink(intRowNum, intColumnNum, "..\\Screenshots\\" + strScreenShotName);

				testLogAccess.setValue(intRowNum, "Description", strStepDescription, cellFormatting);
			} else {
				testLogAccess.setValue(intRowNum, "Description",
						strStepDescription + " (Refer screenshot @ " + strScreenShotName + ")", cellFormatting);
			}
		} else {
			testLogAccess.setValue(intRowNum, "Description", strStepDescription, cellFormatting);
		}
	}

	private boolean processStatusColumn(Status stepStatus) {
		boolean blnStepContainsScreenshot = false;

		switch (stepStatus) {
		case PASS:
			cellFormatting.setForeColorIndex((short) 0xE);
			blnStepContainsScreenshot = reportSettings.shouldTakeScreenshotPassedStep();
			break;

		case FAIL:
			cellFormatting.setForeColorIndex((short) 0xF);
			blnStepContainsScreenshot = reportSettings.shouldTakeScreenshotFailedStep();
			break;

		case WARNING:
			cellFormatting.setForeColorIndex((short) 0x10);
			blnStepContainsScreenshot = reportSettings.shouldTakeScreenshotFailedStep();
			break;

		case DONE:
			cellFormatting.setForeColorIndex((short) 0x11);
			blnStepContainsScreenshot = false;
			break;

		case SCREENSHOT:
			cellFormatting.setForeColorIndex((short) 0x11);
			blnStepContainsScreenshot = true;
			break;

		case DEBUG:
			cellFormatting.setForeColorIndex((short) 0x12);
			blnStepContainsScreenshot = false;
			break;

		default:
			throw new FrameworkException("Invalid step status!");
		}

		return blnStepContainsScreenshot;
	}

	@Override
	public void addTestLogFooter(String strExecutionTime, int intNStepsPassed, int intNStepsFailed) {
		testLogAccess.setDatasheetName(strTEST_LOG);
		int intRowNum = testLogAccess.addRow();

		if (intCurrentSubSectionRowNum != 0) {
			// Group (outline) the previous sub-section rows
			testLogAccess.groupRows(intCurrentSubSectionRowNum, intRowNum - 1);
		}

		if (intCurrentSectionRowNum != 0) {
			// Group (outline) the previous section rows
			testLogAccess.groupRows(intCurrentSectionRowNum, intRowNum - 1);
		}

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);

		testLogAccess.setValue(intRowNum, 0, "Execution Duration: " + strExecutionTime, cellFormatting);
		testLogAccess.mergeCells(intRowNum, intRowNum, 0, 4);

		intRowNum = testLogAccess.addRow();
		cellFormatting.blnCentred = false;
		cellFormatting.setBackColorIndex((short) 0x9);

		cellFormatting.setForeColorIndex((short) 0xE);
		testLogAccess.setValue(intRowNum, "Step_No", "Steps passed", cellFormatting);
		testLogAccess.setValue(intRowNum, "Step_Name", ": " + intNStepsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short) 0x8);
		testLogAccess.setValue(intRowNum, "Description", "", cellFormatting);
		cellFormatting.setForeColorIndex((short) 0xF);
		testLogAccess.setValue(intRowNum, "Status", "Steps failed", cellFormatting);
		testLogAccess.setValue(intRowNum, "Step_Time", ": " + intNStepsFailed, cellFormatting);

		wrapUpTestLog();
	}

	private void wrapUpTestLog() {
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);

		testLogAccess.setDatasheetName(strCOVER_PAGE);
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);
	}

	/* RESULT SUMMARY FUNCTIONS */

	@Override
	public void initializeResultSummary() {
		resultSummaryAccess.createWorkbook();
		resultSummaryAccess.addSheet(strCOVER_PAGE);
		resultSummaryAccess.addSheet(strRESULT_SUMMARY);

		initializeResultSummaryColorPalette();
	}

	private void initializeResultSummaryColorPalette() {
		resultSummaryAccess.setCustomPaletteColor((short) 0x8, reportTheme.getHeadingBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0x9, reportTheme.getHeadingForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xA, reportTheme.getSectionBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xB, reportTheme.getSectionForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xC, reportTheme.getContentBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xD, reportTheme.getContentForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xE, "#008000"); // Green
																			// (Pass)
		resultSummaryAccess.setCustomPaletteColor((short) 0xF, "#FF0000"); // Red
																			// (Fail)
	}

	@Override
	public void addResultSummaryHeading(String strHeading) {
		resultSummaryAccess.setDatasheetName(strCOVER_PAGE);
		int intRowNum = resultSummaryAccess.getLastRowNum();
		if (intRowNum != 0) {
			intRowNum = resultSummaryAccess.addRow();
		}

		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short) 12);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);

		resultSummaryAccess.setValue(intRowNum, 0, strHeading, cellFormatting);
		resultSummaryAccess.mergeCells(intRowNum, intRowNum, 0, 4);
	}

	@Override
	public void addResultSummarySubHeading(String strSubHeading1, String strSubHeading2, String strSubHeading3,
			String strSubHeading4) {
		resultSummaryAccess.setDatasheetName(strCOVER_PAGE);
		int intRowNum = resultSummaryAccess.addRow();

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);

		resultSummaryAccess.setValue(intRowNum, 0, strSubHeading1, cellFormatting);
		resultSummaryAccess.setValue(intRowNum, 1, strSubHeading2, cellFormatting);
		resultSummaryAccess.setValue(intRowNum, 2, "", cellFormatting);
		resultSummaryAccess.setValue(intRowNum, 3, strSubHeading3, cellFormatting);
		resultSummaryAccess.setValue(intRowNum, 4, strSubHeading4, cellFormatting);
	}

	@Override
	public void addResultSummaryTableHeadings() {
		resultSummaryAccess.setDatasheetName(strRESULT_SUMMARY);

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);

		resultSummaryAccess.addColumn(strTEST_SCENARIO, cellFormatting);
		resultSummaryAccess.addColumn(strTEST_CASE, cellFormatting);
		resultSummaryAccess.addColumn(strTEST_INSTANCE, cellFormatting);
		resultSummaryAccess.addColumn(TEST_DESCRIPTION, cellFormatting);
		resultSummaryAccess.addColumn(strADDITIONAL_DETAILS, cellFormatting);
		resultSummaryAccess.addColumn(strEXECUTION_TIME, cellFormatting);
		resultSummaryAccess.addColumn(strTEST_STATUS, cellFormatting);
	}

	@Override
	public void updateResultSummary(TestParameters testParameters, String strTestReportName, String strExecutionTime,
			String strTestStatus) {
		String strScenarioName = testParameters.getCurrentScenario();
		String strTestcaseName = testParameters.getCurrentTestcase();
		String strTestInstanceName = testParameters.getCurrentTestInstance();
		String strTestcaseDescription = testParameters.getCurrentTestDescription();
		String strAdditionalDetails = testParameters.getAdditionalDetails();

		resultSummaryAccess.setDatasheetName(strRESULT_SUMMARY);
		int intRowNum = resultSummaryAccess.addRow();

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBackColorIndex((short) 0xC);
		cellFormatting.setForeColorIndex((short) 0xD);

		cellFormatting.blnCentred = false;
		cellFormatting.blnBold = false;
		resultSummaryAccess.setValue(intRowNum, strTEST_SCENARIO, strScenarioName, cellFormatting);
		resultSummaryAccess.setValue(intRowNum, strTEST_CASE, strTestcaseName, cellFormatting);

		int intColumnNum = resultSummaryAccess.getColumnNum(strTEST_INSTANCE, 0);
		resultSummaryAccess.setValue(intRowNum, intColumnNum, strTestInstanceName, cellFormatting);
		if (reportSettings.shouldLinkTestLogsToSummary()) {
			resultSummaryAccess.setHyperlink(intRowNum, intColumnNum, strTestReportName + ".xls");
		}

		resultSummaryAccess.setValue(intRowNum, TEST_DESCRIPTION, strTestcaseDescription, cellFormatting);
		resultSummaryAccess.setValue(intRowNum, strADDITIONAL_DETAILS, strAdditionalDetails, cellFormatting);

		cellFormatting.blnCentred = true;
		resultSummaryAccess.setValue(intRowNum, strEXECUTION_TIME, strExecutionTime, cellFormatting);

		cellFormatting.blnBold = true;
		if ("Passed".equalsIgnoreCase(strTestStatus)) {
			cellFormatting.setForeColorIndex((short) 0xE);
		}
		if ("Failed".equalsIgnoreCase(strTestStatus)) {
			cellFormatting.setForeColorIndex((short) 0xF);
		}
		resultSummaryAccess.setValue(intRowNum, strTEST_STATUS, strTestStatus, cellFormatting);
	}

	@Override
	public void addResultSummaryFooter(String strTotalExecutionTime, int intNTestsPassed, int intNTestsFailed) {
		resultSummaryAccess.setDatasheetName(strRESULT_SUMMARY);
		int intRowNum = resultSummaryAccess.addRow();

		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.blnBold = true;
		cellFormatting.blnCentred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);

		resultSummaryAccess.setValue(intRowNum, 0, "Total Duration: " + strTotalExecutionTime, cellFormatting);
		resultSummaryAccess.mergeCells(intRowNum, intRowNum, 0, 6);

		intRowNum = resultSummaryAccess.addRow();
		cellFormatting.blnCentred = false;
		cellFormatting.setBackColorIndex((short) 0x9);

		cellFormatting.setForeColorIndex((short) 0xE);
		resultSummaryAccess.setValue(intRowNum, strTEST_SCENARIO, "Tests passed", cellFormatting);
		resultSummaryAccess.setValue(intRowNum, strTEST_CASE, ": " + intNTestsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short) 0x8);
		resultSummaryAccess.setValue(intRowNum, strTEST_INSTANCE, "", cellFormatting);
		resultSummaryAccess.setValue(intRowNum, TEST_DESCRIPTION, "", cellFormatting);
		resultSummaryAccess.setValue(intRowNum, strADDITIONAL_DETAILS, "", cellFormatting);
		cellFormatting.setForeColorIndex((short) 0xF);
		resultSummaryAccess.setValue(intRowNum, strEXECUTION_TIME, "Tests failed", cellFormatting);
		resultSummaryAccess.setValue(intRowNum, strTEST_STATUS, ": " + intNTestsFailed, cellFormatting);

		wrapUpResultSummary();
	}

	private void wrapUpResultSummary() {
		resultSummaryAccess.autoFitContents(0, 6);
		resultSummaryAccess.addOuterBorder(0, 6);

		resultSummaryAccess.setDatasheetName(strCOVER_PAGE);
		resultSummaryAccess.autoFitContents(0, 4);
		resultSummaryAccess.addOuterBorder(0, 4);
	}

}