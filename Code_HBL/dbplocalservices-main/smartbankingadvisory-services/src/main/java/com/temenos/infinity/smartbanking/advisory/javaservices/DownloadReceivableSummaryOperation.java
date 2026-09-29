package com.temenos.infinity.smartbanking.advisory.javaservices;

import java.io.ByteArrayOutputStream;
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

public class DownloadReceivableSummaryOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

		Result result = new Result();
		Map<String, String> fileId = generateExcelList(methodId, inputArray, request, response);
		String fileId1 = fileId.get("fileId");
		String fileDetails = (String) MemoryManager.getFromCache(fileId1);
		MemoryManager.removeFromCache(fileId1);
		byte[] bytes = Base64.decodeBase64(fileDetails);
		try {
			response.getHeaders().putAll(getCustomHeaders(fileId1.substring(0, 3)));
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
	private Map<String, String> generateExcelList(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("AID Receivables");
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
				|| StringUtils.isEmpty((String) payloadMap.get("Subscriber"))
				|| StringUtils.isEmpty((String) payloadMap.get("topParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("orderParam"))
				|| StringUtils.isEmpty((String) payloadMap.get("businessName"))
				|| StringUtils.isEmpty((String) payloadMap.get("subTitle"))) {
			CommonUtils.constructAndThrowValidationException("All the params are mandatory");
		}
		SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
		@SuppressWarnings("unused")
		Map<String, Map<String, Object>> ReceviablesFullResp = new LinkedHashMap<>();
		ReceviablesFullResp = SBABusinessDelegate.getReceivablesSummary(payloadMap);

		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("Smart Banking Advisor");
		titleCell.setCellStyle(style20);

		Row businessName = sheet.createRow(2);
		Cell businessNameCell = businessName.createCell(0);
		businessNameCell.setCellValue(payloadMap.get("businessName").toString());
		businessNameCell.setCellStyle(style16);

		Row subTitle = sheet.createRow(4);
		Cell subTitleCell = subTitle.createCell(0);
		subTitleCell.setCellValue(payloadMap.get("subTitle").toString());
		subTitleCell.setCellStyle(style14);

		Row invoicesTitle = sheet.createRow(5);
		Cell invoicesTitleCell = invoicesTitle.createCell(1);
		invoicesTitleCell.setCellValue("Overdue Invoices");
		invoicesTitleCell.setCellStyle(style);

		Cell invoicesTitleCells = invoicesTitle.createCell(11);
		invoicesTitleCells.setCellValue("Upcoming Invoices");
		invoicesTitleCells.setCellStyle(style);

		Map<String, Object> responseHeadersMap = new HashMap<>();
		Map<String, Object> responseHeadersMapHeaders = new HashMap<>();
		int headRownum = 6;
		responseHeadersMap.put("0" + headRownum,
				new Object[] { "Customer Name", "Amount", "No. Invoices", "Amount", "No. Invoices", "Amount",
						"No. Invoices", "Amount", "No. Invoices", "Amount", "No. Invoices", "Amount", "No. Invoices",
						"Amount", "No. Invoices", "Amount", "No. Invoices", "Amount", "No. Invoices", "Amount",
						"No. Invoices", "Average Payment Days" });
		responseHeadersMapHeaders.put("0" + headRownum, new Object[] { "0-10 days", "11-30 days", "31-60 days",
				">60 days", "Total Overdue", "0-10 days", "11-30 days", "31-60 days", ">60 days", "Total Upcoming" });

		for (String headKey : responseHeadersMapHeaders.keySet()) {
			Row headRow = sheet.createRow(headRownum);
			Object[] objHeadArr = (Object[]) responseHeadersMapHeaders.get(headKey);
			int headCellnum = 1;
			for (Object obj : objHeadArr) {
				if (headCellnum % 2 == 1) {
					CellRangeAddress cellRange = new CellRangeAddress(headRownum, headRownum, headCellnum,
							headCellnum + 1);
					sheet.addMergedRegion(cellRange);
				}
				Cell headCell = headRow.createCell(headCellnum);
				headCell.setCellValue((String) obj);
				headCell.setCellStyle(styleAlignment);
				headCellnum += 2;
			}
			headRownum++;
		}
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
		int rowNum = 8;
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
						cell.setCellValue((Double) dataValue);
					} else if (dataValue instanceof Integer) {
						cell.setCellValue((Integer) dataValue);
					}
				}
				cellNum++;
			}
			rowNum++;
		}

		Map<String, Object> avgData = ReceviablesFullResp.get("Avg");
		if (avgData != null) {
			int i = 8;
			for (Map.Entry<String, Object> entry : avgData.entrySet()) {
				Row avgRow = sheet.getRow(i);
				String columnName = entry.getKey();
				String valueString = entry.getValue().toString();
				double numericValue = extractNumericValue(valueString);
				Cell cell = avgRow.createCell(21);
				cell.setCellValue(numericValue);
				i++;
			}
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
		String fileId = "RSX" + CommonUtils.generateUniqueID(32);
		MemoryManager.saveIntoCache(fileId, Base64.encodeBase64String(bytes), 120);
		responsesMap.put("fileId", fileId);
		return responsesMap;
	}

	private static double extractNumericValue(String input) {
		input = input.replaceAll("[{}]", "");
		String[] parts = input.split("=");
		if (parts.length == 2) {
			try {
				return Double.parseDouble(parts[1]);
			} catch (NumberFormatException e) {
				alert.prepareError("Error while getting the value", e).log();
			}
		}
		return 0.0;
	}

}