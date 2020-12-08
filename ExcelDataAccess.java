package com.ford.fc.glo.autotest.integration;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Hyperlink; // not available under hssf
import org.apache.poi.ss.usermodel.CreationHelper; // not available under hssf
import org.apache.poi.ss.util.CellRangeAddress;

//import org.apache.poi.hssf.util.CellRangeAddress;	not used because this is deprecated

/**
 * Class to encapsulate the excel data access layer of the framework
 */
public class ExcelDataAccess {
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
	public ExcelDataAccess(String strFilePath, String strFileName) {
		this.strFilePath = strFilePath;
		this.strFileName = strFileName;
	}

	private void checkPreRequisites() {
		if (strDatasheetName == null) {
			throw new FrameworkException(
					"ExcelDataAccess.datasheetName is not set!");
		}
	}

	private HSSFWorkbook openFileForReading() {

		String strAbsoluteFilePath = strFilePath + Util.getFileSeparator() + strFileName
				+ ".xls";

		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(strAbsoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified file \""
					+ strAbsoluteFilePath + "\" does not exist!");
		}

		HSSFWorkbook workbook;
		try {
			workbook = new HSSFWorkbook(fileInputStream);
			// fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while opening the specified Excel workbook \""
							+ strAbsoluteFilePath + "\"");
		}

		return workbook;
	}

	private void writeIntoFile(HSSFWorkbook workbook) {
		String strAbsoluteFilePath = strFilePath + Util.getFileSeparator() + strFileName
				+ ".xls";

		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(strAbsoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified file \""
					+ strAbsoluteFilePath + "\" does not exist!");
		}

		try {
			workbook.write(fileOutputStream);
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while writing into the specified Excel workbook \""
							+ strAbsoluteFilePath + "\"");
		}
	}

	private HSSFSheet getWorkSheet(HSSFWorkbook workbook) {
		HSSFSheet worksheet = workbook.getSheet(strDatasheetName);
		if (worksheet == null) {
			throw new FrameworkException("The specified sheet \""
					+ strDatasheetName + "\""
					+ "does not exist within the workbook \"" + strFileName
					+ ".xls\"");
		}

		return worksheet;
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding row number
	 * 
	 * @param key
	 *            The value being searched for
	 * @param columnNum
	 *            The column number in which the key should be searched
	 * @param startRowNum
	 *            The row number from which the search should start
	 * @return The row number in which the specified key is found (-1 if the key
	 *         is not found)
	 */
	public int getRowNum(String strKey, int intColumnNum, int intStartRowNum) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		String strCurrentValue;
		for (int intCurrentRowNum = intStartRowNum; intCurrentRowNum <= worksheet
				.getLastRowNum(); intCurrentRowNum++) {

			HSSFRow row = worksheet.getRow(intCurrentRowNum);
			HSSFCell cell = row.getCell(intColumnNum);
			strCurrentValue = getCellValueAsString(cell, formulaEvaluator);

			if (strCurrentValue.equals(strKey)) {
				return intCurrentRowNum;
			}
		}

		return -1;
	}

	/*
	 * private String getCellValueAsString(HSSFCell cell, FormulaEvaluator
	 * formulaEvaluator) { if (cell == null) { return ""; } else {
	 * switch(formulaEvaluator.evaluate(cell).getCellType()) { case
	 * HSSFCell.CELL_TYPE_BLANK: case HSSFCell.CELL_TYPE_STRING:
	 * System.out.print("string: "); return cell.getStringCellValue().trim();
	 * 
	 * case HSSFCell.CELL_TYPE_BOOLEAN: System.out.print("bool: "); return
	 * Boolean.toString(cell.getBooleanCellValue());
	 * 
	 * case HSSFCell.CELL_TYPE_NUMERIC: if
	 * (HSSFDateUtil.isCellDateFormatted(cell)) { System.out.print("date: ");
	 * return cell.getDateCellValue().toString(); } else {
	 * System.out.print("numeric: "); return
	 * Double.toString(cell.getNumericCellValue()); }
	 * 
	 * case HSSFCell.CELL_TYPE_ERROR: System.out.print("error: "); throw new
	 * FrameworkException("Error in formula within this cell! " + "Error code: "
	 * + cell.getErrorCellValue());
	 * 
	 * //case HSSFCell.CELL_TYPE_FORMULA: // This will never occur!
	 * 
	 * default: throw new FrameworkException("Unhandled cell type!"); } } }
	 */

	private String getCellValueAsString(HSSFCell cell,
			FormulaEvaluator formulaEvaluator) {
		if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			return "";
		} else {
			if (formulaEvaluator.evaluate(cell).getCellType() == HSSFCell.CELL_TYPE_ERROR) {
				throw new FrameworkException(
						"Error in formula within this cell! " + "Error code: "
								+ cell.getErrorCellValue());
			}

			DataFormatter dataFormatter = new DataFormatter();
			return dataFormatter.formatCellValue(formulaEvaluator
					.evaluateInCell(cell));
		}
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding row number
	 * 
	 * @param key
	 *            The value being searched for
	 * @param columnNum
	 *            The column number in which the key should be searched
	 * @return The row number in which the specified key is found (-1 if the key
	 *         is not found)
	 */
	public int getRowNum(String strKey, int intColumnNum) {
		return getRowNum(strKey, intColumnNum, 0);
	}

	/**
	 * Function to get the last row number within the worksheet
	 * 
	 * @return The last row number within the worksheet
	 */
	public int getLastRowNum() {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		return worksheet.getLastRowNum();
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding occurence count
	 * 
	 * @param key
	 *            The value being searched for
	 * @param columnNum
	 *            The column number in which the key should be searched
	 * @param startRowNum
	 *            The row number from which the search should start
	 * @return The occurence count of the specified key
	 */
	public int getRowCount(String strKey, int intColumnNum, int intStartRowNum) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		int intRowCount = 0;
		boolean blnKeyFound = false;

		String strCurrentValue;
		for (int intCurrentRowNum = intStartRowNum; intCurrentRowNum <= worksheet
				.getLastRowNum(); intCurrentRowNum++) {

			HSSFRow row = worksheet.getRow(intCurrentRowNum);
			HSSFCell cell = row.getCell(intColumnNum);
			strCurrentValue = getCellValueAsString(cell, formulaEvaluator);

			if (strCurrentValue.equals(strKey)) {
				intRowCount++;
				blnKeyFound = true;
			} else {
				if (blnKeyFound) {
					break; // Assumption: Keys always appear contiguously
				}
			}
		}

		return intRowCount;
	}

	/**
	 * Function to search for a specified key within a column, and return the
	 * corresponding occurence count
	 * 
	 * @param key
	 *            The value being searched for
	 * @param columnNum
	 *            The column number in which the key should be searched
	 * @return The occurence count of the specified key
	 */
	public int getRowCount(String strKey, int intColumnNum) {
		return getRowCount(strKey, intColumnNum, 0);
	}

	/**
	 * Function to search for a specified key within a row, and return the
	 * corresponding column number
	 * 
	 * @param key
	 *            The value being searched for
	 * @param rowNum
	 *            The row number in which the key should be searched
	 * @return The column number in which the specified key is found (-1 if the
	 *         key is not found)
	 */
	public int getColumnNum(String strKey, int intRowNum) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		HSSFRow row = worksheet.getRow(intRowNum);
		String strCurrentValue;
		for (int intCurrentColumnNum = 0; intCurrentColumnNum < row.getLastCellNum(); intCurrentColumnNum++) {

			HSSFCell cell = row.getCell(intCurrentColumnNum);
			strCurrentValue = getCellValueAsString(cell, formulaEvaluator);

			if (strCurrentValue.equals(strKey)) {
				return intCurrentColumnNum;
			}
		}

		return -1;
	}

	/**
	 * Function to get the value in the cell identified by the specified row and
	 * column numbers
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnNum
	 *            The column number of the cell
	 * @return The value present in the cell
	 */
	public String getValue(int intRowNum, int intColumnNum) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		HSSFRow row = worksheet.getRow(intRowNum);
		HSSFCell cell = row.getCell(intColumnNum);
		return getCellValueAsString(cell, formulaEvaluator);
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
		
		//introduced to ignore the error
		//try {
			
			//original
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		HSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int intColumnNum = -1;
		String strCurrentValue;
		for (int intCurrentColumnNum = 0; intCurrentColumnNum < row.getLastCellNum(); intCurrentColumnNum++) {

			HSSFCell cell = row.getCell(intCurrentColumnNum);
			strCurrentValue = getCellValueAsString(cell, formulaEvaluator);

			if (strCurrentValue.equals(strColumnHeader)) {
				intColumnNum = intCurrentColumnNum;
				break;
			}
			
		}

		if (intColumnNum == -1) {
			throw new FrameworkException("The specified column header \""
					+ strColumnHeader + "\"" + "is not found in the sheet \""
					+ strDatasheetName + "\"!");
		} else {
			row = worksheet.getRow(intRowNum);
			HSSFCell cell = row.getCell(intColumnNum);
			
			return getCellValueAsString(cell, formulaEvaluator);
		}
		
		//original
		
		//introduced to ignore the error
		//}catch (Exception e) {
			//System.out.println(e.getMessage());
			//return null;
		//}
	}

	private HSSFCellStyle applyCellStyle(HSSFWorkbook workbook,
			ExcelCellFormatting cellFormatting) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		if (cellFormatting.blnCentred) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		}
		cellStyle.setFillForegroundColor(cellFormatting.getBackColorIndex());
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFFont font = workbook.createFont();
		font.setFontName(cellFormatting.strGetFontName());
		font.setFontHeightInPoints(cellFormatting.getFontSize());
		if (cellFormatting.blnBold) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		font.setColor(cellFormatting.getForeColorIndex());
		cellStyle.setFont(font);

		return cellStyle;
	}

	/**
	 * Function to set the specified value in the cell identified by the
	 * specified row and column numbers
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnNum
	 *            The column number of the cell
	 * @param value
	 *            The value to be set in the cell
	 */
	public void setValue(int intRowNum, int intColumnNum, String strValue) {
		setValue(intRowNum, intColumnNum, strValue, null);
	}

	/**
	 * Function to set the specified value in the cell identified by the
	 * specified row and column numbers
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnNum
	 *            The column number of the cell
	 * @param value
	 *            The value to be set in the cell
	 * @param cellFormatting
	 *            The {@link ExcelCellFormatting} to be applied to the cell
	 */
	public void setValue(int intRowNum, int intColumnNum, String strValue,
			ExcelCellFormatting cellFormatting) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		HSSFRow row = worksheet.getRow(intRowNum);
		HSSFCell cell = row.createCell(intColumnNum);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(strValue);

		if (cellFormatting != null) {
			HSSFCellStyle cellStyle = applyCellStyle(workbook, cellFormatting);
			cell.setCellStyle(cellStyle);
		}

		writeIntoFile(workbook);
	}

	/**
	 * Function to set the specified value in the cell identified by the
	 * specified row number and column header
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnHeader
	 *            The column header of the cell
	 * @param value
	 *            The value to be set in the cell
	 */
	public void setValue(int intRowNum, String strColumnHeader, String strValue) {
		setValue(intRowNum, strColumnHeader, strValue, null);
	}

	/**
	 * Function to set the specified value in the cell identified by the
	 * specified row number and column header
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnHeader
	 *            The column header of the cell
	 * @param value
	 *            The value to be set in the cell
	 * @param cellFormatting
	 *            The {@link ExcelCellFormatting} to be applied to the cell
	 */
	public void setValue(int intRowNum, String strColumnHeader, String strValue,
			ExcelCellFormatting cellFormatting) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		HSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int intColumnNum = -1;
		String strCurrentValue;
		for (int intCurrentColumnNum = 0; intCurrentColumnNum < row.getLastCellNum(); intCurrentColumnNum++) {

			HSSFCell cell = row.getCell(intCurrentColumnNum);
			strCurrentValue = getCellValueAsString(cell, formulaEvaluator);

			if (strCurrentValue.equals(strColumnHeader)) {
				intColumnNum = intCurrentColumnNum;
				break;
			}
		}

		if (intColumnNum == -1) {
			throw new FrameworkException("The specified column header \""
					+ strColumnHeader + "\"" + "is not found in the sheet \""
					+ strDatasheetName + "\"!");
		} else {
			row = worksheet.getRow(intRowNum);
			HSSFCell cell = row.createCell(intColumnNum);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(strValue);

			if (cellFormatting != null) {
				HSSFCellStyle cellStyle = applyCellStyle(workbook,
						cellFormatting);
				cell.setCellStyle(cellStyle);
			}

			writeIntoFile(workbook);
		}
	}

	/**
	 * Function to set a hyperlink in the cell identified by the specified row
	 * and column numbers
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnNum
	 *            The column number of the cell
	 * @param linkAddress
	 *            The link address to be set
	 */
	public void setHyperlink(int intRowNum, int intColumnNum, String strLinkAddress) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		HSSFRow row = worksheet.getRow(intRowNum);
		HSSFCell cell = row.getCell(intColumnNum);
		if (cell == null) {
			throw new FrameworkException("Specified cell is empty! "
					+ "Please set a value before including a hyperlink...");
		}

		setCellHyperlink(workbook, cell, strLinkAddress);

		writeIntoFile(workbook);
	}

	private void setCellHyperlink(HSSFWorkbook workbook, HSSFCell cell,
			String strLinkAddress) {
		HSSFCellStyle cellStyle = cell.getCellStyle();
		HSSFFont font = cellStyle.getFont(workbook);
		font.setUnderline(HSSFFont.U_SINGLE);
		cellStyle.setFont(font);

		CreationHelper creationHelper = workbook.getCreationHelper();
		Hyperlink hyperlink = creationHelper
				.createHyperlink(Hyperlink.LINK_URL);
		hyperlink.setAddress(strLinkAddress);

		cell.setCellStyle(cellStyle);
		cell.setHyperlink(hyperlink);
	}

	/**
	 * Function to set a hyperlink in the cell identified by the specified row
	 * number and column header
	 * 
	 * @param rowNum
	 *            The row number of the cell
	 * @param columnHeader
	 *            The column header of the cell
	 * @param linkAddress
	 *            The link address to be set
	 */
	public void setHyperlink(int intRowNum, String strColumnHeader, String strLinkAddress) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);
		FormulaEvaluator formulaEvaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();

		HSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int intColumnNum = -1;
		String strCurrentValue;
		for (int intCurrentColumnNum = 0; intCurrentColumnNum < row.getLastCellNum(); intCurrentColumnNum++) {

			HSSFCell cell = row.getCell(intCurrentColumnNum);
			strCurrentValue = getCellValueAsString(cell, formulaEvaluator);

			if (strCurrentValue.equals(strColumnHeader)) {
				intColumnNum = intCurrentColumnNum;
				break;
			}
		}

		if (intColumnNum == -1) {
			throw new FrameworkException("The specified column header \""
					+ strColumnHeader + "\"" + "is not found in the sheet \""
					+ strDatasheetName + "\"!");
		} else {
			row = worksheet.getRow(intRowNum);
			HSSFCell cell = row.getCell(intColumnNum);
			if (cell == null) {
				throw new FrameworkException("Specified cell is empty! "
						+ "Please set a value before including a hyperlink...");
			}

			setCellHyperlink(workbook, cell, strLinkAddress);

			writeIntoFile(workbook);
		}
	}

	/**
	 * Function to create a new Excel workbook
	 */
	public void createWorkbook() {
		HSSFWorkbook workbook = new HSSFWorkbook();

		writeIntoFile(workbook);
	}

	/**
	 * Function to add a sheet to the Excel workbook
	 * 
	 * @param sheetName
	 *            The sheet name to be added
	 */
	public void addSheet(String strSheetName) {
		HSSFWorkbook workbook = openFileForReading();

		HSSFSheet worksheet = workbook.createSheet(strSheetName);
		worksheet.createRow(0); // include a blank row in the sheet created

		writeIntoFile(workbook);

		this.strDatasheetName = strSheetName;
	}

	/**
	 * Function to add a new row to the Excel worksheet
	 * 
	 * @return The row number of the newly added row
	 */
	public int addRow() {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		int intNewRowNum = worksheet.getLastRowNum() + 1;
		worksheet.createRow(intNewRowNum);

		writeIntoFile(workbook);

		return intNewRowNum;
	}

	/**
	 * Function to add a new column to the Excel worksheet
	 * 
	 * @param columnHeader
	 *            The column header to be added
	 */
	public void addColumn(String strColumnHeader) {
		addColumn(strColumnHeader, null);
	}

	/**
	 * Function to add a new column to the Excel worksheet
	 * 
	 * @param columnHeader
	 *            The column header to be added
	 * @param cellFormatting
	 *            The {@link ExcelCellFormatting} to be applied to the column
	 *            header
	 */
	public void addColumn(String strColumnHeader,
			ExcelCellFormatting cellFormatting) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		HSSFRow row = worksheet.getRow(0); // 0 because header is always in the
											// first row
		int intLastCellNum = row.getLastCellNum();
		if (intLastCellNum == -1) {
			intLastCellNum = 0;
		}

		HSSFCell cell = row.createCell(intLastCellNum);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(strColumnHeader);

		if (cellFormatting != null) {
			HSSFCellStyle cellStyle = applyCellStyle(workbook, cellFormatting);
			cell.setCellStyle(cellStyle);
		}

		writeIntoFile(workbook);
	}

	/**
	 * Function to set a specified color at the specified index within the
	 * custom palette
	 * 
	 * @param index
	 *            The index at which the color should be set within the palette
	 * @param hexColor
	 *            The hex value of the color to be set within the palette
	 */
	public void setCustomPaletteColor(short index, String strHexColor) {
		HSSFWorkbook workbook = openFileForReading();
		HSSFPalette palette = workbook.getCustomPalette();

		if (index < 0x8 || index > 0x40) {
			throw new FrameworkException(
					"Valid indexes for the Excel custom palette are from 0x8 to 0x40 (inclusive)!");
		}

		Color color = Color.decode(strHexColor);
		palette.setColorAtIndex(index, (byte) color.getRed(),
				(byte) color.getGreen(), (byte) color.getBlue());

		writeIntoFile(workbook);
	}

	/**
	 * Function to merge the specified range of cells (all inputs are 0-based)
	 * 
	 * @param firstRow
	 *            The first row
	 * @param lastRow
	 *            The last row
	 * @param firstCol
	 *            The first column
	 * @param lastCol
	 *            The last column
	 */
	public void mergeCells(int intFirstRow, int intLastRow, int intFirstCol, int intLastCol) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		CellRangeAddress cellRangeAddress = new CellRangeAddress(intFirstRow,
				intLastRow, intFirstCol, intLastCol);
		worksheet.addMergedRegion(cellRangeAddress);

		writeIntoFile(workbook);
	}

	/**
	 * Function to specify whether the row summaries appear below the detail
	 * within an outline (grouped set of rows)
	 * 
	 * @param rowSumsBelow
	 *            Boolean value to specify row summaries below detail within an
	 *            outline
	 */
	public void setRowSumsBelow(boolean blnRowSumsBelow) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		worksheet.setRowSumsBelow(blnRowSumsBelow);

		writeIntoFile(workbook);
	}

	/**
	 * Function to outline (i.e., group together) the specified rows
	 * 
	 * @param firstRow
	 *            The first row
	 * @param lastRow
	 *            The last row
	 */
	public void groupRows(int intFirstRow, int intLastRow) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		worksheet.groupRow(intFirstRow, intLastRow);

		writeIntoFile(workbook);
	}

	/**
	 * Function to automatically adjust the column width to fit the contents for
	 * the specified range of columns (all inputs are 0-based)
	 * 
	 * @param firstCol
	 *            The first column
	 * @param lastCol
	 *            The last column
	 */
	public void autoFitContents(int intFirstCol, int intLastCol) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		if (intFirstCol < 0) {
			intFirstCol = 0;
		}

		if (intFirstCol > intLastCol) {
			throw new FrameworkException(
					"First column cannot be greater than last column!");
		}

		for (int intCurrentColumn = intFirstCol; intCurrentColumn <= intLastCol; intCurrentColumn++) {
			worksheet.autoSizeColumn(intCurrentColumn);
		}

		writeIntoFile(workbook);
	}

	/**
	 * Function to add an outer border around the specified range of columns
	 * (all inputs are 0-based)
	 * 
	 * @param firstCol
	 *            The first column
	 * @param lastCol
	 *            The last column
	 */
	public void addOuterBorder(int intFirstCol, int intLastCol) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		CellRangeAddress cellRangeAddress = new CellRangeAddress(0,
				worksheet.getLastRowNum(), intFirstCol, intLastCol);
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				cellRangeAddress, worksheet, workbook);
		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				cellRangeAddress, worksheet, workbook);

		writeIntoFile(workbook);
	}

	/**
	 * Function to add an outer border around the specified range of columns
	 * (all inputs are 0-based)
	 * 
	 * @param firstRow
	 *            The first row
	 * @param lastRow
	 *            The last row
	 * @param firstCol
	 *            The first column
	 * @param lastCol
	 *            The last column
	 */
	public void addOuterBorder(int intFirstRow, int intLastRow, int intFirstCol,
			int intLastCol) {
		checkPreRequisites();

		HSSFWorkbook workbook = openFileForReading();
		HSSFSheet worksheet = getWorkSheet(workbook);

		CellRangeAddress cellRangeAddress = new CellRangeAddress(intFirstRow,
				intLastRow, intFirstCol, intLastCol);
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				cellRangeAddress, worksheet, workbook);
		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				cellRangeAddress, worksheet, workbook);
		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				cellRangeAddress, worksheet, workbook);
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				cellRangeAddress, worksheet, workbook);

		writeIntoFile(workbook);
	}
}