package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.api.PortfolioPerformanceBackendDelegate;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class PortfolioPerformanceBackendDelegateImpl implements PortfolioPerformanceBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	WealthMockUtil wealthMockUtil = new WealthMockUtil();
	String coreValue = "";
	
	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public Result getPortfolioPerformance(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object portfolioIdobj = inputParams.get(TemenosConstants.PORTFOLIOID);
		Object dateFromObj = inputParams.get(TemenosConstants.DATEFROM);
		Object dateToObj = inputParams.get(TemenosConstants.DATETO);
		Object benchMarkObj=inputParams.get(TemenosConstants.BENCHMARK);
		Object durationObj=inputParams.get(TemenosConstants.DURATION);
		Object sortByObj = inputParams.get(TemenosConstants.SORTBY);
		Object sortTypeObj = inputParams.get(TemenosConstants.SORTORDER);
		Object limitObj = inputParams.get(TemenosConstants.PAGESIZE);
		Object offsetObj = inputParams.get(TemenosConstants.PAGEOFFSET);
		Object currencyId = inputParams.get(TemenosConstants.CURRENCYID);
		String dateformat = "YYYYMMdd";
		String dateFrom = null, dateTo = null, portfolioId = null, benchMark=null,duration=null,sortBy=null,sortType=null,
				limit=null,offset=null;
		 
			if (portfolioIdobj != null || !portfolioIdobj.equals("")) {
				portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
				inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
			}
			else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
			}
			if (dateFromObj != null || !dateFromObj.equals("")) {
				dateFrom = inputParams.get(TemenosConstants.DATEFROM).toString();
				inputMap.put(TemenosConstants.DATEFROM, dateFrom);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat,dateFrom);
				if(!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATEFROM);
				}
			}
			else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DATEFROM);
			}
			
			if (dateToObj != null || ! dateToObj.equals("")) {
				dateTo = inputParams.get(TemenosConstants.DATETO).toString();
				inputMap.put(TemenosConstants.DATETO, dateTo);
				boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat,dateTo);
				if(!isTrue) {
					return PortfolioWealthUtils.validateFormat(TemenosConstants.DATETO);
				}
			}
			else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DATETO);
			}
			if (benchMarkObj != null) {
				benchMark = inputParams.get(TemenosConstants.BENCHMARK).toString();
				inputMap.put(TemenosConstants.BENCHMARK, benchMark);
			}
			if (durationObj != null || ! durationObj.equals("")) {
				duration = inputParams.get(TemenosConstants.DURATION).toString();
				inputMap.put(TemenosConstants.DURATION, duration);
			}
			else {
				return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.DURATION);
			}
			if (sortByObj != null) {
				sortBy = inputParams.get(TemenosConstants.SORTBY).toString();
				inputMap.put(TemenosConstants.SORTBY, sortBy);
			}
			if (sortTypeObj != null) {
				sortType = inputParams.get(TemenosConstants.SORTORDER).toString();
				inputMap.put(TemenosConstants.SORTORDER, sortType);
			}
			if (limitObj != null) {
				limit = inputParams.get(TemenosConstants.PAGESIZE).toString();
				inputMap.put(TemenosConstants.PAGESIZE, limit);
			}
			if (offsetObj != null) {
				offset = inputParams.get(TemenosConstants.PAGEOFFSET).toString();
				inputMap.put(TemenosConstants.PAGEOFFSET, offset);
			}
			if (currencyId != null) {
				currencyId = inputParams.get(TemenosConstants.CURRENCYID).toString();
				inputMap.put(TemenosConstants.CURRENCYID, currencyId);
			}
		
		String createResponse = null;
		String serviceName = ServiceId.WEALTHORCHESTRATION;
		String operationName = OperationName.GETPORTFOLIOPERFORMANCE;
		
		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {
			try {
				createResponse = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceName).
						withObjectId(null).
						withOperationId(operationName).
						withRequestParameters(inputMap).
						withRequestHeaders(headersMap).
						withDataControllerRequest(request).
						build().getResponse();
				JSONObject resultJSON = new JSONObject(createResponse);
				return Utilities.constructResultFromJSONObject(resultJSON);
			} catch (Exception e) {
				alert.prepareError("Error while invoking Service - " + PortfolioWealthAPIServices.WEALTH_GETPORTFOLIOPERFORMANCE.getOperationName()
				+ "  : " + e).log();
			}
		}else {
			alert.prepareError("Portfolio ID "+portfolioId +" does not exist for the Customer").log();
			return PortfolioWealthUtils.UnauthorizedAccess();
		}
		return null;
	}
	public JSONArray filterDate(JSONArray array, String startDate, String endDate) {
		JSONArray filteredArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < array.length(); ++i) {
			JSONObject obj = array.getJSONObject(i);
			try {
				Date sdt = sdformat.parse(startDate);
				Date edt = sdformat.parse(endDate);
				Date tdt = sdformat.parse(obj.getString(TemenosConstants.DATE_TIME));
				if (sdt.compareTo(tdt) * tdt.compareTo(edt) >= 0) {
					filteredArray.put(obj);
				}
			} catch (Exception e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETPORTFOLIOPERFORMANCE.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filteredArray;

	}
	
	private JSONArray concatArray(JSONArray arr1, JSONArray arr2) {
	    JSONArray result = new JSONArray();
	    for (int i = 0; i < arr1.length(); i++) {
	        result.put(arr1.get(i));
	    }
	    for (int i = 0; i < arr2.length(); i++) {
	        result.put(arr2.get(i));
	    }
	    return result;
	}
		
	public JSONArray filterCustomDate(JSONArray array, String startDate, String endDate) throws ParseException {
		JSONArray filteredArray = new JSONArray();
		JSONArray filteredNewArray = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date1=new SimpleDateFormat("yyyyMMdd").parse(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		cal.get(Calendar.DAY_OF_MONTH);
		String lastDateOfPreviousMonth="";
		
		
		if((cal.get(Calendar.DAY_OF_MONTH))>=15 && (cal.get(Calendar.DAY_OF_MONTH))<=20) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE, -1);
			lastDateOfPreviousMonth = sdf.format(cal.getTime());
		}
		filteredNewArray=filterDate(array,lastDateOfPreviousMonth,lastDateOfPreviousMonth);
		//if(filteredNewArray != null) {
			//JSONObject object = filteredNewArray.getJSONObject(0);
			//filteredArray.put(object);
		//}
		filteredArray=filterDate(array,startDate,endDate);
		if(lastDateOfPreviousMonth != "") {
			return concatArray(filteredNewArray,filteredArray);
		}else {
			return filteredArray;
		}

	}
	
	
	public String[] mockDateTime(int arrayLength) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String[] tradeDateArr = new String[arrayLength];
		String[] tradeDateArrRev = new String[arrayLength];
		Calendar cal = Calendar.getInstance();
		Calendar calToday = Calendar.getInstance();
		for (int i = 1; i < arrayLength; i++) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			 cal.add(Calendar.DATE, -1);
			tradeDateArr[i] = sdf.format(cal.getTime());
		}
		tradeDateArr[0] = sdf.format(calToday.getTime());
		int k = 0;
		for (int j = arrayLength - 1; j >= 0; j--) {
			tradeDateArrRev[k] = tradeDateArr[j];
			k++;
		}
		return tradeDateArrRev;

	}
	
	@SuppressWarnings("unused")
	public JSONArray getBenchMarkArray() {
		JSONObject response = new JSONObject();
		String[] benchMark = null, id = null;
		benchMark = new String[] { "S&P500", "NASDAQ", "DOWJ" };
		id = new String[] { "1", "2", "3" };
		JSONArray benchMarkArray = new JSONArray();
		for (int i = 0; i < benchMark.length; i++) {
			JSONObject benchMarkObj = new JSONObject();
			benchMarkObj.put("benchMark", benchMark[i]);
			benchMarkObj.put("benchMarkId", id[i]);
			benchMarkArray.put(benchMarkObj);
		}
		
		return benchMarkArray;
	}
	
	public String[] getBenchMarkReturn(String selectedBenchMark) {
		String[] response=null;
		String[] sp=null,nasdaq=null,dowJ=null;
		sp=new String[] {"2","3","11","8","-1","6","2","8","-3","1","6","8","17",
				"11","3","-2","1","-2","4","3"};
		nasdaq=new String[] {"4","5","9","7","-1","7","1","9","-2","1","6","8","10",
				"12","2","-2","3","-2","3","-1"};
		dowJ=new String[] {"3","1","8","2","-1","7","1","10","-2","1","4","5","8",
				"6","2","-2","3","3","-1","2"};
		if(selectedBenchMark.equals("S&P500")) {
			response=sp;
		}else if(selectedBenchMark.equals("NASDAQ")) {
			response=nasdaq;
		}else {
			response=dowJ;
		}
		return response;
	}
	
	public double calculateCurrentValue(JSONObject performanceObj) {
		DecimalFormat df2 = new DecimalFormat("#.##");
		double initialValue = Double.parseDouble(performanceObj.getString("initialValue"));
		double netDeposit = Double.parseDouble(performanceObj.getString("netDeposit"));
		double pl =Double.parseDouble(performanceObj.getString("pl"));
		double feesAndTax = Double.parseDouble(performanceObj.getString("feesAndTax"));
		double currentValue =initialValue + netDeposit + pl + feesAndTax;
		return Double.parseDouble(df2.format(currentValue));
		}
	
	
	public String getPreviousMonth(String fromDate) throws ParseException{
		DateFormat  sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		Date convertedDate = new Date();
		convertedDate=sdf.parse(fromDate);
		cal.setTime(convertedDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DATE, -1);
	    String startDate = sdf.format(cal.getTime());
	    return startDate;
	}
	
	public JSONArray sortArray(JSONArray array,String sortBy, String sortType) {
		JSONArray sortedJSON = new JSONArray();
		JSONArray sortedJSONArray = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		ArrayList<String> datesList = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			jsonValues.add(array.getJSONObject(i));
			datesList.add(array.getJSONObject(i).getString("dateTime"));
		}
		if (sortBy.equals("") || sortBy.equalsIgnoreCase(TemenosConstants.DATE_TIME)) {
			Collections.sort(datesList);
			for (String dates : datesList) {
				for (int i = 0; i < array.length(); i++) {
					if (jsonValues.get(i).get("dateTime").equals(dates)) {
						sortedJSON.put(jsonValues.get(i));
					}
				}
			}
			if (sortType != null && sortType.equalsIgnoreCase(TemenosConstants.DESCENDING)) {
				for (int i = sortedJSON.length()-1; i >= 0; i--) {
					sortedJSONArray.put(sortedJSON.get(i));
				}

			} else {
				for (int i = 0; i < sortedJSON.length(); i++) {
					sortedJSONArray.put(sortedJSON.get(i));
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
