package com.temenos.infinity.smartbanking.advisory.businessdelegate.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.AbstractRecord;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.smartbanking.advisory.utils.BackendCommonUtils;
import com.temenos.infinity.smartbanking.advisory.utils.CommonUtils;
import com.temenos.infinity.smartbanking.advisory.backenddelegate.api.SmartBankingAdvisoryBackendDelegate;
import com.temenos.infinity.smartbanking.advisory.businessdelegate.api.SmartBankingAdvisoryBusinessDelegate;
import com.temenos.infinity.smartbanking.advisory.errorhandling.SBAException;

public class SmartBankingAdvisoryBusinessDelegateImpl implements SmartBankingAdvisoryBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	SmartBankingAdvisoryBackendDelegate SBABackendDelegate = DBPAPIAbstractFactoryImpl
			.getBackendDelegate(SmartBankingAdvisoryBackendDelegate.class);
	BackendCommonUtils backendUtils = new BackendCommonUtils();
	
	@Override
	public Map<String, Object> getBusinessScoreAndDriversDetails(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> backendResponse = SBABackendDelegate.getBusinessScoreAndDriversDetails(payloadMap);
		return formatBusinessHealthScoreAndDrivers(backendResponse);
	}

	private Map<String, Object> formatBusinessHealthScoreAndDrivers(Map<String, Object> backendResponse)
			throws SBAException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> finalResponse = new HashMap<>();
		List<Map<String, Object>> positiveDrivers = new ArrayList<>();
		List<Map<String, Object>> negativeDrivers = new ArrayList<>();
		try {
			String driversStr = objectMapper.writeValueAsString(backendResponse.get("drivers"));
			List<Map<String, Object>> driverDetails = objectMapper.readValue(driversStr,
					new TypeReference<List<Map<String, Object>>>() {
					});
			driverDetails.stream().forEach(driver -> {
				Map<String, Object> response = new HashMap<>();
				response.put("driverRank", driver.get("DriverRank"));
				response.put("value", driver.get("Actual Value"));
				response.put("percentage", Double.valueOf(driver.get("DriverWeight").toString()) * 100);
				String[] driverNameAndGrade = driver.get("Driver").toString().replaceAll("\\s", "").split("=");
				response.put("name", driverNameAndGrade[0]);
				response.put("grading", driverNameAndGrade[1]);
				if (driver.get("ScoreLabel").toString().equalsIgnoreCase("Good")) {
					positiveDrivers.add(response);
				} else {
					negativeDrivers.add(response);
				}
			});
			finalResponse.put("DriversPositive", positiveDrivers);
			finalResponse.put("DriversNegative", negativeDrivers);
			finalResponse.put("currentScore", Double.valueOf(backendResponse.get("softCreditScore").toString()) * 100);

		} catch (Exception e) {
			alert.prepareError("Error in SmartBankingAdvisoryBusinessDelegateImpl : formatBusinessHealthScoreAndDrivers"
					+ e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException("Failed while formatting the backend response");
		}
		return finalResponse;
	}

	@Override
	public Map<String, Object> getEnrollmentStatus(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("Subscriber", payloadMap.get("subscriber"));
		return SBABackendDelegate.getEnrollmentStatus(payloadMap);
	}

	@Override
	public Map<String, Object> getAccountingData(Map<String, Object> payloadMap) throws SBAException {
		return SBABackendDelegate.getAccountingData(payloadMap);
	}

	@Override
	public Map<String, Object> startProcess(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> payload = new HashMap<String, Object>();
		try {
			Map<String, Object> backendResponse = SBABackendDelegate.startProcess(payloadMap);
			if (backendResponse.containsKey("errmsg_postConnection")){
				payload.put("errmsg_postConnection", backendResponse.get("errmsg_postConnection"));
				payload.put("isEnrollmentSucess", backendResponse.get("isEnrollmentSucess"));
			}
			else {
				payload.put("isEnrollmentSucess", backendResponse.get("isEnrollmentSucess"));
				payload.put("linkURL", backendResponse.get("linkUrl"));
			}
		} catch (Exception e) {
			alert.prepareError("Error in SmartBankingAdvisoryBusinessDelegateImpl : startProcess" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException("Failed while fetching backend response");
		}
		return payload;
	}

	@Override
	public Map<String, Object> get12MonthCashFlow(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> payload = new HashMap<String, Object>(); 
		Date d=new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int month = cal.get(Calendar.MONTH);
		int currentMonth =  ++month;
		try {
		Map<String, Object> backendResponse = SBABackendDelegate.get12MonthCashFlow(payloadMap);
		List<Map<String, Object>> formatted12MonthsTransactions = formatGet12MonthsTransactions(backendResponse);
		List<Map<String, Object>> formattedPredictionData = formatPredictionData(backendResponse);
		Map<String, Object> formattedDriversData = formatCashFlowDrivers(backendResponse);
		if (formatted12MonthsTransactions != null) {
			formatted12MonthsTransactions.stream().forEach(data -> {
				if (String.valueOf(data.get("month")).equalsIgnoreCase(String.valueOf(currentMonth))) {
					payload.put("currCashOnHand", data.get("cashOnHand"));
					payload.put("currMoneyIn", data.get("moneyIn"));
					payload.put("currMoneyOut", data.get("moneyOut"));
					payload.put("currClosingBalance", data.get("closingBalance"));
					payload.put("currNetDisposableIncome", data.get("netDisposableIncome"));
				}
			});
			if(!payload.containsKey("currCashOnHand")) {
				payload.put("currCashOnHand", 0);
				payload.put("currMoneyIn", 0);
				payload.put("currMoneyOut", 0);
				payload.put("currClosingBalance", 0);
				payload.put("currNetDisposableIncome", 0);
			}
		}
		payload.put("recordsPast", formatted12MonthsTransactions);
		payload.put("recordsPredictive", formattedPredictionData);
		payload.putAll(formattedDriversData);
		} catch (Exception e) {
			alert.prepareError("Error in SmartBankingAdvisoryBusinessDelegateImpl : get12MonthCashFlow" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException("Failed while fetching backend response for Cash Flow");
		}
		return payload;
	}

	private List<Map<String, Object>> formatGet12MonthsTransactions(Map<String, Object> backendResponse) throws SBAException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> finalTransactionsResp = new ArrayList<>();
		try {
			String transactionsStr = objectMapper.writeValueAsString(backendResponse.get("transactions"));
			List<Map<String, Object>> transactionsDetails = objectMapper.readValue(transactionsStr,
					new TypeReference<List<Map<String, Object>>>() {
					});
			transactionsDetails.stream().forEach(data -> {
				Map<String, Object> transactionsResponse = new HashMap<>();
				transactionsResponse.put("date", data.get("Transaction Date"));

				if (StringUtils.isEmpty(data.get("SumOfCredits").toString())) {
					data.put("SumOfCredits", 0);
				}
				if (StringUtils.isEmpty(data.get("SumOfDebits").toString())) {
					data.put("SumOfDebits", 0);
				}
				if (StringUtils.isEmpty(data.get("NetDisposableIncome").toString())) {
					data.put("NetDisposableIncome", 0);
				}
				if (StringUtils.isEmpty(data.get("BalanceIn3Months").toString())) {
					data.put("BalanceIn3Months", 0);
				}

				double monthlyTotal = Double.parseDouble(data.get("SumOfCredits").toString())
						- Double.parseDouble(data.get("SumOfDebits").toString());

				transactionsResponse.put("monthlyTotal", backendUtils.roundDoubleValueTo2Digits(monthlyTotal));
				transactionsResponse.put("month", data.get("Transaction Month"));
				transactionsResponse.put("yearMonth", data.get("Transaction YearMonth"));
				transactionsResponse.put("date", data.get("Transaction Date"));
				transactionsResponse.put("moneyIn", backendUtils.roundDoubleValueTo2Digits(Double.parseDouble(data.get("SumOfCredits").toString())));
				transactionsResponse.put("moneyOut", backendUtils.roundDoubleValueTo2Digits(Double.parseDouble(data.get("SumOfDebits").toString())));
				transactionsResponse.put("netDisposableIncome", backendUtils.roundDoubleValueTo2Digits(Double.parseDouble(data.get("NetDisposableIncome").toString())));
				transactionsResponse.put("balanceIn3Months", backendUtils.roundDoubleValueTo2Digits(Double.parseDouble(data.get("BalanceIn3Months").toString())));
				double totalBalance;
				if (StringUtils.isEmpty(data.get("TotalBalance").toString())) {
					 totalBalance = 0;
					transactionsResponse.put("totalBalance", totalBalance);
				} else {
					 totalBalance = Double.parseDouble(data.get("TotalBalance").toString());
				}

				double monthlyChange = (monthlyTotal/ totalBalance)* 100;

				transactionsResponse.put("monthlyChange", Double.toString(backendUtils.roundDoubleValueTo2Digits(monthlyChange)).concat("%"));
				transactionsResponse.put("cashOnHand", backendUtils.roundDoubleValueTo2Digits(Double.parseDouble(data.get("TotalBalance").toString())));
				transactionsResponse.put("closingBalance", backendUtils.roundDoubleValueTo2Digits(Double.parseDouble(data.get("TotalBalance").toString())));

				finalTransactionsResp.add(transactionsResponse);
			});
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : formatGet12MonthsTransactions" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while formatting the backend response Transactions Cash Flow");
		}

		return finalTransactionsResp;
	}
	
	private Map<String, Object> formatCashFlowDrivers(Map<String, Object> backendResponse)
			throws SBAException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> driverResponse = new HashMap<>();
		List<Map<String, Object>> positiveDrivers = new ArrayList<>();
		List<Map<String, Object>> negativeDrivers = new ArrayList<>();
		try {
			String cashFlowdrivers = objectMapper.writeValueAsString(backendResponse.get("drivers"));
			List<Map<String, Object>> driverDetails = objectMapper.readValue(cashFlowdrivers,
					new TypeReference<List<Map<String, Object>>>() {
					});
			driverDetails.stream().forEach(driver -> {
				Map<String, Object> response = new HashMap<>();
				response.put("driverRank", driver.get("DriverRank"));
				response.put("percentage", backendUtils.roundDoubleValueTo2Digits(Double.valueOf(driver.get("DriverWeight").toString()) * 100));
				String[] driverNameAndGrade = driver.get("Driver").toString().replaceAll("\\s", "").split("=");
				response.put("driverDetails", driverNameAndGrade[0]);
				response.put("driverValue", driverNameAndGrade[1]);
				response.put("grading", driver.get("ModelClass"));
				response.put("value", driver.get("price"));
				if (driver.get("ModelClass").toString().equalsIgnoreCase("Good")) {
					positiveDrivers.add(response);
				} else {
					negativeDrivers.add(response);
				}
			});
			driverResponse.put("positiveDrivers", positiveDrivers);
			driverResponse.put("negativeDrivers", negativeDrivers);
		} catch (Exception e) {
			alert.prepareError("Error in SmartBankingAdvisoryBusinessDelegateImpl : formatCashFlowDrivers"
					+ e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException("Failed while formatting CashFlow Drivers Response");
		}
		return driverResponse;
	}

	private List<Map<String, Object>> formatPredictionData(Map<String, Object> backendResponse) throws SBAException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, Object>> finalPredectiveResp = new ArrayList<>();

		try {
			String predictiveDataStr = objectMapper.writeValueAsString(backendResponse.get("value"));
			List<Map<String, Object>> predictiveDataDetails = objectMapper.readValue(predictiveDataStr,
					new TypeReference<List<Map<String, Object>>>() {
					});
			predictiveDataDetails.stream().forEach(predictiveData -> {
				Map<String, Object> predectiveResponse = new HashMap<>();
				if (predictiveData != null) {
					double netDisposableIncomeMonth1 = 0;
					double netDisposableIncomeMonth2 = 0;
					double netDisposableIncomeMonth3 = 0;
					if (predictiveData.containsKey("netDisposableIncomeMonth1")) {
						netDisposableIncomeMonth1 = Double.parseDouble(predictiveData.get("netDisposableIncomeMonth1").toString());
					}
					if (predictiveData.containsKey("netDisposableIncomeMonth2")) {
						netDisposableIncomeMonth2 = Double.parseDouble(predictiveData.get("netDisposableIncomeMonth2").toString());
					}
					if (predictiveData.containsKey("netDisposableIncomeMonth3")) {
						netDisposableIncomeMonth3 = Double.parseDouble(predictiveData.get("netDisposableIncomeMonth3").toString());
					}

					for (int index = 1; index < 4; index++) {
						predectiveResponse = new HashMap<>();
						predectiveResponse.put("month", index);
						double closingBalance = ((index == 1) ? netDisposableIncomeMonth1
								: ((index == 2) ? (netDisposableIncomeMonth1 + netDisposableIncomeMonth2) : (netDisposableIncomeMonth1 + netDisposableIncomeMonth2
												+ netDisposableIncomeMonth3)));
						double netDisposableIncome = (index == 1) ? netDisposableIncomeMonth1
								: (index == 2) ? netDisposableIncomeMonth2 : netDisposableIncomeMonth3;

						predectiveResponse.put("closingBalance", backendUtils.roundDoubleValueTo2Digits(closingBalance));
						predectiveResponse.put("netDisposableIncome", backendUtils.roundDoubleValueTo2Digits(netDisposableIncome));
						finalPredectiveResp.add(predectiveResponse);
					}
				}
			});
		} catch (Exception e) {
			alert.prepareError("Error in SmartBankingAdvisoryBusinessDelegateImpl : formatPredictionData" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while formatting the backend response of Future Predictions");
		}

		return finalPredectiveResp;
	}
	
	@Override
	public Map<String, Object> getSimulationDetails(Map<String, Object> payloadMap) throws SBAException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> payload = new HashMap<String, Object>();
		Map<String, Object> xaiPayload = new HashMap<String, Object>();
		try{
			xaiPayload.put("income", payloadMap.get("income"));
			xaiPayload.put("expense", payloadMap.get("expense"));
			Map<String, Object> backendResponse = SBABackendDelegate.getAnalyticsSimulationDetails(payloadMap);
			String analyticsDataStr = objectMapper.writeValueAsString(backendResponse.get("value"));
			List<Map<String, Object>> analyticsDataDetails = objectMapper.readValue(analyticsDataStr,
					new TypeReference<List<Map<String, Object>>>() {
					});
			analyticsDataDetails.stream().forEach(analyticsData -> {
			xaiPayload.putAll(analyticsData);
		}
		);
			Map<String, Object> backendResponseXai = SBABackendDelegate.getXaiSimulationDetails(xaiPayload);
			payload.put("responseList", backendResponseXai.get("responseList"));
		}
		catch (Exception e) {
			alert.prepareError("Error in SmartBankingAdvisoryBusinessDelegateImpl : getSimulationDetails" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException("Failed while getting Simulation Data");
		}
		return payload;
	}
	
	@Override
	public Map<String, Object> getSBAEnrolmentStatus(Map<String, Object> payloadMap, DataControllerRequest request) throws SBAException {
		return SBABackendDelegate.getSBAEnrolmentStatus(payloadMap, request);
	}
	
	@Override
	public Map<String, Object> updateSBAStatus(Map<String, Object> payloadMap, DataControllerRequest request) throws SBAException {
		return SBABackendDelegate.updateSBAStatus(payloadMap, request);
	}
	@Override
	public Dataset getAccountsReceivable(Map<String, Object> payloadMap) throws SBAException, NullPointerException {
		Result backendResponse = null;
		Dataset valueList = null;
		backendResponse = SBABackendDelegate.getAccountsReceivable(payloadMap);
		valueList = backendResponse.getDatasetById("value");
		if (checkValidReceivableResponse(valueList)) {
			for (@SuppressWarnings("unused")
			Record jsonObject : valueList.getAllRecords()) {
				double OverdueAmount = Double.parseDouble(jsonObject.getParamValueByName("summaryOverdueAmt"));
				double OverdueUpcomingAmount = OverdueAmount
						+ Double.parseDouble(jsonObject.getParamValueByName("summary0to10Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("summary11to30Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("summary31to60Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("summary60Days"));
				double OverdueAmountPercentage = (Double
						.parseDouble(jsonObject.getParamValueByName("overduePercent0to10Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("overduePercent11to30Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("overduePercent31to60Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("overduePercent60Days"))) * 100;
				int OverdueInvoices = Integer.parseInt(jsonObject.getParamValueByName("summaryOverdueInvoices"));
				double OverdueInvoicesPercentage = (Double
						.parseDouble(jsonObject.getParamValueByName("summaryOverdueAmtPercent"))) * 100;
				int TotalNoOfInvoices = Integer.parseInt(jsonObject.getParamValueByName("summaryOverdueInvoices"))+Integer
								.parseInt(jsonObject.getParamValueByName("summaryOverdueUpcomingInvoices"));
				jsonObject.addStringParam("OverdueAmount", String.format("%.2f",OverdueAmount));
				jsonObject.addStringParam("OverdueUpcomingAmount", String.format("%.2f",OverdueUpcomingAmount));
				jsonObject.addStringParam("OverdueAmountPercentage", String.format("%.2f",OverdueAmountPercentage));
				jsonObject.addStringParam("OverdueInvoices", Integer.toString(OverdueInvoices));
				jsonObject.addStringParam("OverdueInvoicesPercentage", String.format("%.2f",OverdueInvoicesPercentage));
				jsonObject.addStringParam("TotalNoOfInvoices", Integer.toString(TotalNoOfInvoices));
			}
		} else {
			throw new NullPointerException("Error in finding Keys");
		}
		return valueList;
	}

	private static boolean checkValidReceivableResponse(Dataset valueList) {
		int count = 0;
		for (Record jsonObject : valueList.getAllRecords()) {
			if (jsonObject.hasParamByName("summaryOverdueAmt") && jsonObject.hasParamByName("summary0to10Days")
					&& jsonObject.hasParamByName("summary11to30Days") && jsonObject.hasParamByName("summary31to60Days")
					&& jsonObject.hasParamByName("summary60Days")
					&& jsonObject.hasParamByName("overduePercent0to10Days")
					&& jsonObject.hasParamByName("overduePercent11to30Days")
					&& jsonObject.hasParamByName("overduePercent31to60Days")
					&& jsonObject.hasParamByName("overduePercent60Days")
					&& jsonObject.hasParamByName("summaryOverdueInvoices")
					&& jsonObject.hasParamByName("summaryOverdueAmtPercent")
					&& jsonObject.hasParamByName("summaryOverdueUpcomingInvoices")) {
				count++;
			}
		}
		if (valueList.getAllRecords().size() == count) {
			return true;
		}
		return false;
	}
	
	@Override
	public Dataset getAccountsPayable(Map<String, Object> payloadMap) throws SBAException, NullPointerException {
		Result backendResponse = null;
		Dataset valueList = null;
		backendResponse = SBABackendDelegate.getAccountsPayable(payloadMap);
		valueList = backendResponse.getDatasetById("value");
		if (checkValidPayableResponse(valueList)) {
			for (@SuppressWarnings("unused")
			Record jsonObject : valueList.getAllRecords()) {
				double OverdueAmount = Double.parseDouble(jsonObject.getParamValueByName("summaryOverdueAmt"));
				double OverdueUpcomingAmount = OverdueAmount
						+ Double.parseDouble(jsonObject.getParamValueByName("summary0to10Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("summary11to30Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("summary31to60Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("summary60Days"));
				double OverdueAmountPercentage = (Double
						.parseDouble(jsonObject.getParamValueByName("overduePercent0to10Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("overduePercent11to30Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("overduePercent31to60Days"))
						+ Double.parseDouble(jsonObject.getParamValueByName("overduePercent60Days"))) * 100;
				int TotalNoOfBills = Integer.parseInt(jsonObject.getParamValueByName("CountOfOverdueBills"))
						+ Integer.parseInt(jsonObject.getParamValueByName("summaryOverdueUpcomingBills"));
				int OverdueBills = (Integer.parseInt(jsonObject.getParamValueByName("CountOfOverdueBills")));
				double OverdueBillsPercentage = (Double
						.parseDouble(jsonObject.getParamValueByName("summaryOverdueAmtPercent"))) * 100;
				jsonObject.addStringParam("OverdueAmount", String.format("%.2f", OverdueAmount));
				jsonObject.addStringParam("OverdueUpcomingAmount", String.format("%.2f", OverdueUpcomingAmount));
				jsonObject.addStringParam("OverdueAmountPercentage", String.format("%.2f", OverdueAmountPercentage));
				jsonObject.addStringParam("TotalNoOfBills", Integer.toString(TotalNoOfBills));
				jsonObject.addStringParam("OverdueBills", Integer.toString(OverdueBills));
				jsonObject.addStringParam("OverdueBillsPercentage", String.format("%.2f", OverdueBillsPercentage));
			}
		} else {
			throw new NullPointerException("Error in finding Keys");
		}
		return valueList;
	}
	
	private static boolean checkValidPayableResponse(Dataset valueList) {
		int count = 0;
		for (Record jsonObject : valueList.getAllRecords()) {
			if (jsonObject.hasParamByName("summaryOverdueAmt") && jsonObject.hasParamByName("summary0to10Days")
					&& jsonObject.hasParamByName("summary11to30Days") && jsonObject.hasParamByName("summary31to60Days")
					&& jsonObject.hasParamByName("summary60Days")
					&& jsonObject.hasParamByName("overduePercent0to10Days")
					&& jsonObject.hasParamByName("overduePercent11to30Days")
					&& jsonObject.hasParamByName("overduePercent31to60Days")
					&& jsonObject.hasParamByName("overduePercent60Days")
					&& jsonObject.hasParamByName("CountOfOverdueBills")
					&& jsonObject.hasParamByName("summaryOverdueAmtPercent")
					&& jsonObject.hasParamByName("summaryOverdueUpcomingBills")) {
				count++;
			}
		}
		if (valueList.getAllRecords().size() == count) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("null")
	@Override
	public Dataset getReceivablesDebtorDaysReq(Map<String, Object> payloadMap)
			throws SBAException, NullPointerException {
		String values = "value";
		Result backendResponse = null;
		Dataset valueList = null;
		Dataset valueList1 = new Dataset();
		backendResponse = SBABackendDelegate.getReceivablesDebtorDaysReq(payloadMap);
		valueList = backendResponse.getDatasetById("value");
		for (Record jsonObject : valueList.getAllRecords()) {
			if (Integer.parseInt(jsonObject.getParamValueByName("rank")) <= 5) {
				valueList1.addRecord(jsonObject);
			}
		}
		valueList1.setId(values);
		return valueList1;
	}
	
	@Override
	public Map<String, Map<String, Object>> getReceivablesSummary(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> payload = new HashMap<String, Object>();
		Map<String, Map<String, Object>> updatedCustomerData = new LinkedHashMap<>();
		ObjectMapper objMapper = new ObjectMapper();
		try {
			int customerIndex = 0;
			Map<String, Object> backendResponse = SBABackendDelegate.getAccountsReceivableByCustomer(payloadMap);
			List<Map<String, Object>> valueList = (List<Map<String, Object>>) backendResponse.get("value");
			Result backendResponseAvgDays = SBABackendDelegate.getReceivablesDebtorDaysReq(payloadMap);
			Map<String, Object> responseMapAvgDays = objMapper.readValue(ResultToJSON.convert(backendResponseAvgDays),
					Map.class);
			List<Map<String, Object>> valueListAvgDays = (List<Map<String, Object>>) responseMapAvgDays.get("value");
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueList) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("customerName", (String) (customerData.get("customerName")));
				customerDataMap.put("sumOfOverdueInvoices0to10",
						formatter.format(Double.valueOf(customerData.get("sumOfOverdueInvoices0to10").toString())));
				customerDataMap.put("countOfOverdueInvoices0to10",
						Double.valueOf(customerData.get("countOfOverdueInvoices0to10").toString()));
				customerDataMap.put("sumOfOverdueInvoices11to30",
						formatter.format(Double.valueOf(customerData.get("sumOfOverdueInvoices11to30").toString())));
				customerDataMap.put("countOfOverdueInvoices11to30",
						Double.valueOf(customerData.get("countOfOverdueInvoices11to30").toString()));
				customerDataMap.put("sumOfOverdueInvoices31to60",
						formatter.format(Double.valueOf(customerData.get("sumOfOverdueInvoices31to60").toString())));
				customerDataMap.put("countOfOverdueInvoices31to60",
						Double.valueOf(customerData.get("countOfOverdueInvoices31to60").toString()));
				customerDataMap.put("sumOfOverdueInvoicesBeyond60",
						formatter.format(Double.valueOf(customerData.get("sumOfOverdueInvoicesBeyond60").toString())));
				customerDataMap.put("countOfOverdueInvoicesBeyond60",
						Double.valueOf(customerData.get("countOfOverdueInvoicesBeyond60").toString()));
				double TotalOverdueAmount = (Double.valueOf(customerData.get("sumOfOverdueInvoices0to10").toString())
						+ Double.valueOf(customerData.get("sumOfOverdueInvoices11to30").toString())
						+ Double.valueOf(customerData.get("sumOfOverdueInvoices31to60").toString())
						+ Double.valueOf(customerData.get("sumOfOverdueInvoicesBeyond60").toString()));
				double TotalOverdueNoInvoices = (Double
						.valueOf(customerData.get("countOfOverdueInvoices0to10").toString())
						+ Double.valueOf(customerData.get("countOfOverdueInvoices11to30").toString())
						+ Double.valueOf(customerData.get("countOfOverdueInvoices31to60").toString())
						+ Double.valueOf(customerData.get("countOfOverdueInvoicesBeyond60").toString()));
				customerDataMap.put("TotalOverdueAmount", formatter.format(TotalOverdueAmount));
				customerDataMap.put("TotalOverdueNoInvoices", TotalOverdueNoInvoices);
				customerDataMap.put("sumCount0to10Days",
						formatter.format(Double.valueOf(customerData.get("sumCount0to10Days").toString())));
				customerDataMap.put("invoiceCount0to10Days",
						Double.valueOf(customerData.get("invoiceCount0to10Days").toString()));
				customerDataMap.put("sumCount11to30Days",
						formatter.format(Double.valueOf(customerData.get("sumCount11to30Days").toString())));
				customerDataMap.put("invoiceCount11to30Days",
						Double.valueOf(customerData.get("invoiceCount11to30Days").toString()));
				customerDataMap.put("sumCount31to60Days",
						formatter.format(Double.valueOf(customerData.get("sumCount31to60Days").toString())));
				customerDataMap.put("invoiceCount31to60Days",
						Double.valueOf(customerData.get("invoiceCount31to60Days").toString()));
				customerDataMap.put("sumCountBeyond60Days",
						formatter.format(Double.valueOf(customerData.get("sumCountBeyond60Days").toString())));
				customerDataMap.put("invoiceCountBeyond60Days",
						Double.valueOf(customerData.get("invoiceCountBeyond60Days").toString()));
				double TotalUpcomingAmount = (Double.valueOf(customerData.get("sumOfOverdueInvoices0to10").toString())
						+ Double.valueOf(customerData.get("sumOfOverdueInvoices11to30").toString())
						+ Double.valueOf(customerData.get("sumOfOverdueInvoices31to60").toString())
						+ Double.valueOf(customerData.get("sumOfOverdueInvoicesBeyond60").toString()));
				double TotalUpcomingNoInvoices = (Double
						.valueOf(customerData.get("countOfOverdueInvoices0to10").toString())
						+ Double.valueOf(customerData.get("countOfOverdueInvoices11to30").toString())
						+ Double.valueOf(customerData.get("countOfOverdueInvoices31to60").toString())
						+ Double.valueOf(customerData.get("countOfOverdueInvoicesBeyond60").toString()));
				customerDataMap.put("TotalUpcomingAmount", formatter.format(TotalUpcomingAmount));
				customerDataMap.put("TotalUpcomingNoInvoices", TotalUpcomingNoInvoices);
				updatedCustomerData.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			Map<String, Object> customerDataMapResp = new LinkedHashMap<>();
			customerIndex = 0;
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueListAvgDays) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("sumOfOverdueInvoices0to10",
						Double.valueOf(customerData.get("avgDaystoPay").toString()));
				customerDataMapResp.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			updatedCustomerData.put("Avg", customerDataMapResp);
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getReceivablesSummary" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for Receivables Summary");
		}
		return updatedCustomerData;
	}
	
	@Override
	public Map<String, Map<String, Object>> getPayableSummary(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> payload = new HashMap<String, Object>();
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, Map<String, Object>> updatedCustomerData = new LinkedHashMap<>();
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		try {
			int customerIndex = 0;
			Map<String, Object> backendResponse = SBABackendDelegate.getPayablesSummaryBySupplier(payloadMap);
			List<Map<String, Object>> valueList = (List<Map<String, Object>>) backendResponse.get("value");
			Result backendResponseAvgDays = SBABackendDelegate.getPayablesDebtorDaysReq(payloadMap);
			Map<String, Object> responseMapAvgDays = objMapper.readValue(ResultToJSON.convert(backendResponseAvgDays),
					Map.class);
			List<Map<String, Object>> valueListAvgDays = (List<Map<String, Object>>) responseMapAvgDays.get("value");
			
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueList) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("customerName", (String) (customerData.get("customerName")));
				customerDataMap.put("sumOfOverdueBills0to10",
						formatter.format(Double.valueOf(customerData.get("sumOfOverdueBills0to10").toString())));
				customerDataMap.put("countOfOverdueBills0to10",
						Double.valueOf(customerData.get("countOfOverdueBills0to10").toString()));
				customerDataMap.put("sumOfOverdueBills11to30",
						formatter.format(Double.valueOf(customerData.get("sumOfOverdueBills11to30").toString())));
				customerDataMap.put("countOfOverdueBills11to30",
						Double.valueOf(customerData.get("countOfOverdueBills11to30").toString()));
				customerDataMap.put("sumOfOverdueBills31to60",
						formatter.format(Double.valueOf(customerData.get("sumOfOverdueBills31to60").toString())));
				customerDataMap.put("countOfOverdueBills31to60",
						Double.valueOf(customerData.get("countOfOverdueBills31to60").toString()));
				customerDataMap.put("sumOfOverdueBillsBeyond60",
						formatter.format(Double.valueOf(customerData.get("sumOfOverdueBillsBeyond60").toString())));
				customerDataMap.put("countOfOverdueBillsBeyond60",
						Double.valueOf(customerData.get("countOfOverdueBillsBeyond60").toString()));
				customerDataMap.put("TotalOverdueAmount",
						formatter.format(Double.valueOf(customerData.get("sumOfAllBillsDue").toString())));
				customerDataMap.put("TotalOverdueNoBills",
						Double.valueOf(customerData.get("countOfOverdueBills").toString()));
				customerDataMap.put("billsSum0to10Days",
						formatter.format(Double.valueOf(customerData.get("billsSum0to10Days").toString())));
				customerDataMap.put("billsCount0to10Days",
						Double.valueOf(customerData.get("billsCount0to10Days").toString()));
				customerDataMap.put("billsSum11to30Days",
						formatter.format(Double.valueOf(customerData.get("billsSum11to30Days").toString())));
				customerDataMap.put("billsCount11to30Days",
						Double.valueOf(customerData.get("billsCount11to30Days").toString()));
				customerDataMap.put("billsSum31to60Days",
						formatter.format(Double.valueOf(customerData.get("billsSum31to60Days").toString())));
				customerDataMap.put("billsCount31to60Days",
						Double.valueOf(customerData.get("billsCount31to60Days").toString()));
				customerDataMap.put("billsSumBeyond60Days",
						formatter.format(Double.valueOf(customerData.get("billsSumBeyond60Days").toString())));
				customerDataMap.put("billsCountBeyond60Days",
						Double.valueOf(customerData.get("billsCountBeyond60Days").toString()));
				customerDataMap.put("TotalUpcomingAmount",
						formatter.format(Double.valueOf(customerData.get("sumOfAllBillsDue").toString())));
				customerDataMap.put("TotalUpcomingNoBills",
						Double.valueOf(customerData.get("billsCountDue").toString()));
				updatedCustomerData.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			Map<String, Object> customerDataMapResp = new LinkedHashMap<>();
			customerIndex = 0;
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueListAvgDays) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("sumOfOverdueInvoices0to10",
						Double.valueOf(customerData.get("avgDaysToPay").toString()));
				customerDataMapResp.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			updatedCustomerData.put("Avg", customerDataMapResp);
		} catch (Exception e) {
			alert.prepareError("Error in SmartBankingAdvisoryBusinessDelegateImpl : getPayableSummary" + e.getMessage())
					.log();
			CommonUtils
					.constructAndThrowMiddlewareException("Failed while fetching backend response for Payable Summary");
		}
		return updatedCustomerData;
	}
	
	@SuppressWarnings("null")
	@Override
	public Dataset getPayablesDebtorDaysReq(Map<String, Object> payloadMap)
			throws SBAException, NullPointerException {
		String values = "value";
		Result backendResponse = null;
		Dataset valueList = null;
		Dataset valueList1 = new Dataset();
		backendResponse = SBABackendDelegate.getPayablesDebtorDaysReq(payloadMap);
		valueList = backendResponse.getDatasetById("value");
		for (Record jsonObject : valueList.getAllRecords()) {
			if (Integer.parseInt(jsonObject.getParamValueByName("rank")) <= 5) {
				valueList1.addRecord(jsonObject);
			}
		}
		valueList1.setId(values);
		return valueList1;
	}
	
	@Override
	public Map<String, Object> getAccountsReceivableByCustomer(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> backendResponse = new HashMap<>();
		try {
			backendResponse = SBABackendDelegate.getAccountsReceivableByCustomer(payloadMap);
		} catch (Exception e) {
			alert.prepareError("Error in SmartBankingAdvisoryBusinessDelegateImpl : getAccountsReceivableByCustomer"
					+ e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for Receivable By customer Details");
		}
		return backendResponse;
	}
	
	@Override
	public Map<String, Object> getReceivableOverdueExcel(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> updatedCustomerData = new HashMap<String, Object>();
		int customerIndex = 0;
		String key = "overdueAmt";
		try {
			Map<String, Object> backendResponse = SBABackendDelegate.getAccountsReceivableByCustomer(payloadMap);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> valueList = (List<Map<String, Object>>) backendResponse.get("value");
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueList) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("customerName", (String) (customerData.get("customerName")));
				customerDataMap.put("empty", "");
				customerDataMap.put("countOfOverdueInvoices", (String) (customerData.get("countOfOverdueInvoices")));
				customerDataMap.put("overdueAmt", (Double.valueOf((customerData.get("overdueAmt").toString()))));
				updatedCustomerData.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			updatedCustomerData = sortMapByAmt(updatedCustomerData, key);
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getReceivableOverdueExcel" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for Receivables Overdue");
		}
		return updatedCustomerData;
	}
	
	private static Map<String, Object> sortMapByAmt(Map<String, Object> unsortedMap, String param) {
		List<Map.Entry<String, Object>> entryList = new ArrayList<>(unsortedMap.entrySet());
		Collections.sort(entryList, (entry1, entry2) -> {
			@SuppressWarnings("unchecked")
			Map<String, Object> customerDataMap1 = (Map<String, Object>) entry1.getValue();
			@SuppressWarnings("unchecked")
			Map<String, Object> customerDataMap2 = (Map<String, Object>) entry2.getValue();
			String overdueAmt1 = (String) customerDataMap1.get(param).toString();
			String overdueAmt2 = (String) customerDataMap2.get(param).toString();
			return Double.compare(Double.parseDouble(overdueAmt2), Double.parseDouble(overdueAmt1));
		});
		Map<String, Object> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<String, Object> entry : entryList) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	@Override
	public Map<String, Object> getReceivableUpcomingExcel(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> updatedCustomerData = new HashMap<String, Object>();
		int customerIndex = 0;
		String key = "totDueAmt";
		try {
			Map<String, Object> backendResponse = SBABackendDelegate.getAccountsReceivableByCustomer(payloadMap);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> valueList = (List<Map<String, Object>>) backendResponse.get("value");
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueList) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("customerName", (String) (customerData.get("customerName")));
				customerDataMap.put("empty", "");
				customerDataMap.put("CountOfOutstandingInvoices",
						(String) (customerData.get("CountOfOutstandingInvoices")));
				customerDataMap.put("totDueAmt", (Double.valueOf((customerData.get("totDueAmt").toString()))));
				updatedCustomerData.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			updatedCustomerData = sortMapByAmt(updatedCustomerData, key);
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getReceivableUpcomingExcel" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for Receivables Upcoming");
		}
		return updatedCustomerData;
	}
	
	@Override
	public Map<String, Object> generateByCustomerExcelList(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> updatedCustomerData = new HashMap<String, Object>();
		int customerIndex = 0;
		String key = "TotalAmount";
		double TotalAmount;
		try {
			Map<String, Object> backendResponse = SBABackendDelegate.getAccountsReceivableByCustomer(payloadMap);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> valueList = (List<Map<String, Object>>) backendResponse.get("value");
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueList) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("customerName", (String) (customerData.get("customerName")));
				customerDataMap.put("empty", "");
				customerDataMap.put("countOfOverdueInvoices", (String) (customerData.get("countOfOverdueInvoices")));
				customerDataMap.put("overdueAmt", (Double.valueOf((customerData.get("overdueAmt").toString()))));
				customerDataMap.put("emptyvalue", "");
				customerDataMap.put("CountOfOutstandingInvoices",
						(String) (customerData.get("CountOfOutstandingInvoices")));
				customerDataMap.put("totDueAmt", (Double.valueOf((customerData.get("totDueAmt").toString()))));
				customerDataMap.put("emptyvalues", "");
				TotalAmount = (Double.valueOf(customerData.get("overdueAmt").toString()) 
						+ Double.valueOf(customerData.get("totDueAmt").toString()));
				customerDataMap.put("TotalAmount", TotalAmount);
				updatedCustomerData.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			updatedCustomerData = sortMapByAmt(updatedCustomerData, key);
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : generateByCustomerExcelList" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for Receivables ByCustomer");
		}
		return updatedCustomerData;
	}
	
	@Override
	public Map<String, Object> getAccountsPayableBySupplier(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> backendResponse = new HashMap<>();
		try {
			backendResponse = SBABackendDelegate.getPayablesSummaryBySupplier(payloadMap);
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getAccountsPayableBySupplier" + e.getMessage())
					.log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for Payable By Supplier Details");
		}
		return backendResponse;
	}

	@Override
	public Map<String, Object> getPayableOverdueExcel(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> updatedCustomerData = new HashMap<String, Object>();
		int customerIndex = 0;
		String key = "overdueAmt";
		try {
			Map<String, Object> backendResponse = SBABackendDelegate.getPayablesSummaryBySupplier(payloadMap);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> valueList = (List<Map<String, Object>>) backendResponse.get("value");
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueList) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("customerName", (String) (customerData.get("customerName")));
				customerDataMap.put("empty", "");
				customerDataMap.put("countOfOverdueBills", (String) (customerData.get("countOfOverdueBills")));
				customerDataMap.put("overdueAmt", (Double.valueOf((customerData.get("overdueAmt").toString()))));
				updatedCustomerData.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			updatedCustomerData = sortMapByAmt(updatedCustomerData, key);
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getPayableOverdueExcel" + e.getMessage())
					.log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for Payables Overdue");
		}
		return updatedCustomerData;
	}

	@Override
	public Map<String, Object> getPayableUpcomingExcel(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> updatedCustomerData = new HashMap<String, Object>();
		int customerIndex = 0;
		String key = "totDueAmt";
		try {
			Map<String, Object> backendResponse = SBABackendDelegate.getPayablesSummaryBySupplier(payloadMap);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> valueList = (List<Map<String, Object>>) backendResponse.get("value");
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueList) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("customerName", (String) (customerData.get("customerName")));
				customerDataMap.put("empty", "");
				customerDataMap.put("CountOfOutstandingBills", (String) (customerData.get("CountOfOutstandingBills")));
				customerDataMap.put("totDueAmt", (Double.valueOf((customerData.get("totDueAmt").toString()))));
				updatedCustomerData.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			updatedCustomerData = sortMapByAmt(updatedCustomerData, key);
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getPayableUpcomingExcel" + e.getMessage())
					.log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for Payables Upcoming");
		}
		return updatedCustomerData;
	}

	@Override
	public Map<String, Object> generateBySupplierExcelList(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> updatedCustomerData = new HashMap<String, Object>();
		int customerIndex = 0;
		String key = "TotalAmount";
		double TotalAmount;
		try {
			Map<String, Object> backendResponse = SBABackendDelegate.getPayablesSummaryBySupplier(payloadMap);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> valueList = (List<Map<String, Object>>) backendResponse.get("value");
			for (@SuppressWarnings("unused")
			Map<String, Object> customerData : valueList) {
				Map<String, Object> customerDataMap = new LinkedHashMap<>();
				customerDataMap.put("customerName", (String) (customerData.get("customerName")));
				customerDataMap.put("empty", "");
				customerDataMap.put("countOfOverdueBills", (String) (customerData.get("countOfOverdueBills")));
				customerDataMap.put("overdueAmt", (Double.valueOf((customerData.get("overdueAmt").toString()))));
				customerDataMap.put("emptyvalue", "");
				customerDataMap.put("CountOfOutstandingBills", (String) (customerData.get("CountOfOutstandingBills")));
				customerDataMap.put("totDueAmt", (Double.valueOf((customerData.get("totDueAmt").toString()))));
				customerDataMap.put("emptyvalues", "");
				TotalAmount = (Double.valueOf(customerData.get("overdueAmt").toString())
						+ Double.valueOf(customerData.get("totDueAmt").toString()));
				customerDataMap.put("TotalAmount", TotalAmount);
				updatedCustomerData.put(String.valueOf(customerIndex), customerDataMap);
				customerIndex++;
			}
			updatedCustomerData = sortMapByAmt(updatedCustomerData, key);
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : generateBySupplierExcelList" + e.getMessage())
					.log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for Payables BySupplier");
		}
		return updatedCustomerData;
	}
	
	@SuppressWarnings("null")
	@Override
	public Map<String, Object> getSBAFeaturesActions(Map<String, Object> payloadMap) throws SBAException {
		Map<String, Object> backendResponse = new HashMap<>();
		Map<String, Object> analyticsStatusResponse = new HashMap<>();
		Result analyticsStatusResult = new Result();
		List<Map<String, Object>> finalResponse = new ArrayList<>();
		List<Map<String, Object>> updatedFinalResponse = new ArrayList<>();
		try {
			Result backendResult = null;
			payloadMap.put("_userId", payloadMap.get("userId"));
			payloadMap.put("_legalEntityId", payloadMap.get("legalEntityId"));
			List<String> subscribersList = new ArrayList<>();

			backendResult = SBABackendDelegate.getSBAFeaturesActions(payloadMap);
			Dataset customerDs = backendResult.getDatasetById("records1");
			Dataset featuresDs = backendResult.getDatasetById("records");
			for (Record jsonObject : customerDs.getAllRecords()) {
				Map<String, Object> resultResponse = new HashMap<>();
				resultResponse.put("Customer_id", jsonObject.getParamValueByName("Customer_id"));
				resultResponse.put("BackendId", jsonObject.getParamValueByName("BackendId"));
				List<String> featuresList = new ArrayList<>();
				List<String> permissionsList = new ArrayList<>();

				for (Record featureRecord : featuresDs.getAllRecords()) {
					if (StringUtils.equals(jsonObject.getParamValueByName("BackendId").toString(),
							featureRecord.getParamValueByName("coreCustomerId".toString()))) {
						if (StringUtils.isNotEmpty(featureRecord.getParamValueByName("featureId").toString())
								&& StringUtils.isNotEmpty(featureRecord.getParamValueByName("Action_id").toString())) {
							featuresList.add(featureRecord.getParamValueByName("featureId").toString());
							permissionsList.add(featureRecord.getParamValueByName("Action_id").toString());

							if (jsonObject.hasParamByName("sbaEnrolmentStatus")) {
								resultResponse.put("sbaEnrolmentStatus",
										jsonObject.getParamValueByName("sbaEnrolmentStatus"));
							}
						}
					}
					resultResponse.put("features", featuresList);
					resultResponse.put("permissions", permissionsList);

				}
				if (!featuresList.isEmpty()) {
					if (containsThreeCharacters("SBA_", featuresList)) {
						subscribersList.add(resultResponse.get("BackendId").toString());
					}

				}
				payloadMap.put("Subscriber", subscribersList);
				finalResponse.add(resultResponse);
			}
			if (payloadMap.containsKey("Subscriber")) {
				analyticsStatusResponse = SBABackendDelegate.getEnrollmentStatus(payloadMap);
				if (analyticsStatusResponse.containsKey("isLive")) {
					analyticsStatusResult = JSONToResult.convert(JSONUtils.stringify(analyticsStatusResponse));
					Dataset subscribersDS = analyticsStatusResult.getDatasetById("value");
					for (Record EnrolRecord : subscribersDS.getAllRecords()) {
						Map<String, Object> subscriberEnrolmentStatus = new HashMap<>();
						for (int i = 0; i < subscribersList.size(); i++) {
							subscriberEnrolmentStatus.put("EnrollmentState",
									EnrolRecord.getParamValueByName("EnrollmentState"));
							subscriberEnrolmentStatus.put("EventDateTime",
									EnrolRecord.getParamValueByName("EventDateTime"));
							subscriberEnrolmentStatus.put("EventMinutesAgo",
									EnrolRecord.getParamValueByName("EventMinutesAgo"));
							subscriberEnrolmentStatus.put("Subscriber", EnrolRecord.getParamValueByName("Subscriber"));
						}

						finalResponse.add(subscriberEnrolmentStatus);
					}
					for (int i = 0; i < finalResponse.size(); i++) {

						Map<String, Object> resultFinalResponse = finalResponse.get(i);
						if (resultFinalResponse.containsKey("BackendId")) {
							for (int j = 1; j < finalResponse.size(); j++) {
								Map<String, Object> resultFinalResponse1 = finalResponse.get(j);
								if (resultFinalResponse1.containsKey("Subscriber")) {
									resultFinalResponse.put("enrolmentStatus", resultFinalResponse1);
								}
							}
						
						updatedFinalResponse.add(resultFinalResponse);
						}
					}
				} else {
					analyticsStatusResult = JSONToResult.convert(JSONUtils.stringify(analyticsStatusResponse));
					Dataset subscribersDS = analyticsStatusResult.getDatasetById("value");
					for (Record EnrolRecord : subscribersDS.getAllRecords()) {
						Map<String, Object> subscriberEnrolmentStatus = new HashMap<>();
						for (int i = 0; i < subscribersList.size(); i++) {
							if (StringUtils.equals(EnrolRecord.getParamValueByName("Subscriber").toString(),
									subscribersList.get(i).toString())) {
								subscriberEnrolmentStatus.put("EnrollmentState",
										EnrolRecord.getParamValueByName("EnrollmentState"));
								subscriberEnrolmentStatus.put("EventDateTime",
										EnrolRecord.getParamValueByName("EventDateTime"));
								subscriberEnrolmentStatus.put("EventMinutesAgo",
										EnrolRecord.getParamValueByName("EventMinutesAgo"));
								subscriberEnrolmentStatus.put("Subscriber",
										EnrolRecord.getParamValueByName("Subscriber"));
							}
						}

						finalResponse.add(subscriberEnrolmentStatus);
					}
				
				for (int i = 0; i < finalResponse.size(); i++) {

					Map<String, Object> resultFinalResponse = finalResponse.get(i);
					if (resultFinalResponse.containsKey("BackendId")) {
						for (int j = 1; j < finalResponse.size(); j++) {
							Map<String, Object> resultFinalResponse1 = finalResponse.get(j);
							if (resultFinalResponse1.containsKey("Subscriber")) {
								if (StringUtils.equals(resultFinalResponse.get("BackendId").toString(),
										resultFinalResponse1.get("Subscriber").toString())) {
									resultFinalResponse.put("enrolmentStatus", resultFinalResponse1);
								}
							}
						}
						updatedFinalResponse.add(resultFinalResponse);
					}
				}
				}
			}
			backendResponse.put("Response", updatedFinalResponse);
		} catch (Exception e) {
			alert.prepareError(
					"Error in SmartBankingAdvisoryBusinessDelegateImpl : getSBAFeaturesActions" + e.getMessage()).log();
			CommonUtils.constructAndThrowMiddlewareException(
					"Failed while fetching backend response for getSBAFeaturesActions");
		}
		return backendResponse;
	}
	private static boolean containsThreeCharacters(String inputString, List<String> stringList) {
        for (String substring : stringList) {
            if (inputString.contains(substring.substring(0, 3))) {
                return true;
            }
        }
        return false;
    }
}