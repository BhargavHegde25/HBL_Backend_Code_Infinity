package com.temenos.infinity.smartbanking.advisory.javaservices;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.smartbanking.advisory.businessdelegate.api.SmartBankingAdvisoryBusinessDelegate;
import com.temenos.infinity.smartbanking.advisory.constants.GeneratedAccountReceivablesDetailsEnum;
import com.temenos.infinity.smartbanking.advisory.utils.CommonUtils;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.Log4j2Configurator;

public class DownloadReceivableCustomerDetailsExcelOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		Map<String, String> fileId = null;
		Map<String, Object> payloadMap = new HashMap<>();
		payloadMap = (Map<String, Object>) inputArray[1];
		if (payloadMap.get("type").equals("Overdue")) {
			fileId = generateOverdueExcelList(methodId, inputArray, request, response);
		} else if (payloadMap.get("type").equals("Upcoming")) {
			fileId = generateUpcomingExcelList(methodId, inputArray, request, response);
		} else if (payloadMap.get("type").equals("Bycustomer")) {
			fileId = generateByCustomerExcelList(methodId, inputArray, request, response);
		}
		String fileId1 = fileId.get("fileId");
		String fileDetails = (String) MemoryManager.getFromCache(fileId1);
		MemoryManager.removeFromCache(fileId1);
		byte[] bytes = Base64.decodeBase64(fileDetails);
		try {
			response.getHeaders().putAll(getCustomHeaders(fileId1.substring(0, 5)));
			response.setAttribute("chunkedresults_json", new BufferedHttpEntity(new ByteArrayEntity(bytes)));
			response.setStatusCode(200);
			alert.prepareError(response.toString()).log();
		} catch (Exception e) {
			alert.prepareError("Error while downloading the generated file", e).log();
		}
		return result;
	}

	private Map<String, String> getCustomHeaders(String prefix) {
		GeneratedAccountReceivablesDetailsEnum file = GeneratedAccountReceivablesDetailsEnum.valueOf(prefix);
		Map<String, String> customHeaders = new HashMap<>();
		customHeaders.put(HttpHeaders.CONTENT_TYPE, file.getContentType());
		customHeaders.put("Access-Control-Expose-Headers", "Content-Disposition");
		customHeaders.put("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
		return customHeaders;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> generateOverdueExcelList(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) throws Exception {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("AID Receivables Overdue");
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Map<String, Object> payloadMap = new HashMap<>();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 11);
		font.setBold(true);
		font.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);

		Font font20 = workbook.createFont();
		font20.setFontName("Calibri");
		font20.setFontHeightInPoints((short) 20);
		font20.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style20 = workbook.createCellStyle();
		style20.setFont(font20);

		Font font16 = workbook.createFont();
		font16.setFontName("Calibri");
		font16.setFontHeightInPoints((short) 16);
		font16.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style16 = workbook.createCellStyle();
		style16.setFont(font16);

		Font font14 = workbook.createFont();
		font14.setFontName("Calibri");
		font14.setFontHeightInPoints((short) 14);
		font14.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style14 = workbook.createCellStyle();
		style14.setFont(font14);

		Font fontAligment = workbook.createFont();
		fontAligment.setFontName("Calibri");
		fontAligment.setBold(true);
		CellStyle styleAlignment = workbook.createCellStyle();
		styleAlignment.setAlignment(HorizontalAlignment.CENTER);
		styleAlignment.setVerticalAlignment(VerticalAlignment.CENTER);
		styleAlignment.setFont(fontAligment);

		payloadMap = (Map<String, Object>) inputArray[1];
		if (StringUtils.isEmpty((String) payloadMap.get("queryParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("modelName"))
				|| StringUtils.isEmpty((String) payloadMap.get("topParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("orderParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("Subscriber"))
				|| StringUtils.isEmpty((String) payloadMap.get("businessName"))
				|| StringUtils.isEmpty((String) payloadMap.get("type"))) {
			CommonUtils.constructAndThrowValidationException("All the params are mandatory");
		}

		SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
		@SuppressWarnings("unused")
		Map<String, Object> ReceviablesFullResp = new HashMap<String, Object>();
		ReceviablesFullResp = SBABusinessDelegate.getReceivableOverdueExcel(payloadMap);

		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Smart Banking Advisor");
		titleCell.setCellStyle(style20);

		Row titleRow1 = sheet.createRow(2);
		Cell titleCell1 = titleRow1.createCell(0);
		titleCell1.setCellValue(payloadMap.get("businessName").toString());
		titleCell1.setCellStyle(style16);

		Row titleRow2 = sheet.createRow(4);
		Cell titleCell2 = titleRow2.createCell(0);
		titleCell2.setCellValue("List of Customers with Overdue Invoices");
		titleCell2.setCellStyle(style14);

		Map<String, Object> responseHeadersMap = new HashMap<>();
		int headRownum = 6;
		responseHeadersMap.put("0" + headRownum, new Object[] { "Customer Name", "", "No. Invoices", "Amount" });

		for (String headKey : responseHeadersMap.keySet()) {
			Row headRow = sheet.createRow(headRownum);
			Object[] objHeadArr = (Object[]) responseHeadersMap.get(headKey);
			int headCellnum = 0;
			for (Object obj : objHeadArr) {
				Cell headCellHeaders = headRow.createCell(headCellnum++);
				headCellHeaders.setCellValue((String) obj);
				headCellHeaders.setCellStyle(style);
			}
			headRownum++;
		}
		int rowNum = 7;
		for (String key : ReceviablesFullResp.keySet()) {
			Map<String, Object> customerData = (Map<String, Object>) ReceviablesFullResp.get(key);
			Row row = sheet.createRow(rowNum);
			int cellNum = 0;
			for (String dataKey : customerData.keySet()) {
				Cell cell = row.createCell(cellNum);
				Object dataValue = customerData.get(dataKey);
				if (dataValue instanceof String) {
					cell.setCellValue((String) dataValue);
				} else if (dataValue instanceof Number) {
					if (dataValue instanceof Double) {
						cell.setCellValue(formatter.format(dataValue));
					} else if (dataValue instanceof Integer) {
						cell.setCellValue(formatter.format(dataValue));
					}
				}
				cellNum++;
			}
			rowNum++;
		}

		for (int i = 0; i < 22; i++) {
			sheet.autoSizeColumn(i);
		}
		try {
			workbook.write(bos);
		} finally {
			bos.close();
			workbook.close();
		}
		byte[] bytes = bos.toByteArray();
		Map<String, String> responsesMap = new HashMap<>();
		String fileId = "RSXAO" + CommonUtils.generateUniqueID(32);
		MemoryManager.saveIntoCache(fileId, Base64.encodeBase64String(bytes), 120);
		responsesMap.put("fileId", fileId);
		return responsesMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> generateUpcomingExcelList(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) throws Exception {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("AID Receivables Upcoming");
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Map<String, Object> payloadMap = new HashMap<>();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 11);
		font.setBold(true);
		font.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);

		Font font20 = workbook.createFont();
		font20.setFontName("Calibri");
		font20.setFontHeightInPoints((short) 20);
		font20.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style20 = workbook.createCellStyle();
		style20.setFont(font20);

		Font font16 = workbook.createFont();
		font16.setFontName("Calibri");
		font16.setFontHeightInPoints((short) 16);
		font16.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style16 = workbook.createCellStyle();
		style16.setFont(font16);

		Font font14 = workbook.createFont();
		font14.setFontName("Calibri");
		font14.setFontHeightInPoints((short) 14);
		font14.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style14 = workbook.createCellStyle();
		style14.setFont(font14);

		Font fontAligment = workbook.createFont();
		fontAligment.setFontName("Calibri");
		fontAligment.setBold(true);
		CellStyle styleAlignment = workbook.createCellStyle();
		styleAlignment.setAlignment(HorizontalAlignment.CENTER);
		styleAlignment.setVerticalAlignment(VerticalAlignment.CENTER);
		styleAlignment.setFont(fontAligment);

		payloadMap = (Map<String, Object>) inputArray[1];
		if (StringUtils.isEmpty((String) payloadMap.get("queryParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("modelName"))
				|| StringUtils.isEmpty((String) payloadMap.get("topParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("orderParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("Subscriber"))
				|| StringUtils.isEmpty((String) payloadMap.get("businessName"))
				|| StringUtils.isEmpty((String) payloadMap.get("type"))) {
			CommonUtils.constructAndThrowValidationException("All the params are mandatory");
		}

		SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
		@SuppressWarnings("unused")
		Map<String, Object> ReceviablesFullResp = new HashMap<String, Object>();
		ReceviablesFullResp = SBABusinessDelegate.getReceivableUpcomingExcel(payloadMap);

		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Smart Banking Advisor");
		titleCell.setCellStyle(style20);

		Row titleRow1 = sheet.createRow(2);
		Cell titleCell1 = titleRow1.createCell(0);
		titleCell1.setCellValue(payloadMap.get("businessName").toString());
		titleCell1.setCellStyle(style16);

		Row titleRow2 = sheet.createRow(4);
		Cell titleCell2 = titleRow2.createCell(0);
		titleCell2.setCellValue("List of Customers with Upcoming  Invoices");
		titleCell2.setCellStyle(style14);

		Map<String, Object> responseHeadersMap = new HashMap<>();
		int headRownum = 6;
		responseHeadersMap.put("0" + headRownum, new Object[] { "Customer Name", "", "No. Invoices", "Amount" });

		for (String headKey : responseHeadersMap.keySet()) {
			Row headRow = sheet.createRow(headRownum);
			Object[] objHeadArr = (Object[]) responseHeadersMap.get(headKey);
			int headCellnum = 0;
			for (Object obj : objHeadArr) {
				Cell headCellHeaders = headRow.createCell(headCellnum++);
				headCellHeaders.setCellValue((String) obj);
				headCellHeaders.setCellStyle(style);
			}
			headRownum++;
		}
		int rowNum = 7;
		for (String key : ReceviablesFullResp.keySet()) {
			Map<String, Object> customerData = (Map<String, Object>) ReceviablesFullResp.get(key);
			Row row = sheet.createRow(rowNum);
			int cellNum = 0;
			for (String dataKey : customerData.keySet()) {
				Cell cell = row.createCell(cellNum);
				Object dataValue = customerData.get(dataKey);
				if (dataValue instanceof String) {
					cell.setCellValue((String) dataValue);
				} else if (dataValue instanceof Number) {
					if (dataValue instanceof Double) {
						cell.setCellValue(formatter.format(dataValue));
					} else if (dataValue instanceof Integer) {
						cell.setCellValue(formatter.format(dataValue));
					}
				}
				cellNum++;
			}
			rowNum++;
		}
		for (int i = 0; i < 22; i++) {
			sheet.autoSizeColumn(i);
		}
		try {
			workbook.write(bos);
		} finally {
			bos.close();
			workbook.close();
		}
		byte[] bytes = bos.toByteArray();
		Map<String, String> responsesMap = new HashMap<>();
		String fileId = "RSXAU" + CommonUtils.generateUniqueID(32);
		MemoryManager.saveIntoCache(fileId, Base64.encodeBase64String(bytes), 120);
		responsesMap.put("fileId", fileId);
		return responsesMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> generateByCustomerExcelList(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) throws Exception {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("AID Receivables Bycustomer");
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Map<String, Object> payloadMap = new HashMap<>();
		Font font = workbook.createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 11);
		font.setBold(true);
		font.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);

		Font font20 = workbook.createFont();
		font20.setFontName("Calibri");
		font20.setFontHeightInPoints((short) 20);
		font20.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style20 = workbook.createCellStyle();
		style20.setFont(font20);

		Font font16 = workbook.createFont();
		font16.setFontName("Calibri");
		font16.setFontHeightInPoints((short) 16);
		font16.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style16 = workbook.createCellStyle();
		style16.setFont(font16);

		Font font14 = workbook.createFont();
		font14.setFontName("Calibri");
		font14.setFontHeightInPoints((short) 14);
		font14.setColor(IndexedColors.BLACK.getIndex());
		CellStyle style14 = workbook.createCellStyle();
		style14.setFont(font14);

		Font fontAligment = workbook.createFont();
		fontAligment.setFontName("Calibri");
		fontAligment.setBold(true);
		CellStyle styleAlignment = workbook.createCellStyle();
		styleAlignment.setAlignment(HorizontalAlignment.CENTER);
		styleAlignment.setVerticalAlignment(VerticalAlignment.CENTER);
		styleAlignment.setFont(fontAligment);

		payloadMap = (Map<String, Object>) inputArray[1];
		if (StringUtils.isEmpty((String) payloadMap.get("queryParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("modelName"))
				|| StringUtils.isEmpty((String) payloadMap.get("topParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("orderParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("Subscriber"))
				|| StringUtils.isEmpty((String) payloadMap.get("businessName"))
				|| StringUtils.isEmpty((String) payloadMap.get("type"))) {
			CommonUtils.constructAndThrowValidationException("All the params are mandatory");
		}

		SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
		@SuppressWarnings("unused")
		Map<String, Object> ReceviablesFullResp = new HashMap<String, Object>();
		ReceviablesFullResp = SBABusinessDelegate.generateByCustomerExcelList(payloadMap);

		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Smart Banking Advisor");
		titleCell.setCellStyle(style20);

		Row titleRow1 = sheet.createRow(2);
		Cell titleCell1 = titleRow1.createCell(0);
		titleCell1.setCellValue(payloadMap.get("businessName").toString());
		titleCell1.setCellStyle(style16);

		Row titleRow2 = sheet.createRow(4);
		Cell titleCell2 = titleRow2.createCell(0);
		titleCell2.setCellValue("List of Customers with Receivables");
		titleCell2.setCellStyle(style14);

		Row titleRow3 = sheet.createRow(5);
		Cell titleCell3 = titleRow3.createCell(2);
		titleCell3.setCellValue("Overdue");
		titleCell3.setCellStyle(style);
		Cell titleCell4 = titleRow3.createCell(5);
		titleCell4.setCellValue("Upcoming");
		titleCell4.setCellStyle(style);

		Map<String, Object> responseHeadersMap = new HashMap<>();
		int headRownum = 6;
		responseHeadersMap.put("0" + headRownum, new Object[] { "Customer Name", "", "No. Invoices", "Amount", "",
				"No. Invoices", "Amount", "", "Total Receivables" });

		for (String headKey : responseHeadersMap.keySet()) {
			Row headRow = sheet.createRow(headRownum);
			Object[] objHeadArr = (Object[]) responseHeadersMap.get(headKey);
			int headCellnum = 0;
			for (Object obj : objHeadArr) {
				Cell headCellHeaders = headRow.createCell(headCellnum++);
				headCellHeaders.setCellValue((String) obj);
				headCellHeaders.setCellStyle(style);
			}
			headRownum++;
		}
		int rowNum = 7;
		for (String key : ReceviablesFullResp.keySet()) {
			Map<String, Object> customerData = (Map<String, Object>) ReceviablesFullResp.get(key);
			Row row = sheet.createRow(rowNum);
			int cellNum = 0;
			for (String dataKey : customerData.keySet()) {
				Cell cell = row.createCell(cellNum);
				Object dataValue = customerData.get(dataKey);
				if (dataValue instanceof String) {
					cell.setCellValue((String) dataValue);
				} else if (dataValue instanceof Number) {
					if (dataValue instanceof Double) {
						cell.setCellValue(formatter.format(dataValue));
					} else if (dataValue instanceof Integer) {
						cell.setCellValue(formatter.format(dataValue));
					}
				}
				cellNum++;
			}
			rowNum++;
		}
		for (int i = 0; i < 22; i++) {
			sheet.autoSizeColumn(i);
		}
		try {
			workbook.write(bos);
		} finally {
			bos.close();
			workbook.close();
		}
		byte[] bytes = bos.toByteArray();
		Map<String, String> responsesMap = new HashMap<>();
		String fileId = "RSXCA" + CommonUtils.generateUniqueID(32);
		MemoryManager.saveIntoCache(fileId, Base64.encodeBase64String(bytes), 120);
		responsesMap.put("fileId", fileId);
		return responsesMap;
	}
}