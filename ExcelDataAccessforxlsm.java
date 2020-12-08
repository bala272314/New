package com.ford.fc.glo.autotest.integration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataAccessforxlsm {

	private final String strFilePath, strFileName;

	private String strDatasheetName;

	/**
	 * Function to get the Excel sheet name
	 * 
	 * @return The Excel sheet name
	 */
	public String getDatasheetName() {
		return strDatasheetName;
	}

	/**
	 * Function to set the Excel sheet name
	 * 
	 * @param datasheetName
	 *            The Excel sheet name
	 */
	public void setDatasheetName(String strDatasheetName) {
		this.strDatasheetName = strDatasheetName;
	}

	/**
	 * Constructor to initialize the excel data filepath and filename
	 * 
	 * @param filePath
	 *            The absolute path where the excel data file is stored
	 * @param fileName
	 *            The name of the excel data file (without the extension). Note
	 *            that .xlsx files are not supported, only .xls files are
	 *            supported
	 */
	public ExcelDataAccessforxlsm(String strFilePath, String strFileName) {
		this.strFilePath = strFilePath;
		this.strFileName = strFileName;
	}

	private void checkPreRequisites() {
		if (strDatasheetName == null) {
			throw new FrameworkException("ExcelDataAccess.datasheetName is not set!");
		}
	}

	private XSSFWorkbook openFileForReading() {

		String strAbsoluteFilePath = strFilePath + Util.getFileSeparator() + strFileName + ".xlsx";

		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(strAbsoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified file \"" + strAbsoluteFilePath + "\" does not exist!");
		}

		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(fileInputStream);
			// fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while opening the specified Excel workbook \"" + strAbsoluteFilePath + "\"");
		}

		return workbook;
	}

	private XSSFSheet getWorkSheet(XSSFWorkbook workbook) {
		XSSFSheet worksheet = workbook.getSheet(strDatasheetName);
		if (worksheet == null) {
			throw new FrameworkException("The specified sheet \"" + strDatasheetName + "\""
					+ "does not exist within the workbook \"" + strFileName + ".xlsx\"");
		}

		return worksheet;
	}

	/**
	 * Function to get the last row number within the worksheet
	 * 
	 * @return The last row number within the worksheet
	 */
	public int getLastRowNum() {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);

		return worksheet.getLastRowNum();
	}

	/**
	 * Function to get the value in the cell identified by the specified row
	 * number and column header
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnHeader
	 *            The column header of the cell
	 * @return The value present in the cell
	 */
	public String getValue(int intRowNum, String strColumnHeader) {
		checkPreRequisites();

		XSSFWorkbook workbook = openFileForReading();
		XSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

		XSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int intColumnNum = -1;
		String strCurrentValue;

		for (int intCurrentColumnNum = 0; intCurrentColumnNum < row.getLastCellNum(); intCurrentColumnNum++) {

			XSSFCell cell = row.getCell(intCurrentColumnNum);
			strCurrentValue = getCellValueAsString(cell, formulaEvaluator);

			if (strCurrentValue.equals(strColumnHeader)) {
				intColumnNum = intCurrentColumnNum;
				break;
			}
		}

		if (intColumnNum == -1) {
			throw new FrameworkException("The specified column header \"" + strColumnHeader + "\""
					+ "is not found in the sheet \"" + strDatasheetName + "\"!");
		} else {
			row = worksheet.getRow(intRowNum);
			XSSFCell cell = row.getCell(intColumnNum);
			return getCellValueAsString(cell, formulaEvaluator);
		}
	}

	private String getCellValueAsString(XSSFCell cell, FormulaEvaluator formulaEvaluator) {
		if (cell == null || cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
			return "";
		} else {
			if (formulaEvaluator.evaluate(cell).getCellType() == XSSFCell.CELL_TYPE_ERROR) {
				throw new FrameworkException(
						"Error in formula within this cell! " + "Error code: " + cell.getErrorCellValue());
			}

			DataFormatter dataFormatter = new DataFormatter();
			return dataFormatter.formatCellValue(formulaEvaluator.evaluateInCell(cell));
		}
	}

}
