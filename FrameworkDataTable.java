package com.ford.fc.glo.autotest.integration;

import java.util.Properties;

/**
 * Class to encapsulate the datatable related functions of the framework
 */
public class FrameworkDataTable {
	private final String strDatatablePath, strDatatableName;
	private String strDataReferenceIdentifier = "#";

	private String strCurrentTestcase;
	private int intCurrentIteration = 0, intCurrentSubIteration = 0;
	private static Properties properties;

	/**
	 * Constructor to initialize the {@link FrameworkDataTable} object
	 * 
	 * @param datatablePath
	 *            The path where the datatable is stored
	 * @param datatableName
	 *            The name of the datatable file
	 */
	public FrameworkDataTable(String strDatatablePath, String strDatatableName) {
		this.strDatatablePath = strDatatablePath;
		this.strDatatableName = strDatatableName;
		properties = Settings.getInstance();
	}

	/**
	 * Function to set the data reference identifier character
	 * 
	 * @param dataReferenceIdentifier
	 *            The data reference identifier character
	 */
	public void setDataReferenceIdentifier(String strDataReferenceIdentifier) {
		if (strDataReferenceIdentifier.length() != 1) {
			throw new FrameworkException("The data reference identifier must be a single character!");
		}

		this.strDataReferenceIdentifier = strDataReferenceIdentifier;
	}

	/**
	 * Function to set the variables required to uniquely identify the exact row
	 * of data under consideration
	 * 
	 * @param currentTestcase
	 *            The ID of the current test case
	 * @param currentIteration
	 *            The Iteration being executed currently
	 * @param currentSubIteration
	 *            The Sub-Iteration being executed currently
	 */
	public void setCurrentRow(String strCurrentTestcase, int intCurrentIteration, int intCurrentSubIteration) {
		this.strCurrentTestcase = strCurrentTestcase;
		this.intCurrentIteration = intCurrentIteration;
		this.intCurrentSubIteration = intCurrentSubIteration;
	}

	private void checkPreRequisites() {
		if (strCurrentTestcase == null) {
			throw new FrameworkException("FrameworkDataTable.currentTestCase is not set!");
		}
		if (intCurrentIteration == 0) {
			throw new FrameworkException("FrameworkDataTable.currentIteration is not set!");
		}
		if (properties.getProperty("Approach").equalsIgnoreCase("KeywordDriven")) {
			if (intCurrentSubIteration == 0) {
				throw new FrameworkException("FrameworkDataTable.currentSubIteration is not set!");
			}
		}
	}

	/**
	 * Function to return the test data value corresponding to the sheet name
	 * and field name passed
	 * 
	 * @param datasheetName
	 *            The name of the sheet in which the data is present
	 * @param fieldName
	 *            The name of the field whose value is required
	 * @return The test data present in the field name specified
	 * @see #putData(String, String, String)
	 * @see #getExpectedResult(String)
	 */
	public String getData(String strDatasheetName, String strFieldName) {
		checkPreRequisites();

		ExcelDataAccess testDataAccess = new ExcelDataAccess(strDatatablePath, strDatatableName);
		testDataAccess.setDatasheetName(strDatasheetName);

		int intRowNum = testDataAccess.getRowNum(strCurrentTestcase, 0, 1); // Start
																		// at
																		// row
																		// 1,
																		// skipping
																		// the
																		// header
																		// row
		if (intRowNum == -1) {
			throw new FrameworkException("The test case \"" + strCurrentTestcase + "\""
					+ "is not found in the test data sheet \"" + strDatasheetName + "\"!");
		}
		intRowNum = testDataAccess.getRowNum(Integer.toString(intCurrentIteration), 1, intRowNum);
		if (intRowNum == -1) {
			throw new FrameworkException("The iteration number \"" + intCurrentIteration + "\"" + "of the test case \""
					+ strCurrentTestcase + "\"" + "is not found in the test data sheet \"" + strDatasheetName + "\"!");
		}
		if (properties.getProperty("Approach").equalsIgnoreCase("KeywordDriven")) {
			intRowNum = testDataAccess.getRowNum(Integer.toString(intCurrentSubIteration), 2, intRowNum);
			if (intRowNum == -1) {
				throw new FrameworkException("The sub iteration number \"" + intCurrentSubIteration + "\""
						+ "under iteration number \"" + intCurrentIteration + "\"" + "of the test case \""
						+ strCurrentTestcase + "\"" + "is not found in the test data sheet \"" + strDatasheetName + "\"!");
			}
		}

		String strDataValue = testDataAccess.getValue(intRowNum, strFieldName);

		if (strDataValue.startsWith(strDataReferenceIdentifier)) {
			strDataValue = getCommonData(strFieldName, strDataValue);
		}

		return strDataValue;
	}

