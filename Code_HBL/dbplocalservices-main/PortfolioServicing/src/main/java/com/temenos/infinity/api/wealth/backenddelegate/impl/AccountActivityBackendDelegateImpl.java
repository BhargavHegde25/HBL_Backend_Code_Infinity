package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.api.AccountActivityBackendDelegate;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class AccountActivityBackendDelegateImpl implements AccountActivityBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	//TransactionsListBackendDelegateImpl transactionsListBackendDelegateImpl = new TransactionsListBackendDelegateImpl();
	WealthMockUtil wealthMockUtil = new WealthMockUtil();
	String coreValue = "";
	Boolean isTAP = false;

	@SuppressWarnings({ "unchecked", "null", "rawtypes" })
	@Override
	public Result getAccountActivity(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		
		isTAP  = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_CORE,
				request).contains("TAP") ? true : false);
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object accountIdObj = inputParams.get(TemenosConstants.ACCID);
		Object dateFromObj = inputParams.get(TemenosConstants.DATEFROM);
		Object dateToObj = inputParams.get(TemenosConstants.DATETO);
		Object portfolioIdObj = inputParams.get(TemenosConstants.PORTFOLIOID);
		Object listTypeObj = inputParams.get(TemenosConstants.LISTTYPE);
		Object sortByObj = inputParams.get(TemenosConstants.SORTBY);
		Object sortTypeObj = inputParams.get(TemenosConstants.SORTORDER);
		String dateformat = "YYYYMMdd";

		int indexOfSort = 0;
		String sortBy = null, sortType = null;
		List sortValues = Arrays.asList("bookingDate", "amount", "balance", "displayName", "quantity", "valueDate",
				"shortName");
		String[] fieldName = new String[] { "bookingDate", "amount", "balance", "displayName", "quantity", "valueDate",
				"shortName" };
		String[] columnName = new String[] { "OPERATION_DATE", "DEBIT_CREDIT", "ACCOUNT_BALANCE", "OPERATION_NATURE",
				"QUANTITY", "VALUE_DATE", "Instr_Name" };

		String accountId = null, dateFrom = null, dateTo = null, portfolioId = null, listType = null;
		if (!sortValues.contains(sortByObj) ) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", TemenosConstants.SORTBY+" value is not valid");
			return Utilities.constructResultFromJSONObject(result);
		} else {
			if (portfolioIdObj != null || !portfolioIdObj.equals("")) {
				portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
				inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
			}
			if (accountIdObj != null || !accountIdObj.equals("")) {
				accountId = inputParams.get(TemenosConstants.ACCID).toString();
				inputMap.put(TemenosConstants.ACCID, accountId);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ACCID);
			}
			if (dateFromObj != null || !dateFromObj.equals("")) {
				dateFrom = inputParams.get(TemenosConstants.DATEFROM).toString();
				inputMap.put(TemenosConstants.DATEFROM, dateFrom);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat,dateFrom);
				if(!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATEFROM);
				}
			}
			if (dateToObj != null || !dateToObj.equals("")) {
				dateTo = inputParams.get(TemenosConstants.DATETO).toString();
				inputMap.put(TemenosConstants.DATETO, dateTo);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat,dateTo);
				if(!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATETO);
				}
			}
			if (listTypeObj != null || !listTypeObj.equals("")) {
				listType = inputParams.get(TemenosConstants.LISTTYPE).toString();
				inputMap.put(TemenosConstants.LISTTYPE, listType);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LISTTYPE);
			}
			if (sortByObj != null) {
				sortBy = inputParams.get(TemenosConstants.SORTBY).toString();
				inputMap.put(TemenosConstants.SORTBY, sortBy);
				indexOfSort = ArrayUtils.indexOf(fieldName, sortBy);
				if(indexOfSort == -1)
				{
					indexOfSort = 0;
				}
			}
			if (sortTypeObj != null) {
				sortType = inputParams.get(TemenosConstants.SORTORDER).toString();
				inputMap.put(TemenosConstants.SORTORDER, sortType);
				inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
				inputMap.put(TemenosConstants.SORTTYPE,sortType);
			} else {
				if (sortBy.equalsIgnoreCase("bookingDate") || sortBy.equalsIgnoreCase("valueDate")) {
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, "desc");
				} else {
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, "asc");
				}

			}
		}
		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
		List<String> allAccountList = PortfolioWealthUtils.getAllAccountsFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {
			if (allAccountList.contains(accountId)) {
				try {
					String serviceName = ServiceId.WEALTHORCHESTRATION;
					String operationName = OperationName.GETACCOUNTACTIVITY;

				return PortfolioWealthUtils.backendResponse(serviceName, operationName, inputMap, headersMap, request);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITY.getOperationName() + "  : " + e).log();
				return null;

				}
			} else {
				return PortfolioWealthUtils.UnauthorizedAccess();
			}
		} else {
			return PortfolioWealthUtils.UnauthorizedAccess();

		}
		
	}

	
	public String[] mockBookingDate(int arrayLength) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String[] tradeDateArr = new String[arrayLength];
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < arrayLength; i++) {
			if (i > 0) {
				cal.add(Calendar.MONTH, -1);
				cal.add(Calendar.DATE, 1);
			} else {
				cal.add(Calendar.MONTH, 0);
				cal.add(Calendar.DATE, 0);
			}
			tradeDateArr[i] = sdf.format(cal.getTime());
		}
		return tradeDateArr;
	}

	public JSONArray filterDate(JSONArray array, String startDate, String endDate) {
		JSONArray filteredArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < array.length(); ++i) {
			JSONObject obj = array.getJSONObject(i);
			try {
				Date sdt = sdformat.parse(startDate);
				Date edt = sdformat.parse(endDate);
				Date tdt;
				String dateHolder = obj.getString(TemenosConstants.BOOKINGDATE);
				if(dateHolder.contains("T")) {
					String[] arrOfDateStr = dateHolder.split("T",2);
					String a = arrOfDateStr[0];
					a = a.replace("-","");
					tdt = sdformat.parse(a);
				}
				else {
					tdt = sdformat.parse(dateHolder);
				}
				//if (tdt.after(sdt) && tdt.before(edt)) {
				if (sdt.compareTo(tdt) * tdt.compareTo(edt) >= 0) {
					filteredArray.put(obj);
				}
			} catch (Exception e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITY.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filteredArray;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result getAccountActivityOperations(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object sortByObj = inputParams.get(TemenosConstants.SORTBY);
		Object sortTypeObj = inputParams.get(TemenosConstants.SORTORDER);
		Object searchObj = inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		Object accountIdObj = inputParams.get(TemenosConstants.ACCID);
		Object dateFromObj = inputParams.get(TemenosConstants.DATEFROM);
		Object dateToObj = inputParams.get(TemenosConstants.DATETO);
		Object portfolioIdObj = inputParams.get(TemenosConstants.PORTFOLIOID);
		Object listTypeObj = inputParams.get(TemenosConstants.LISTTYPE);
		Object limitObj = inputParams.get(TemenosConstants.PAGESIZE);
		Object offsetObj = inputParams.get(TemenosConstants.PAGEOFFSET);
		List sortValues = Arrays.asList("amount", "balance", "displayName", "shortName", "quantity", "bookingDate",
				"valueDate");
		String sortBy = null;
		String search = null;
		int indexOfSort = 0;
		String dateformat = "YYYYMMdd";

		String[] fieldName = new String[] { "bookingDate", "amount", "balance", "displayName", "quantity", "valueDate",
				"shortName" };
		String[] columnName = new String[] { "OPERATION_DATE", "DEBIT_CREDIT", "ACCOUNT_BALANCE", "OPERATION_NATURE",
				"QUANTITY", "VALUE_DATE", "Instr_Name" };

		String accountId = null, dateFrom = null, dateTo = null, portfolioId = null, listType = null, sortType = null,
				limit = null, offset = null;
		if (!sortValues.contains(sortByObj)) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", TemenosConstants.SORTBY + " value is not valid");
			return Utilities.constructResultFromJSONObject(result);
		} else {
			if (sortByObj != null) {
				sortBy = inputParams.get(TemenosConstants.SORTBY).toString();
				inputMap.put(TemenosConstants.SORTBY, sortBy);
				indexOfSort = ArrayUtils.indexOf(fieldName, sortBy);
				if(indexOfSort == -1)
				{
					indexOfSort = 0;
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.SORTBY);
			}
			if (searchObj != null) {
				search = inputParams.get(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString();
				inputMap.put(TemenosConstants.SEARCHBYINSTRUMENTNAME, search);
			}
			if (portfolioIdObj != null && !portfolioIdObj.equals("")) {
				portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
				inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
			}
			if (accountIdObj != null && !accountIdObj.equals("")) {
				accountId = inputParams.get(TemenosConstants.ACCID).toString();
				inputMap.put(TemenosConstants.ACCID, accountId);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.ACCID);
			}

			if (dateFromObj != null && !dateFromObj.equals("")) {
				dateFrom = inputParams.get(TemenosConstants.DATEFROM).toString();
				inputMap.put(TemenosConstants.DATEFROM, dateFrom);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, dateFrom);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATEFROM);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DATEFROM);
			}
			if (dateToObj != null && !dateToObj.equals("")) {
				dateTo = inputParams.get(TemenosConstants.DATETO).toString();
				inputMap.put(TemenosConstants.DATETO, dateTo);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, dateTo);
				if (!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATETO);
				}
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DATETO);
			}
			if (listTypeObj != null && !listTypeObj.equals("")) {
				listType = inputParams.get(TemenosConstants.LISTTYPE).toString();
				inputMap.put(TemenosConstants.LISTTYPE, listType);
			} else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.LISTTYPE);
			}
			if (sortTypeObj != null) {
				sortType = inputParams.get(TemenosConstants.SORTORDER).toString();
				inputMap.put(TemenosConstants.SORTORDER, sortType);
				inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
				inputMap.put(TemenosConstants.SORTTYPE,sortType);
			} else {
				if (sortBy.equalsIgnoreCase("bookingDate") || sortBy.equalsIgnoreCase("valueDate")) {
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, "desc");
				} else {
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, "asc");
				}

			}
			if (limitObj != null) {
				limit = inputParams.get(TemenosConstants.PAGESIZE).toString();
				inputMap.put(TemenosConstants.PAGESIZE, limit);
			}
			if (offsetObj != null) {
				offset = inputParams.get(TemenosConstants.PAGEOFFSET).toString();
				inputMap.put(TemenosConstants.PAGEOFFSET, offset);
			}
		}
		
		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
		List<String> allAccountList = PortfolioWealthUtils.getAllAccountsFromCache(request);
		if (allportfoliosList.contains(portfolioId)) {
			if (allAccountList.contains(accountId)) {
				try {
					Result result = new Result();
					result = getAccountActivity(methodID, inputArray, request, response, headersMap);
					try {
						if (result.getErrMsgParamValue() != null) {
							return result;
						}
						JSONObject jsonResult = getAccActOperations(inputMap, result);
						return Utilities.constructResultFromJSONObject(jsonResult);
					} catch (Exception e) {

					alert.prepareError("Error while invoking Service - "
							+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITYOPERATIONS.getOperationName() + "  : " + e).log();
				}
			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITY.getOperationName() + "  : " + e).log();
				return null;

				}
			} else {
				return PortfolioWealthUtils.UnauthorizedAccess();
			}
		} else {
			return PortfolioWealthUtils.UnauthorizedAccess();

		}
		
		return null;

	}

	@SuppressWarnings("unused")
	private JSONObject getAccActOperations(Map<String, Object> inputMap, Result result) {
		String portfolioId = (String) inputMap.get(TemenosConstants.PORTFOLIOID);
		String accountId = (String) inputMap.get(TemenosConstants.ACCID);
		String listType = (String) inputMap.get(TemenosConstants.LISTTYPE);
		String dateFrom = (String) inputMap.get(TemenosConstants.DATEFROM);
		String dateTo = (String) inputMap.get(TemenosConstants.DATETO);
		String sortBy = (String) inputMap.get(TemenosConstants.SORTBY);
		String sortType = (String) inputMap.get(TemenosConstants.SORTORDER);
		String searchVal = (String) inputMap.get(TemenosConstants.SEARCHBYINSTRUMENTNAME);
		String limitVal = (String) inputMap.get(TemenosConstants.PAGESIZE);
		String offsetVal = (String) inputMap.get(TemenosConstants.PAGEOFFSET);
		
		int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
		int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;
		
		int totalCount = 0;
		JSONObject recordObject = new JSONObject();
		JSONArray accListArr = new JSONArray();
		JSONObject response = new JSONObject();
		JSONObject status = new JSONObject();
		JSONObject accountActivity = new JSONObject();
		String search =  (searchVal != null && searchVal.trim().length() > 0) ? searchVal : "";
		if (result.getAllDatasets().size()>0 ) {
			List<Dataset> dataset = result.getAllDatasets();
			List<Record> drecords = dataset.get(0).getAllRecords();
			for (int j = 0; j < drecords.size(); j++) {
				JSONObject actListObj = new JSONObject();
				Record drecord = drecords.get(j);
				actListObj = CommonUtils.convertRecordToJSONObject(drecord);
				if (actListObj.get(TemenosConstants.BOOKINGDATE) != null
						&& !actListObj.get(TemenosConstants.BOOKINGDATE).equals("")) {
					actListObj.put(TemenosConstants.BOOKINGDATE,
							actListObj.get(TemenosConstants.BOOKINGDATE).toString().replace("-", ""));
					if (actListObj.get(TemenosConstants.VALUEDATE) != null
							&& !actListObj.get(TemenosConstants.VALUEDATE).equals("")) {
						actListObj.put(TemenosConstants.VALUEDATE,
								actListObj.get(TemenosConstants.VALUEDATE).toString().replace("-", ""));
					}
					accListArr.put(actListObj);
				}
			}
		}else {
			List<Record> records = result.getAllRecords();
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
					if (actListObj.get(TemenosConstants.BOOKINGDATE) != null
							&& !actListObj.get(TemenosConstants.BOOKINGDATE).equals("")) {
						actListObj.put(TemenosConstants.BOOKINGDATE,
								actListObj.get(TemenosConstants.BOOKINGDATE).toString().replace("-", ""));
						if (actListObj.get(TemenosConstants.VALUEDATE) != null
								&& !actListObj.get(TemenosConstants.VALUEDATE).equals("")) {
							actListObj.put(TemenosConstants.VALUEDATE,
									actListObj.get(TemenosConstants.VALUEDATE).toString().replace("-", ""));
						}
						accListArr.put(actListObj);
					}
				}
			}
		}
		JSONArray sortedArray = new JSONArray();
		sortedArray = accListArr;
		
		if (sortBy != null && isTAP ==false) {
			sortedArray = sortArray(sortedArray, sortBy, sortType);
		} else {
		}
		if (search.equals("")) {

		} else {
			sortedArray = returnSearch(sortedArray, search);
		}
		
		totalCount = sortedArray.length();
		
		if (limit > 0 && offset >= 0) {
			sortedArray = wealthMockUtil.pagination(sortedArray, limit, offset);
		}
		
		status.put(TemenosConstants.STATUS, "success");
		response.put("body", sortedArray);
		response.put("header", status);
		accountActivity.put("accountActivityList", response);
		accountActivity.put("totalCount", totalCount);
		return accountActivity;

	}

	public JSONArray returnSearch(JSONArray array, String searchValue) {
		JSONArray filteredArray = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String search = searchValue.toLowerCase();

				String desc = obj.getString("displayName").toLowerCase();

				if (desc.contains(search)) {
					filteredArray.put(obj);
				}

			} catch (JSONException e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITYOPERATIONS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filteredArray;
	}
	
	public JSONArray returnAccountsBasedOnId(JSONArray array, String accountId) {
		JSONArray filteredArray = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String accId = obj.getString("accountId");
				if (accId.contentEquals(accountId)) {
					filteredArray.put(obj);
				}

			} catch (JSONException e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETACCOUNTACTIVITYOPERATIONS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filteredArray;
	}


	public JSONArray sortArray(JSONArray array, String sortBy, String sortType) {
		JSONArray sortedJSON = new JSONArray();
		JSONArray sortedJSONArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			jsonValues.add(array.getJSONObject(i));
		}
		if (sortBy.equals("") || sortBy.equalsIgnoreCase(TemenosConstants.BOOKINGDATE)
				|| sortBy.equalsIgnoreCase(TemenosConstants.VALUEDATE)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = (sortBy.equalsIgnoreCase(TemenosConstants.VALUEDATE)) ? "valueDate"
						: sortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					str1 = (String) a.get(KEY_NAME);
					str2 = (String) b.get(KEY_NAME);
					Date d1 = new Date();
					Date d2 = new Date();
					try {
						d1 = sdformat.parse(str1);
						d2 = sdformat.parse(str2);
					} catch (ParseException e) {
						e.getMessage();
					}
					return d1.compareTo(d2);
				}

			});
			for (int i = array.length() - 1; i >= 0; i--) {
				sortedJSON.put(jsonValues.get(i));
			}
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.ASCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
					sortedJSONArray.put(sortedJSON.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(sortedJSON.get(i));
				}
			}

		} else if (sortBy.equalsIgnoreCase(TemenosConstants.QUANTITY)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = sortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = null;
					String str2 = null;
					Integer val1 = null, val2;
					if (a.has(sortBy)) {
						str1 = (String) a.get(KEY_NAME);
						if (str1 != null && !str1.trim().isEmpty()) {
							val1 = Integer.parseInt(str1);
						} else {
							val1 = 0;
						}
					} else {
						val1 = 0;
					}
					if (b.has(sortBy)) {
						str2 = (String) b.get(KEY_NAME);
						if (str2 != null && !str2.trim().isEmpty()) {
							val2 = Integer.parseInt(str2);
						} else {
							val2 = 0;
						}
					} else {
						val2 = 0;
					}
					return val1.compareTo(val2);
				}

			});
			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}
			
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
					sortedJSONArray.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(jsonValues.get(i));
				}
			}

		} else if (sortBy.equalsIgnoreCase(TemenosConstants.DISPLAYNAME)
				|| sortBy.equalsIgnoreCase(TemenosConstants.SHORTNAME)) {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = sortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String str1 = new String();
					String str2 = new String();
					if (a.has(sortBy)) {
						str1 = (String) a.get(KEY_NAME);
						if (str1 != null && !str1.trim().isEmpty()) {
						} else {
							str1 = "";
						}
					} else {
						str1 = "";
					}
					if (b.has(sortBy)) {
						str2 = (String) b.get(KEY_NAME);
						if (str2 != null && !str2.trim().isEmpty()) {
						} else {
							str2 = "";
						}
					} else {
						str2 = "";
					}
					return str1.compareToIgnoreCase(str2);
				}

			});
			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}
			
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
					sortedJSONArray.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(jsonValues.get(i));
				}
			}
			
		} else {
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				private final String KEY_NAME = sortBy;

				@Override
				public int compare(JSONObject a, JSONObject b) {
					if (a.has(KEY_NAME) && b.has(KEY_NAME) && a.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return (b.get(KEY_NAME).toString().equalsIgnoreCase("")) ? 0 : -1;
					}
					if (b.has(KEY_NAME) && b.get(KEY_NAME).toString().equalsIgnoreCase("")) {
						return 1;
					}
					Double str1 = null;
					Double str2 = null;
					str1 = (Double) a.getDouble(KEY_NAME);
					str2 = (Double) b.getDouble(KEY_NAME);
					return str1.compareTo(str2);
				}
			});

			for (int i = 0; i < array.length(); i++) {
				sortedJSON.put(jsonValues.get(i));
			}
			
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
					sortedJSONArray.put(jsonValues.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(jsonValues.get(i));
				}
			}
		}
		return sortedJSONArray;

	}
}
