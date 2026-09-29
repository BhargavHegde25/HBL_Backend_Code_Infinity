package com.kony.dbputilities.fileutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.HelperMethods;

public class ExcelGenerator implements FileGenerator {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public byte[] generateFile(JsonArray data, String title, String generatedBy, String startDate, String endDate,
            Map<String, String> fieldList, Map<String, Object> otherData, String filters) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            Row row = null;
            Cell cell = null;
            int rowIndex = 10;
            int columnIndex = 0;
            String cellValue = "";
            JsonObject rowData = null;
            Sheet sheet = workbook.createSheet();

            createHeader(sheet, title, generatedBy, startDate, endDate, fieldList,
                    (String) otherData.get("accountNumber"), (String) otherData.get("bankName"));
            if (null != data && data.size() > 0) {
                for (int i = data.size() - 1; i >= 0; i--) {
                    row = sheet.createRow(rowIndex);
                    rowData = data.get(i).getAsJsonObject();
                    for (String field : fieldList.keySet()) {
                        cell = row.createCell(columnIndex);
                        cellValue = (null == rowData.get(field)) ? "" : rowData.get(field).getAsString();
                        if ("transactionDate".equals(field)) {
                            try {
                                cellValue = HelperMethods.convertDateFormat(cellValue, "MM/dd/yyyy");
                            } catch (ParseException e) {

                                alert.prepareError(e.getMessage()).log();
                            }
                        }
                        cell.setCellValue(cellValue);
                        columnIndex++;
                    }
                    rowIndex++;
                    columnIndex = 0;
                }
            }
            if (row == null) {
                int rownum = sheet.getLastRowNum();
                row = sheet.getRow(rownum);
            }
            /*int cells = row.getLastCellNum();
            for (int i = 0; i < cells; i++) {
                sheet.autoSizeColumn(i);
            }*/
            workbook.write(bos);
            return bos.toByteArray();
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (workbook != null) {
                // workbook.close();
                workbook.dispose();
            }
        }
    }
    
    public byte[] generateCombinedStatementsFile(JsonArray dataObject, String title, String generatedBy, String startDate,
            String endDate, Map<String, String> fieldList, String bankName, String currencyCode, String paymentDateFormat) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            Row row = null;
            Cell cell = null;
            int rowIndex = 7;
            int columnIndex = 0;
            String cellValue = "";
            JsonObject rowData = null;
            Sheet sheet = workbook.createSheet();
            createCombinedStatementsHeader(sheet, title, generatedBy, startDate, endDate, fieldList, bankName);
            for(int i=0;i<dataObject.size();i++) {
            	JsonElement accountElement = dataObject.get(i);
	        	JsonObject accountObject = accountElement.getAsJsonObject();
	        	String accountName = null;
	        	String accountNumber = null;
	        	JsonArray data = new JsonArray();
	        	if(StringUtils.isNotBlank(accountObject.get("accountName").toString()))
	        		accountName = accountObject.get("accountName").getAsString();
	        	if(StringUtils.isNotBlank(accountObject.get("accountNumber").toString()))
	        		accountNumber = accountObject.get("accountNumber").getAsString();
				if(StringUtils.isNotBlank(accountObject.get("transactionsList").toString()))
					data = accountObject.get("transactionsList").getAsJsonArray();
	        	rowIndex = createCombinedStatementsSubHeader(sheet, rowIndex, fieldList, accountName, accountNumber);
				if (null != data && data.size() > 0) {
					for (int j = data.size() - 1; j >= 0; j--) {
						row = sheet.createRow(rowIndex);
	                    rowData = ((JsonObject) data.get(j)).getAsJsonObject();
	                    for (String field : fieldList.keySet()) {
	                        cell = row.createCell(columnIndex);
	                        cellValue = (null == rowData.get(field)) ? "" : rowData.get(field).getAsString();
	                        if ("transactionDate".equals(field)) {
	                            try {
	                                cellValue = HelperMethods.convertDateFormat(cellValue, "MM/dd/yyyy");
	                            } catch (ParseException e) {

	                                alert.prepareError(e.getMessage()).log();
	                            }
	                        }
	                        cell.setCellValue(cellValue);
	                        columnIndex++;
	                    }
	                    rowIndex++;
	                    columnIndex = 0;
					}
				}
				
				rowIndex++; 
			}
            workbook.write(bos);
            return bos.toByteArray();
        } catch (Exception e) {
        	alert.prepareError("Error while generating excel file"+ e).log();
            throw e;
        } finally {
            if (workbook != null) {
                workbook.dispose();
            }
        }
    }

    private void createHeader(Sheet sheet, String title, String generatedBy, String startDate, String endDate,
            Map<String, String> fieldList, String accountNumber, String bankName) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);

        Row row = null;
        Cell cell = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("Title");
        cell = row.createCell(1);
        cell.setCellValue(title);
        cell.setCellStyle(style);
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("Report generated by : ");
        cell = row.createCell(1);
        cell.setCellValue(generatedBy);
        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("Report generated on : ");
        cell = row.createCell(1);
        cell.setCellValue(dateFormat.format(new Date()));
        row = sheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("Account Number : ");
        cell = row.createCell(1);
        cell.setCellValue(accountNumber);
        row = sheet.createRow(4);
        cell = row.createCell(0);
        cell.setCellValue("Bank Name : ");
        cell = row.createCell(1);
        cell.setCellValue(bankName);
        row = sheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellValue("Start Date : ");
        cell = row.createCell(1);
        try {
			startDate = HelperMethods.convertDateFormat(startDate, "MM/dd/yyyy");
			endDate = HelperMethods.convertDateFormat(endDate, "MM/dd/yyyy");
		} catch (ParseException e) {
			alert.prepareError("Error while parsing date" +e).log();
		}
        cell.setCellValue(StringUtils.isBlank(startDate) ? "NA" : startDate);
        row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue("End Date : ");
        cell = row.createCell(1);
        cell.setCellValue(StringUtils.isBlank(endDate) ? "NA" : endDate);
        row = sheet.createRow(9);
        int colIndex = 0;
        for (String field : fieldList.keySet()) {
            cell = row.createCell(colIndex);
            cell.setCellValue(fieldList.get(field));
            cell.setCellStyle(style);
            colIndex++;
        }
    }
    
    private void createCombinedStatementsHeader(Sheet sheet, String title, String generatedBy, String startDate, String endDate,
            Map<String, String> fieldList, String bankName) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Row row = null;
        Cell cell = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("Title");
        cell = row.createCell(1);
        cell.setCellValue(title);
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("Bank Name : ");
        cell = row.createCell(1);
        cell.setCellValue(bankName);
        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("Report generated by : ");
        cell = row.createCell(1);
        cell.setCellValue(generatedBy);
        row = sheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("Report generated on : ");
        cell = row.createCell(1);
        cell.setCellValue(dateFormat.format(new Date()));
        row = sheet.createRow(4);
        cell = row.createCell(0);
        cell.setCellValue("Start Date : ");
        cell = row.createCell(1);
        try {
			startDate = HelperMethods.convertDateFormat(startDate, "MM/dd/yyyy");
			endDate = HelperMethods.convertDateFormat(endDate, "MM/dd/yyyy");
		} catch (ParseException e) {
			alert.prepareError("Error while parsing date" +e).log();
		}
        cell.setCellValue(StringUtils.isBlank(startDate) ? "NA" : startDate);
        row = sheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellValue("End Date : ");
        cell = row.createCell(1);
        cell.setCellValue(StringUtils.isBlank(endDate) ? "NA" : endDate);
        row = sheet.createRow(6);
    }
    
    private int createCombinedStatementsSubHeader(Sheet sheet, int rowIndex, Map<String, String> fieldList, String accountName, String accountNumber) {
		Row row = null;
		Cell cell = null;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Account Name: " + accountName);
		rowIndex++;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(0);
		cell.setCellValue("Account Number: " + accountNumber);
		rowIndex++;
		int colIndex = 0;
		row = sheet.createRow(rowIndex);
		cell = row.createCell(colIndex);
		cell.setCellValue("Transaction Date");
		colIndex++;
		cell = row.createCell(colIndex);
		cell.setCellValue("Transaction Reference Number");
		colIndex++;
		cell = row.createCell(colIndex);
		cell.setCellValue("Description");
		colIndex++;
		cell = row.createCell(colIndex);
		cell.setCellValue("Amount");
		colIndex++;
		cell = row.createCell(colIndex);
		cell.setCellValue("Balance");
		colIndex++;
		rowIndex++;
		return rowIndex;
    }
    
    @Override
    public String getContentType() {
        return "application/vnd.ms-excel";
    }

	@Override
	public byte[] generateLoanFile(JsonArray data, String title, String generatedBy, Map<String, String> fieldList,
			Map<String, Object> otherData, String filters, String installmentType,Map<String, String> summaryDetails,String paymentDateFormat) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}