	private String getCommonData(String strFieldName, String strDataValue) {
		ExcelDataAccess commonDataAccess = new ExcelDataAccess(strDatatablePath, "Common Testdata");
		commonDataAccess.setDatasheetName("Common_Testdata");

		String strDataReferenceId = strDataValue.split(strDataReferenceIdentifier)[1];

		int intRowNum = commonDataAccess.getRowNum(strDataReferenceId, 0, 1); // Start
																		// at
																		// row
																		// 1,
																		// skipping
																		// the
																		// header
																		// row
		if (intRowNum == -1) {
			throw new FrameworkException("The common test data row identified by \"" + strDataReferenceId + "\""
					+ "is not found in the common test data sheet!");
		}

		return commonDataAccess.getValue(intRowNum, strFieldName);
	}

	/**
	 * Function to output intermediate data (output values) into the specified
	 * sheet
	 * 
	 * @param datasheetName
	 *            The name of the sheet into which the data is to be written
	 * @param fieldName
	 *            The name of the field into which the data is to be written
	 * @param dataValue
	 *            The value to be written into the field specified
	 * @see #getData(String, String)
	 */
	public void putData(String strDatasheetName, String strFieldName, String strDataValue) {
		checkPreRequisites();

		ExcelDataAccess testDataAccess = new ExcelDataAccess(strDatatablePath, strDatatableName);
		testDataAccess.setDatasheetName(strDatasheetName);

		int intRowNum = testDataAccess.getRowNum(strCurrentTestcase, 0, 1); // Start
																		// at
																		// row
																		// 1,
																		// skipping
																		// the
																		// header
																		// row
		if (intRowNum == -1) {
			throw new FrameworkException("The test case \"" + strCurrentTestcase + "\""
					+ "is not found in the test data sheet \"" + strDatasheetName + "\"!");
		}
		intRowNum = testDataAccess.getRowNum(Integer.toString(intCurrentIteration), 1, intRowNum);
		if (intRowNum == -1) {
			throw new FrameworkException("The iteration number \"" + intCurrentIteration + "\"" + "of the test case \""
					+ strCurrentTestcase + "\"" + "is not found in the test data sheet \"" + strDatasheetName + "\"!");
		}
		if (properties.getProperty("Approach").equalsIgnoreCase("KeywordDriven")) {
			intRowNum = testDataAccess.getRowNum(Integer.toString(intCurrentSubIteration), 2, intRowNum);
			if (intRowNum == -1) {
				throw new FrameworkException("The sub iteration number \"" + intCurrentSubIteration + "\""
						+ "under iteration number \"" + intCurrentIteration + "\"" + "of the test case \""
						+ strCurrentTestcase + "\"" + "is not found in the test data sheet \"" + strDatasheetName + "\"!");
			}
		}

		synchronized (FrameworkDataTable.class) {
			testDataAccess.setValue(intRowNum, strFieldName, strDataValue);
		}
	}

	/**
	 * Function to get the expected result corresponding to the field name
	 * passed
	 * 
	 * @param fieldName
	 *            The name of the field which contains the expected results
	 * @return The expected result present in the field name specified
	 * @see #getData(String, String)
	 */
	public String getExpectedResult(String strFieldName) {
		checkPreRequisites();

		ExcelDataAccess expectedResultsAccess = new ExcelDataAccess(strDatatablePath, strDatatableName);
		expectedResultsAccess.setDatasheetName("Parametrized_Checkpoints");

		int intRowNum = expectedResultsAccess.getRowNum(strCurrentTestcase, 0, 1); // Start
																				// at
																				// row
																				// 1,
																				// skipping
																				// the
																				// header
																				// row
		if (intRowNum == -1) {
			throw new FrameworkException("The test case \"" + strCurrentTestcase + "\""
					+ "is not found in the parametrized checkpoints sheet!");
		}
		intRowNum = expectedResultsAccess.getRowNum(Integer.toString(intCurrentIteration), 1, intRowNum);
		if (intRowNum == -1) {
			throw new FrameworkException("The iteration number \"" + intCurrentIteration + "\"" + "of the test case \""
					+ strCurrentTestcase + "\"" + "is not found in the parametrized checkpoints sheet!");
		}
		if (properties.getProperty("Approach").equalsIgnoreCase("KeywordDriven")) {
			intRowNum = expectedResultsAccess.getRowNum(Integer.toString(intCurrentSubIteration), 2, intRowNum);
			if (intRowNum == -1) {
				throw new FrameworkException("The sub iteration number \"" + intCurrentSubIteration + "\""
						+ "under iteration number \"" + intCurrentIteration + "\"" + "of the test case \""
						+ strCurrentTestcase + "\"" + "is not found in the parametrized checkpoints sheet!");
			}
		}

		return expectedResultsAccess.getValue(intRowNum, strFieldName);
	}
}