package com.temenos.infinity.smartbanking.advisory.javaservices;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.LinkedHashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.smartbanking.advisory.utils.*;
import com.temenos.infinity.smartbanking.advisory.businessdelegate.api.SmartBankingAdvisoryBusinessDelegate;
import com.temenos.infinity.smartbanking.advisory.constants.GeneratedCashFlowFileDetailsEnum;

public class DownloadCashFlowExcelOperation implements JavaService2 {
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
		GeneratedCashFlowFileDetailsEnum file = GeneratedCashFlowFileDetailsEnum.valueOf(prefix);
		Map<String, String> customHeaders = new HashMap<>();
		customHeaders.put(HttpHeaders.CONTENT_TYPE, file.getContentType());
		customHeaders.put("Access-Control-Expose-Headers", "Content-Disposition");
		customHeaders.put("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
		return customHeaders;
	}

	private Map<String, String> generateExcelList(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy & HH:mm:ss");
		Date date = new Date();
		BackendCommonUtils backendUtils = new BackendCommonUtils();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Cash Flow details");
		Map<String, Object> payloadMap = new HashMap<>();
		Map<String, Object> responseTransactionMap = new LinkedHashMap<>();
		Map<String, Object> responsePredictiveMap = new LinkedHashMap<>();
		Map<String, Object> responseHeadersMap = new HashMap<>();
		DecimalFormat df = new DecimalFormat("#,#00.0#");
		Font font = sheet.getWorkbook().createFont();
		CellStyle style = sheet.getWorkbook().createCellStyle();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		LocalDate currentdate = LocalDate.now();
		int currentdateMonth = currentdate.getMonthValue();
		int currentdateYear = currentdate.getYear();
		currentdateMonth = currentdateMonth + 3;
		ObjectMapper objectMapper = new ObjectMapper();

		payloadMap = (Map<String, Object>) inputArray[1];
		if (StringUtils.isEmpty((String) payloadMap.get("subscriber"))
				&& StringUtils.isEmpty((String) payloadMap.get("customerId"))) {
			CommonUtils.constructAndThrowValidationException("Subscriber and Customer Id are mandatory");
		}
		SmartBankingAdvisoryBusinessDelegate SBABusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class);
		Map<String, Object> cashFlowFullResp = SBABusinessDelegate.get12MonthCashFlow(payloadMap);
		String transactionsStr = objectMapper.writeValueAsString(cashFlowFullResp.get("recordsPast"));
		List<Map<String, Object>> transactionsDetails = objectMapper.readValue(transactionsStr,
				new TypeReference<List<Map<String, Object>>>() {
				});
		String predictiveDataStr = objectMapper.writeValueAsString(cashFlowFullResp.get("recordsPredictive"));
		List<Map<String, Object>> predictiveDataDetails = objectMapper.readValue(predictiveDataStr,
				new TypeReference<List<Map<String, Object>>>() {
				});

