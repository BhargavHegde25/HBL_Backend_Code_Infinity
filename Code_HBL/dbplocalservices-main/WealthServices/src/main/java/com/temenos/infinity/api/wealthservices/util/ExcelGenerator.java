/**
 * 
 */
package com.temenos.infinity.api.wealthservices.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.lowagie.text.BadElementException;
import com.temenos.infinity.api.wealthservices.util.PDFGenerator.accountDetails;
import com.temenos.infinity.api.wealthservices.util.PDFGenerator.customerDetails;

/**
 * @author himaja.sridhar
 *
 */
public class ExcelGenerator {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private String imgFileName = "RetailBankingBanner.png";

	public byte[] generateFile(customerDetails details, accountDetails acdet, JSONArray resultArr, int columnCount,
			LinkedHashMap<String, String> fields, String navPage, String isEur) throws IOException {

		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(workbook.getSheetIndex(sheet), navPage);
		
		/** Adding Image **/
		setImageDetails(workbook, sheet);

		/** Customer Details **/
		getCustomerDetailsDiv(sheet, details, acdet, navPage);

		/** Report Details **/
		getReportGenerateDiv(sheet, navPage, isEur);
		
		/** No records **/
		if (resultArr.length() == 0) {
			getNoData(sheet);
		} else if (navPage.equals("Performance")) {
			JSONArray overviewArray = resultArr.getJSONArray(0);
			JSONObject performanceObj = resultArr.getJSONObject(1);
			getPerformanceDetailsDiv(sheet, performanceObj);
			getTableHeaderDetails(sheet, overviewArray, columnCount, fields, navPage);
		} else {
			getTableHeaderDetails(sheet, resultArr, columnCount, fields, navPage);
		}

		for(int colCnt = 0; colCnt<10;colCnt++) {
			sheet.autoSizeColumn(colCnt);
		}

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			bos.close();
			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				workbook.close();
				// workbook.dispose();
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public void setImageDetails(XSSFWorkbook workbook, XSSFSheet sheet) {
		InputStream is = ExcelGenerator.class.getClassLoader().getResourceAsStream(this.imgFileName);
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(1);
		try {
			byte[] image = IOUtils.toByteArray(is);
			int inputImagePictureID1 = workbook.addPicture(image, workbook.PICTURE_TYPE_PNG);
			is.close();
			CreationHelper helper = workbook.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor =helper.createClientAnchor();
			anchor.setCol1(0);
			anchor.setCol2(4);
			anchor.setRow1(0);
			anchor.setRow2(3);
			Picture pict = drawing.createPicture(anchor, inputImagePictureID1);

		} catch (BadElementException e) {

		} catch (Exception e) {

		}
	}
	
	@SuppressWarnings("unused")
	private void getCustomerDetailsDiv(XSSFSheet sheet, customerDetails cust, accountDetails acdet, String navPage) {
		String[] keys = customerDetails.getKeys();
		String[] values = cust.getValues();
		Row row = null;
		Cell cell = null;
		int rowInd = 4;
		for (int i = 0; i < keys.length; i++) {
			int col = 0;
			row = sheet.createRow(rowInd);
			if (values[i] != null) {
				cell = customCell(sheet, row, col, keys[i], "");
				col++;
				cell = customCell(sheet, row, col, values[i], "");
			}
			rowInd++;
		}
		if (navPage.equals("Account Activity")) {
			String[] keysA = accountDetails.getKeys();
			String[] valuesA = acdet.getValues();
			for (int i = 0; i < keysA.length; i++) {
				int col = 0;
				row = sheet.createRow(10);
				cell = customCell(sheet, row, col, keysA[i], "");
				col++;
				cell = customCell(sheet, row, col, valuesA[i], "");
			}
		}
		setBorder(sheet, 4, 5, 0, 1);
	}

	public void getReportGenerateDiv(XSSFSheet sheet, String navPage, String isEur) {
		SimpleDateFormat sdf = (isEur.equals("") ? new SimpleDateFormat("MM/dd/yyyy")
				: new SimpleDateFormat("yyyy/MM/dd"));
		Calendar cal = Calendar.getInstance();
		String header = navPage.concat(" Report");
		String content = navPage.concat(" Report as on ").concat(sdf.format(cal.getTime()));
		Row row = null;
		Cell cell = null;
		int rowInd = 7;
		row = sheet.createRow(rowInd);
		cell = customCell(sheet, row, 0, header, "bold");
		rowInd++;
		row = sheet.createRow(rowInd);
		cell = customCell(sheet, row, 0, content, "");

	}
	
	public void getPerformanceDetailsDiv(XSSFSheet sheet, JSONObject performanceObj) {
		Row row = null;
		Cell cell = null;
		int rowInd = 10;
		Set<String> keys = performanceObj.keySet();
		for (String s : keys) {
			row = sheet.createRow(rowInd);
			cell = customCell(sheet, row, 0, s.concat(": "), "");
			String data = performanceObj.get(s).toString();
			if (s.equals("Fees & Taxes") && data.contains("+") || s.equals("Current Value") && data.contains("+")
					|| s.equals("Initial Value") && data.contains("+") || s.equals("Net Deposit") && data.contains("+")
					|| s.equals("P&L") && data.contains("+") || s.equals("Time-Weighted Return") && data.contains("+")
					|| s.equals("Money-Weighted Return") && data.contains("+")) {
				data = data.replace("+", "");
				cell = customCell(sheet, row, 1, data, "");
			} else if (s.equals("Fees & Taxes") && data.contains("-") || s.equals("Current Value") && data.contains("-")
					|| s.equals("Initial Value") && data.contains("-") || s.equals("Net Deposit") && data.contains("-")
					|| s.equals("P&L") && data.contains("-") || s.equals("Time-Weighted Return") && data.contains("-")
					|| s.equals("Money-Weighted Return") && data.contains("-")) {
				String bracs = "(";
				data = bracs.concat(data.replace("-", "").concat(")"));
				cell = customCell(sheet, row, 1, data, "red");
			} else {
				cell = customCell(sheet, row, 1, performanceObj.get(s).toString(), "");
			}
			rowInd++;
		}
		setBorder(sheet,10, rowInd-1, 0, 1);
	}
	
	public void getTableHeaderDetails(XSSFSheet sheet, JSONArray resultArr, int columnCount,
			LinkedHashMap<String, String> fields, String navPage) {
		Object[] keys = fields.keySet().toArray();
		Row row = null;
		Cell cell = null;
		int rowInd = 10;
		if(navPage.equalsIgnoreCase("Performance")) {
			rowInd = 18;
		}
		int colInd = 0;
		row = sheet.createRow(rowInd);
		for (int i = 0; i < keys.length; i++) {
			cell = customCell(sheet, row, i, keys[i].toString(), "fill");
		}
		setBorder(sheet,rowInd, rowInd, 0, keys.length-1);
		for (int j = 0; j < resultArr.length(); j++) {
			rowInd++;
			JSONObject accountObject = resultArr.getJSONObject(j);
			addRow(sheet, rowInd, colInd, accountObject, fields);
		}

	}

	private void addRow(XSSFSheet sheet, int rowInd, int colInd, JSONObject accountObject,
			LinkedHashMap<String, String> fields) {
		Row row = null;
		row = sheet.createRow(rowInd);
		try {
			for (String key : fields.values()) {
				Cell cell = null;
				if (accountObject.has(key)) {
					String data = accountObject.get(key).toString();
					if (("unrealPLMkt".equals(key) && data.contains("+"))
							|| ("amount".equals(key) && data.contains("+"))
							|| ("percentageChange".equals(key) && data.contains("+"))
							|| ("benchMarkIndex".equals(key) && data.contains("+"))
							|| ("balance".equals(key) && data.contains("+"))
							|| ("marketValue".equals(key) && data.contains("+"))
							|| ("fees".equals(key) && data.contains("+"))
							|| ("portfolioReturn".equals(key) && data.contains("+")))
					{
						data = data.replace("+", "");
						cell = customCell(sheet, row, colInd, data, "");
					} else if (("unrealPLMkt".equals(key) && data.contains("-"))
							|| ("amount".equals(key) && data.contains("-"))
							|| ("percentageChange".equals(key) && data.contains("-"))
							|| ("benchMarkIndex".equals(key) && data.contains("-"))
							|| ("balance".equals(key) && data.contains("-"))
							|| ("marketValue".equals(key) && data.contains("-"))
							|| ("fees".equals(key) && data.contains("-"))
							|| ("portfolioReturn".equals(key) && data.contains("-")))
							{
						String bracs = "(";
						data = bracs.concat(data.replace("-", "").concat(")"));
						cell = customCell(sheet, row, colInd, data, "red");
					} else {
						cell = customCell(sheet, row, colInd, data, "");
					}
				} else {
					cell = customCell(sheet, row, colInd, "", "");
				}
				colInd++;
			}
		} catch (Exception e) {
			alert.prepareError("Error in generation of pdf", e).log();
		}
	}
	
	public void getNoData(XSSFSheet sheet) {
		String noRec = "No records found";
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(12);
		cell = customCell(sheet, row, 0, noRec, "bold");

	}

	public Cell customCell(XSSFSheet sheet, Row row, int col, String data, String type) {
		Cell cell = row.createCell(col);
		cell.setCellValue(data);
		cell.setCellStyle(setStyle(sheet, type));
		return cell;
	}

	private CellStyle setStyle(XSSFSheet sheet, String type) {
		CellStyle style = sheet.getWorkbook().createCellStyle();
		Font font = sheet.getWorkbook().createFont();
		font.setFontName("Times New Roman");
		font.setFontHeight((short) 240f);
		if (type.equalsIgnoreCase("red")) {
			font.setColor(IndexedColors.RED.getIndex());
		} else if (type.equalsIgnoreCase("bold")) {
			font.setBold(true);
		} else {

		}
		if (type.equalsIgnoreCase("fill")) {
			style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE1.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
		style.setFont(font);
		return style;

	}

	private void setBorder(XSSFSheet sheet, int startRow, int endRow, int startColumn, int endColumn) {
		CellRangeAddress region = new CellRangeAddress(startRow, endRow, startColumn, endColumn);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
	}
}
