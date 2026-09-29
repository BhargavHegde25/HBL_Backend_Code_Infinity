/**
 * 
 */
package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.io.IOException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealth.backenddelegate.api.DownloadAttachmentsPDFBackendDelegate;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PDFGenerator;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealth.common.util.UserAttributesUtil;
import com.temenos.infinity.api.wealthservices.util.PDFGenerator.accountDetails;
import com.temenos.infinity.api.wealthservices.util.PDFGenerator.customerDetails;
import com.temenos.infinity.api.wealthservices.util.CSVGenerator;
import com.temenos.infinity.api.wealthservices.util.ExcelGenerator;

/**
 * @author himaja.sridhar
 *
 */
public class DownloadAttachmentsPDFBackendDelegateImpl implements DownloadAttachmentsPDFBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	String coreValue = "";
	int columnCount;
	LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> currency = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> checkCurr = new LinkedHashMap<String, String>();
	WealthDashboardBackendDelegateImpl wealthDashboardBackendDelegateImpl = new WealthDashboardBackendDelegateImpl();

	@SuppressWarnings("unchecked")
	@Override
	public byte[] generatePDF(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object portfolioIdobj = inputParams.get(TemenosConstants.PORTFOLIOID);
		Object navPageObj = inputParams.get(TemenosConstants.NAVPAGE);
		Object accountIdObj = inputParams.get(TemenosConstants.ACCID);
		Object currencyIdObj = inputParams.get(TemenosConstants.CURRENCYID);
		Object isEurObj = inputParams.get(TemenosConstants.ISEURO);
		Object formatObj = inputParams.get(TemenosConstants.DOWNLOADFORMAT);
		String isIncludeOrders = request.getParameter(TemenosConstants.ISINCLUEORDERS);
		String portfolioId = null;
		String navPage = null;
		String accountId = null;
		String currencyId = null;
		String isEur = null;
		String downloadFormat = null;
		if (((portfolioIdobj == null || portfolioIdobj.equals(""))
				&& (!navPageObj.toString().equalsIgnoreCase("Watchlist"))) || navPageObj == null
				|| navPageObj.equals("") || formatObj == null || formatObj.equals("") ) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", "Invalid Input Params!");
			diagnostic.prepareDebug(result.toString()).log();
			// return Utilities.constructResultFromJSONObject(result);
		} else {
			if (portfolioIdobj != null) {
				portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
				inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
			}
			if (navPageObj != null) {
				navPage = inputParams.get(TemenosConstants.NAVPAGE).toString();
				inputMap.put(TemenosConstants.NAVPAGE, navPage);
			}
			if (accountIdObj != null) {
				accountId = inputParams.get(TemenosConstants.ACCID).toString();
				inputMap.put(TemenosConstants.ACCID, accountId);
			} else {
				accountId = "";
				inputMap.put(TemenosConstants.ACCID, accountId);
			}
			if (currencyIdObj != null) {
				currencyId = inputParams.get(TemenosConstants.CURRENCYID).toString();
				inputMap.put(TemenosConstants.CURRENCYID, currencyId);
			} else {
				currencyId = "";
				inputMap.put(TemenosConstants.CURRENCYID, currencyId);
			}
			if (isEurObj != null) {
				isEur = inputParams.get(TemenosConstants.ISEURO).toString();
				if (isEur.equals("true")) {
					inputMap.put(TemenosConstants.ISEURO, isEur);
				} else {
					isEur = "";
					inputMap.put(TemenosConstants.ISEURO, isEur);
				}
			} else {
				isEur = "";
				inputMap.put(TemenosConstants.ISEURO, isEur);
			}
			if (formatObj != null) {
				downloadFormat = inputParams.get(TemenosConstants.DOWNLOADFORMAT).toString();
				inputMap.put(TemenosConstants.DOWNLOADFORMAT, downloadFormat);
			}
		}

		// return Utilities.constructResultFromJSONObject(jsonResult);

		PDFGenerator pdfGenerator = new PDFGenerator();
		ExcelGenerator excelGenerator = new ExcelGenerator();
		CSVGenerator csvGenerator = new CSVGenerator();
		byte[] bytes = null;
		try {
			String fName=null;
			try {
				fName = UserAttributesUtil.getCustomerFirstName(request);
			} catch (Exception e) {
				alert.prepareError("Error while fetching firstName - " + e.getMessage()).log();
			}
			String FirstName = fName;
			//String FirstName = PortfolioWealthUtils.getUserAttributeFromIdentity(request, "FirstName").concat(" ");
			//String lastName = PortfolioWealthUtils.getUserAttributeFromIdentity(request, "LastName");
			String lName=null;
			try {
				lName = UserAttributesUtil.getCustomerLastName(request);
			} catch (Exception e) {
				alert.prepareError("Error while fetching LastName - " + e.getMessage()).log();
			}
			String lastName = lName;
			
			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

			if (allportfoliosList.contains(portfolioId)) {
				JSONArray jsonResult = getResultObject(methodID, inputArray, request, response, headersMap, navPage,
						portfolioId, isEur, isIncludeOrders);
				JSONArray resultArr = jsonResult;
				if(downloadFormat.equalsIgnoreCase("pdf")) {
					bytes = pdfGenerator.generateFile(new customerDetails(FirstName.concat(lastName), portfolioId),
							new accountDetails(accountId, currencyId), resultArr, columnCount, fields, navPage, isEur, isIncludeOrders);
						
				}
				else if (downloadFormat.equalsIgnoreCase("xlsx")) {
					bytes = excelGenerator.generateFile(new customerDetails(FirstName.concat(lastName), portfolioId),
							new accountDetails(accountId, currencyId), resultArr, columnCount, fields, navPage, isEur);
				}
				else if (downloadFormat.equalsIgnoreCase("csv")) {
					bytes = csvGenerator.generateFile(new customerDetails(FirstName.concat(lastName), portfolioId),
							new accountDetails(accountId, currencyId), resultArr, columnCount, fields, navPage, isEur);
				}
				return bytes;
			} else {
				String errStr = "Portfolio ID  does not exist for the Customer";
				alert.prepareError(errStr).log();
				return errStr.getBytes();

			}

		} catch (IOException e) {
			alert.prepareError("Error while invoking Transact - "
					+ PortfolioWealthAPIServices.WEALTH_GENERATEPDF.getOperationName() + "  : " + e).log();
			return null;
		}

	}

	@SuppressWarnings({ "unused" })
	private JSONArray getResultObject(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap, String navPage, String portfolioId,
			String isEur, String isIncludeOrders) {
		Result serviceResult = new Result();
		JSONArray accListArr = new JSONArray();
		JSONArray jsonArrayList = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy");
		String replaceVal = ",", replaceWith = ".";
		if (isEur.equalsIgnoreCase("true")) {
			replaceVal = ".";
			replaceWith = ",";
			sdformat = new SimpleDateFormat("dd/MM/YYYY");
		}
		NumberFormat formatter = new DecimalFormat("#0.00");
		if (navPage.equalsIgnoreCase("Holdings")) {
			fields.clear();
			checkCurr = getCurrency();
			String[] fieldName;
			String[] columnName;
			if(isIncludeOrders.equalsIgnoreCase("true"))
			{
				columnCount = 7;	
				 fieldName = new String[] { "description", "ISIN", "quantity", "costPrice", "unrealPLMkt",
				"marketValue", "status" };
				 columnName = new String[] { "Instrument", "ISIN", "Quantity", "Average Cost", "Unrealized P&L",
				"Market Value", "Status" };
			}else
			{
				columnCount = 6;
				fieldName = new String[] { "description", "ISIN", "quantity", "costPrice", "unrealPLMkt",
				"marketValue" };
				columnName = new String[] { "Instrument", "ISIN", "Quantity", "Average Cost", "Unrealized P&L",
				"Market Value" };
			}
			
			for (int k = 0; k < fieldName.length; k++) {
				fields.put(columnName[k], fieldName[k]);
			}
			try {
				HoldingsListBackendDelegateImpl holdingsListBackendDelegate = new HoldingsListBackendDelegateImpl();
				serviceResult = holdingsListBackendDelegate.getHoldingsList(methodID, inputArray, request, response,
						headersMap);

				List<Dataset> dataset = serviceResult.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				String refCurr = serviceResult.getParamValueByName("referenceCurrency");
				for (int j = 0; j < drecords.size(); j++) {
					JSONObject actListObj = new JSONObject();
					Record drecord = drecords.get(j);
					actListObj = CommonUtils.convertRecordToJSONObject(drecord); // secCCy
					String secCurr = actListObj.get("secCCy").toString();

					String curr = checkCurr.get(secCurr) == null ? secCurr.toUpperCase()
							: checkCurr.get(secCurr).toString();
					String refcurr = checkCurr.get(refCurr) == null ? refCurr.toUpperCase()
							: checkCurr.get(refCurr).toString();

					if(actListObj.get("costPrice").toString().equalsIgnoreCase("")) {

						actListObj.put("costPrice",correctDecimaPlaces(actListObj.get("costPrice").toString(), isEur));
					}
					else {
					actListObj.put("costPrice",
							curr.concat(correctDecimaPlaces(actListObj.get("costPrice").toString(), isEur)));
					}
					actListObj.put("unrealPLMkt",
							refcurr.concat(correctDecimaPlaces(actListObj.get("unrealPLMkt").toString(), isEur)));
					actListObj.put("marketValue",
							refcurr.concat(correctDecimaPlaces(actListObj.get("marketValue").toString(), isEur)));

					actListObj.remove("RICCode");
					actListObj.remove("holdingsId");
					actListObj.remove("holdingsType");
					accListArr.put(actListObj);
				}
				return accListArr;
			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ PortfolioWealthAPIServices.WEALTH_GETTRANSACTIONDETAILS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		if (navPage.equalsIgnoreCase("Transactions")) {
			columnCount = 8;
			fields.clear();
			checkCurr = getCurrency();
			String[] fieldName = new String[] { "tradeDate", "description", "orderType", "quantity", "limitPrice",
					"instrumentAmount", "fees", "total" };
			String[] columnName = new String[] { "Trade Date", "Instrument", "OrderType", "Quantity", "Price", "Amount",
					"Fees", "Total Amount" };
			DecimalFormat df = new DecimalFormat("#.##");
			for (int k = 0; k < fieldName.length; k++) {
				fields.put(columnName[k], fieldName[k]);
			}
			try {
				TransactionsListBackendDelegateImpl transactionsListBackendDelegateImpl = new TransactionsListBackendDelegateImpl();
				serviceResult = transactionsListBackendDelegateImpl.getTransactionDetails(methodID, inputArray, request,
						response, headersMap);

				List<Dataset> dataset = serviceResult.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				for (int j = 0; j < drecords.size(); j++) {
					JSONObject actListObj = new JSONObject();
					Record drecord = drecords.get(j);
					actListObj = CommonUtils.convertRecordToJSONObject(drecord); // secCCy
					String secCurr = actListObj.get("instrumentCurrency").toString();
					String refCurr = actListObj.get("referenceCurrency").toString();
					String feesCurr = actListObj.get("feesCurrency").toString();
					String curr = checkCurr.get(secCurr) == null ? secCurr.toUpperCase()
							: checkCurr.get(secCurr).toString();
					String refcurr = checkCurr.get(refCurr) == null ? refCurr.toUpperCase()
							: checkCurr.get(refCurr).toString();
					String feescurr = checkCurr.get(feesCurr) == null ? feesCurr.toUpperCase()
							: checkCurr.get(feesCurr).toString();

					actListObj.put("limitPrice",
							curr.concat(correctDecimaPlaces(actListObj.get("limitPrice").toString(), isEur)));
					actListObj.put("instrumentAmount",
							refcurr.concat(correctDecimaPlaces(actListObj.get("instrumentAmount").toString(), isEur)));
					Double tot = Double.parseDouble(actListObj.get("total").toString());
					String total = String.format("%.2f", tot);
					actListObj.put("total", refcurr.concat(correctDecimaPlaces(total,isEur)));
					actListObj.put("fees", feescurr.concat(correctDecimaPlaces(actListObj.get("fees").toString(), isEur)));

					actListObj.put("tradeDate", sdformat.format(new SimpleDateFormat("yyyy-MM-dd").parse(actListObj.get("tradeDate").toString())));
					actListObj.remove("valueDate");
					actListObj.remove("ISIN");
					actListObj.remove("transactionId");
					actListObj.remove("exchangeRate");
					actListObj.remove("RICCode");
					actListObj.remove("holdingsType");
					actListObj.remove("netAmount");
					accListArr.put(actListObj);
				}

				return accListArr;
			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ PortfolioWealthAPIServices.WEALTH_GETPORTFOLIOHOLDINGS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		if (navPage.equalsIgnoreCase("Accounts Activity")) {
			columnCount = 6;
			fields.clear();
			checkCurr = getCurrency();
			String[] fieldName = new String[] { "bookingDate", "shortName", "quantity", "displayName", "amount",
					"balance" };
			String[] columnName = new String[] { "Booking Date", "Instrument", "Quantity", "Type", "Change",
					"Account Balance" };
			for (int k = 0; k < fieldName.length; k++) {
				fields.put(columnName[k], fieldName[k]);
			}
			try {
				AccountActivityBackendDelegateImpl accountActivityBackendDelegateImpl = new AccountActivityBackendDelegateImpl();
				serviceResult = accountActivityBackendDelegateImpl.getAccountActivityOperations(methodID, inputArray,
						request, response, headersMap);
				JSONObject recordObject = new JSONObject();
				List<Record> records = serviceResult.getAllRecords();
				List<Dataset> dataset;
				for (int i = 0; i < records.size(); i++) {
					Record record = records.get(i);
					recordObject = CommonUtils.convertRecordToJSONObject(record);
					dataset = record.getAllDatasets();
					List<Record> drecords = dataset.get(0).getAllRecords();
					for (int j = 0; j < drecords.size(); j++) {
						JSONObject actListObj = new JSONObject();
						Record drecord = drecords.get(j);
						actListObj = CommonUtils.convertRecordToJSONObject(drecord);
						String secCurr = actListObj.get("currencyId").toString();

						String curr = checkCurr.get(secCurr) == null ? secCurr.toUpperCase()
								: checkCurr.get(secCurr).toString();

						actListObj.put("amount",
								curr.concat(correctDecimaPlaces(actListObj.get("amount").toString(), isEur)));
						actListObj.put("balance",
								curr.concat(correctDecimaPlaces(actListObj.get("balance").toString(), isEur)));

						actListObj.put("bookingDate", sdformat.format(
								new SimpleDateFormat("yyyyMMdd").parse(actListObj.get("bookingDate").toString())));

						actListObj.remove("accountId");
						actListObj.remove("valueDate");
						actListObj.remove("currencyId");
						accListArr.put(actListObj);

					}
				}
				return accListArr;
			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITYOPERATIONS.getOperationName() + "  : "
						+ e).log();
				return null;
			}
		}
		if (navPage.equalsIgnoreCase("Performance")) {
			columnCount = 4;
			fields.clear();
			checkCurr = getCurrency();
			String[] fieldName = new String[] { "dateTime", "percentageChange", "portfolioReturn", "benchMarkIndex" };
			String[] columnName = new String[] { "Date", "Portfolio Return", "Portfolio Value", "Benchmark Return" };
			for (int k = 0; k < fieldName.length; k++) {
				fields.put(columnName[k], fieldName[k]);
			}
			try {
				PortfolioPerformanceBackendDelegateImpl portfolioPerformanceBackendDelegate = new PortfolioPerformanceBackendDelegateImpl();
				serviceResult = portfolioPerformanceBackendDelegate.getPortfolioPerformance(methodID, inputArray,
						request, response, headersMap);
				JSONObject recordObject = new JSONObject();
				JSONObject changedObj = new JSONObject();
				String secCCy = serviceResult.getParamValueByName("referenceCurrency");
				List<Dataset> dataset = serviceResult.getAllDatasets();
				List<Record> drecords = (serviceResult.getDatasetById("sortedMonthlyOverview").getAllRecords()
						.size() > 0) ? serviceResult.getDatasetById("sortedMonthlyOverview").getAllRecords()
								: serviceResult.getDatasetById("monthlyOverview").getAllRecords();
				for (int j = 0; j < drecords.size(); j++) {
					JSONObject actListObj = new JSONObject();
					Record drecord = drecords.get(j);
					actListObj = CommonUtils.convertRecordToJSONObject(drecord); // secCCy
					String secCurr = secCCy;

					String curr = checkCurr.get(secCurr) == null ? secCurr.toUpperCase()
							: checkCurr.get(secCurr).toString();

					actListObj.put("portfolioReturn",
							curr.concat(correctDecimaPlaces(actListObj.get("portfolioReturn").toString(), isEur)));

					String perChange = actListObj.get("percentageChange").toString().concat("%");
					String benReturn = actListObj.get("benchMarkIndex").toString().equals("") ? ""
							: actListObj.get("benchMarkIndex").toString().concat("%");
					actListObj.put("dateTime", sdformat
							.format(new SimpleDateFormat("yyyyMMdd").parse(actListObj.get("dateTime").toString())));
					actListObj.put("percentageChange", perChange);
					actListObj.put("benchMarkIndex", benReturn);
					accListArr.put(actListObj);
				}
				// Performance List
				List<Record> records = serviceResult.getAllRecords();
				Record record = records.get(0);
				recordObject = CommonUtils.convertRecordToJSONObject(record);
				LinkedHashMap<String, String> mapObj = new LinkedHashMap<String, String>();
				String[] fName = new String[] { "timeWeighted", "moneyWeighted", "initialValue", "netDeposit", "pl",
						"feesAndTax", "currentValue" };
				String[] cName = new String[] { "Time-Weighted Return", "Money-Weighted Return", "Initial Value",
						"Net Deposit", "P&L", "Fees & Taxes", "Current Value" };
				for (int k = 0; k < fName.length; k++) {
					mapObj.put(fName[k], cName[k]);
				}
				for (String keys : mapObj.keySet()) {
					if (checkCurr.containsKey(secCCy)) {
						if (keys.equalsIgnoreCase("timeWeighted") || keys.equalsIgnoreCase("moneyWeighted")) {
							changedObj.put(mapObj.get(keys), recordObject.get(keys).toString().isEmpty() ? ""
									: (correctDecimaPlaces(recordObject.get(keys).toString(), isEur).concat("%")));
						} else {
							String curr = checkCurr.get(secCCy).toString();
							changedObj.put(mapObj.get(keys),
									curr.concat(correctDecimaPlaces(recordObject.get(keys).toString(), isEur)));
						}
					}
				}

				jsonArrayList.put(accListArr);
				jsonArrayList.put(changedObj);
				return jsonArrayList;
			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITYOPERATIONS.getOperationName() + "  : "
						+ e).log();
				return null;
			}
		}
		if (navPage.equalsIgnoreCase("Open Order")) {
			columnCount = 10;
			fields.clear();
			checkCurr = getCurrency();
			String[] fieldName = new String[] { "description", "ISIN", "holdingsType", "orderReference", "tradeDate",
					"orderType", "quantity", "limitPrice", "stopPrice", "status" };
			String[] columnName = new String[] { "Instrument", "ISIN", "Exchange", "Reference ID", "Date", "Type",
					"Quantity", "Limit Price", "Stop Price", "Status" };
			for (int k = 0; k < fieldName.length; k++) {
				fields.put(columnName[k], fieldName[k]);
			}
			try {
				OrdersListBackendDelegateImpl ordersListBackendDelegateImpl = new OrdersListBackendDelegateImpl();
				serviceResult = ordersListBackendDelegateImpl.getOrdersDetails(methodID, inputArray, request, response,
						headersMap);
				List<Dataset> dataset = serviceResult.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				for (int j = 0; j < drecords.size(); j++) {
					JSONObject actListObj = new JSONObject();
					Record drecord = drecords.get(j);
					actListObj = CommonUtils.convertRecordToJSONObject(drecord); // secCCy
					String secCurr = actListObj.get("instrumentCurrency").toString();
					String curr = checkCurr.get(secCurr) == null ? secCurr.toUpperCase()
							: checkCurr.get(secCurr).toString();

					String orderType = actListObj.get("orderType").toString();
					String stopPrice = actListObj.get("stopPrice").toString();
					String limitPrice = actListObj.get("limitPrice").toString();
					actListObj.put("limitPrice",
							limitPrice.isEmpty() ? limitPrice : curr.concat(correctDecimaPlaces(limitPrice, isEur)));
					actListObj.put("stopPrice",
							stopPrice.isEmpty() ? stopPrice : curr.concat(correctDecimaPlaces(stopPrice, isEur)));

					actListObj.put("tradeDate", sdformat
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(actListObj.get("tradeDate").toString())));
					actListObj.remove("valueDate");
					actListObj.remove("orderId");
					actListObj.remove("price");
					actListObj.remove("RICCode");
					actListObj.remove("instrumentAmount");
					actListObj.remove("fees");
					actListObj.remove("netAmount");
					actListObj.remove("valueDate");
					actListObj.remove("orderMode");
					actListObj.remove("orderModeType");
					// actListObj.remove("stopPrice");
					actListObj.remove("validity");
					// actListObj.remove("orderReference");
					accListArr.put(actListObj);
				}
				return accListArr;
			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ PortfolioWealthAPIServices.WEALTH_GETORDERSDETAILS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		if (navPage.equalsIgnoreCase("History Order")) {
			columnCount = 9;
			fields.clear();
			checkCurr = getCurrency();
			String[] fieldName = new String[] { "description", "ISIN", "holdingsType", "orderReference", "tradeDate",
					"orderType", "quantity", "orderExecutionPrice", "status" };
			String[] columnName = new String[] { "Instrument", "ISIN", "Exchange", "Reference ID", "Date", "Type",
					"Quantity", "Price", "Status" };
			for (int k = 0; k < fieldName.length; k++) {
				fields.put(columnName[k], fieldName[k]);
			}
			try {
				OrdersListBackendDelegateImpl ordersListBackendDelegateImpl = new OrdersListBackendDelegateImpl();
				serviceResult = ordersListBackendDelegateImpl.getOrdersDetails(methodID, inputArray, request, response,
						headersMap);
				List<Dataset> dataset = serviceResult.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				for (int j = 0; j < drecords.size(); j++) {
					JSONObject actListObj = new JSONObject();
					Record drecord = drecords.get(j);
					actListObj = CommonUtils.convertRecordToJSONObject(drecord); // secCCy
					String secCurr = actListObj.get("instrumentCurrency").toString();

					String curr = checkCurr.get(secCurr) == null ? secCurr.toUpperCase()
							: checkCurr.get(secCurr).toString();

					String orderType = actListObj.get("orderType").toString();
					String stopPrice = actListObj.get("stopPrice").toString();
					String limitPrice = actListObj.get("limitPrice").toString();
					String orderExecutionPrice = actListObj.get("orderExecutionPrice").toString().replace(replaceVal,
							replaceWith);
					actListObj.put("orderExecutionPrice", orderExecutionPrice.isEmpty() ? orderExecutionPrice
							: curr.concat(correctDecimaPlaces(orderExecutionPrice, isEur)));

					actListObj.put("tradeDate", sdformat
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(actListObj.get("tradeDate").toString())));
					actListObj.remove("valueDate");
					actListObj.remove("orderId");
					actListObj.remove("price");
					actListObj.remove("RICCode");
					actListObj.remove("instrumentAmount");
					actListObj.remove("fees");
					actListObj.remove("netAmount");
					actListObj.remove("valueDate");
					actListObj.remove("orderMode");
					actListObj.remove("orderModeType");
					actListObj.remove("stopPrice");
					actListObj.remove("validity");
					// actListObj.remove("orderReference");
					accListArr.put(actListObj);
				}
				return accListArr;
			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ PortfolioWealthAPIServices.WEALTH_GETORDERSDETAILS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return null;
	}

	private LinkedHashMap<String, String> getCurrency() {
		currency.clear();
		String[] currencyAbb = new String[] { "USD", "EUR", "CRC", "GBP", "ILS", "INR", "JPY", "KRW", "NGN", "PHP",
				"PLN", "PYG", "THB", "UAH", "VND", "AUD", "CAD", "CHF", "SGD", "HKD"};
		String[] currencySymb = new String[] { "$", "€", "₡", "£", "₪", "INR ", "¥", "₩", "₦", "₱", "zł", "₲", "฿", "₴",
				"₫", "$", "$", "Fr.", "SGD", "HK$" };
		for (int k = 0; k < currencyAbb.length; k++) {
			currency.put(currencyAbb[k], currencySymb[k]);
		}
		return currency;

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String correctDecimaPlaces(String field, String isEur) throws NumberFormatException, ParseException {
		field = field.replace(",", "");
		String value = "";
		String value1 = "";
		if (field.equalsIgnoreCase("0.0")) {
			return field;
		} else if (field.equalsIgnoreCase("")) {
			return field;
		}
			else {
			if (isEur.equalsIgnoreCase("true")) {
				Locale ukLocale = new Locale("en", "DE");
				NumberFormat numberFormat = NumberFormat.getInstance(ukLocale);
				Double ukVal = Double.parseDouble(field);
				String val = String.format("%.2f", ukVal);
				Double ukkVal = Double.parseDouble(val);
				value = numberFormat.format(ukkVal);
		        int iP = value.indexOf(",");
		        int dP =  value.length() -  iP -1;
		        if (value.contains(",")){
		        	if (dP==1) {
		        	value1 = value.concat("0");
		        } else if (dP > 1) {
		        	value1 = value;
		        }
		      } else {
		        	value1 = value.concat(",00");
		        }
				return value1;

			} else {
				Locale usLocale = new Locale("en", "US");
				NumberFormat numberFormat = NumberFormat.getInstance(usLocale);
				Double usVal = Double.parseDouble(field);
//				NumberFormat formatter = new DecimalFormat("#0.00");
//			    value = formatter.format(usVal);
				value = numberFormat.format(Double.parseDouble(String.format("%.2f", usVal)));	
//				Double uVal = Double.parseDouble(value);
//				value1 = formatter.format(uVal);
				int iP = value.indexOf(".");
		        int dP =  value.length() -  iP -1;
		        if (value.contains(".")){
		        	if (dP==1) {
		        	value1 = value.concat("0");
		        } else if (dP > 1) {
		        	value1 = value;
		        }
		      } else {
		        	value1 = value.concat(".00");
		        }
				return value1;
			}
		}
	}

	public static String formatDateWithLocale(Date inputdate, String isEur) {
		if(isEur.equalsIgnoreCase("true")) {
			Locale ukLocale = new Locale("en", "DE");
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, ukLocale);		
			String formattedDate = dateFormat.format(inputdate);
			return formattedDate;
		}
		else {
			Locale usLocale = new Locale("en", "FR");
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, usLocale);
			String formattedDate = dateFormat.format(inputdate);
			return formattedDate;
		}
		
	}

}