		Collections.sort(transactionsDetails, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
				return (m2.get("date").toString().compareTo(m1.get("date").toString()));
			}
		});

		Collections.sort(predictiveDataDetails, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
				return (m2.get("month").toString().compareTo(m1.get("month").toString()));
			}
		});

		double moneyInTotal = 0;
		double moneyOutTotal = 0;
		double cashOnHandTotal = 0;
		double netDisposableIncomeTransactionsTotal = 0;
		int i = 0;
		for (Map<String, Object> transactionsData : transactionsDetails) {
			i++;
			if (i < 10) {
				moneyInTotal = moneyInTotal + Double.parseDouble(transactionsData.get("moneyIn").toString());
				moneyOutTotal = moneyOutTotal + Double.parseDouble(transactionsData.get("moneyOut").toString());
				cashOnHandTotal = cashOnHandTotal + Double.parseDouble(transactionsData.get("cashOnHand").toString());
				netDisposableIncomeTransactionsTotal = netDisposableIncomeTransactionsTotal
						+ Double.parseDouble(transactionsData.get("netDisposableIncome").toString());
			}
		}
		double netDisposableIncomePredictionTotal = predictiveDataDetails.stream()
				.mapToDouble(map -> Double.parseDouble(map.get("netDisposableIncome").toString())).sum()
				+ netDisposableIncomeTransactionsTotal;

		int indexTransactions = 0;
		for (Map<String, Object> transactionsData : transactionsDetails) {
			indexTransactions++;
			if (indexTransactions < 10) {
				responseTransactionMap.put(String.valueOf(indexTransactions),
						new Object[] {
								transactionsData.get("yearMonth").toString().substring(0, 4) + "-"
										+ transactionsData.get("month"),
								transactionsData.get("moneyIn"), transactionsData.get("moneyOut"),
								transactionsData.get("cashOnHand"), transactionsData.get("netDisposableIncome") });
			}
		}

		int indexPrediction = 0;
		for (Map<String, Object> predictiveData : predictiveDataDetails) {
			indexPrediction++;
			responsePredictiveMap.put(String.valueOf(indexPrediction),
					new Object[] { (currentdateYear + "-" + currentdateMonth).toString(), 0, 0, 0,
							predictiveData.get("netDisposableIncome") });
			currentdateMonth--;
		}
		Row titleRow = sheet.createRow(0);
		titleRow.createCell(0).setCellValue("Title");
		titleRow.createCell(1).setCellValue("Cash Flow & Prediction");
		Row exportedDetails = sheet.createRow(1);
		exportedDetails.createCell(0).setCellValue("Exported By");
		exportedDetails.createCell(1).setCellValue(payloadMap.get("customerId").toString());
		Row exportedDate = sheet.createRow(2);
		exportedDate.createCell(0).setCellValue("Exported Date");
		exportedDate.createCell(1).setCellValue(formatter.format(date));
		Row companyRow = sheet.createRow(3);
		companyRow.createCell(0).setCellValue("Company");
		companyRow.createCell(1).setCellValue(payloadMap.get("subscriber").toString());
		int headRownum = 6;
		responseHeadersMap.put("" + headRownum,
				new Object[] { "Date", "Cash Inflow", "Cash Outflow", "Cash On Hand", "Net Disposable Income" });

		for (String headKey : responseHeadersMap.keySet()) {
			Row headRow = sheet.createRow(headRownum);
			Object[] objHeadArr = (Object[]) responseHeadersMap.get(headKey);
			int headCellnum = 0;
			for (Object obj : objHeadArr) {
				Cell headCell = headRow.createCell(headCellnum++);
				headCell.setCellValue((String) obj);
			}
		}
		font.setColor((short) 2);
		style.setFont(font);
		int rownumPredictive = headRownum + 1;
		for (String key : responsePredictiveMap.keySet()) {
			Row row = sheet.createRow(rownumPredictive++);
			Object[] objArr = (Object[]) responsePredictiveMap.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof Integer) {
					cell.setCellValue("$" + (Integer) obj);
				} else if (obj instanceof Double) {
					if (obj.toString().contains("-")) {
						cell.setCellValue("$" + df.format(obj));
						cell.setCellStyle(style);
					} else {
						cell.setCellValue("$" + df.format(obj));
					}
				} else
					cell.setCellValue((String) obj);
			}
		}

		int rownum = 10;
		for (String key : responseTransactionMap.keySet()) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = (Object[]) responseTransactionMap.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof Integer) {
					cell.setCellValue("$" + (Integer) obj);
				} else if (obj instanceof Double) {
					if (obj.toString().contains("-")) {
						cell.setCellValue("$" + df.format(obj));
						cell.setCellStyle(style);
					} else {
						cell.setCellValue("$" + df.format(obj));
					}
				} else
					cell.setCellValue((String) obj);
			}
		}

		Row totalRow = sheet.createRow(20);
		totalRow.createCell(0).setCellValue("Total");
		totalRow.createCell(1).setCellValue("$" + df.format(backendUtils.roundDoubleValueTo2Digits(moneyInTotal)));
		totalRow.createCell(2).setCellValue("$" + df.format(backendUtils.roundDoubleValueTo2Digits(moneyOutTotal)));
		totalRow.createCell(3).setCellValue("$" + df.format(backendUtils.roundDoubleValueTo2Digits(cashOnHandTotal)));
		totalRow.createCell(4).setCellValue(
				"$" + df.format(backendUtils.roundDoubleValueTo2Digits(netDisposableIncomePredictionTotal)));
		try {
			workbook.write(bos);
		} finally {
			bos.close();
			workbook.close();
		}
		byte[] bytes = bos.toByteArray();
		Map<String, String> responsesMap = new HashMap<>();
		String fileId = "CFX" + CommonUtils.generateUniqueID(32);
		MemoryManager.saveIntoCache(fileId, Base64.encodeBase64String(bytes), 120);
		responsesMap.put("fileId", fileId);
		return responsesMap;
	}

}
