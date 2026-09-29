package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.CurrencyBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class CurrencyBackendDelegateImpl implements CurrencyBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result getMarketRates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		String currencyPairs = null,portfolioId = null;
		if (inputParams.get(TemenosConstants.CURRENCYPAIRS) != null
				&& inputParams.get(TemenosConstants.CURRENCYPAIRS).toString().trim().length() > 0) {
			currencyPairs = inputParams.get(TemenosConstants.CURRENCYPAIRS).toString();
			if (currencyPairs.substring(0, 3).equals(currencyPairs.substring(3))) {
				alert.prepareError("Error:Invalid input").log();
				JSONObject result = new JSONObject();
				result.put("status", "Failure");
				result.put("error", "Invalid Input! FROM and TO Currency cannot be the same.");
				return Utilities.constructResultFromJSONObject(result);
			}
			inputMap.put(TemenosConstants.CURRENCYPAIRS, currencyPairs);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.CURRENCYPAIRS);
		}
		if (inputParams.get(TemenosConstants.PORTFOLIOID) != null
				&& inputParams.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
		}

		String returnResponse = null;
		String serviceName = ServiceId.WEALTHORCHESTRATION;
		String operationName = OperationName.GET_MARKET_RATES;

		try {
			returnResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resultJSON = new JSONObject(returnResponse);
			return Utilities.constructResultFromJSONObject(resultJSON);

		} catch (Exception e) {
			alert.prepareError("Error while invoking Transact - " + WealthAPIServices.GET_MARKET_RATES.getOperationName()
					+ "  : " + e).log();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result createForexOrders(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> inputJSON = new HashMap<>();

		String buyCurrency = null;
		if (inputParams.get(TemenosConstants.BUYCURRENCY) != null
				&& inputParams.get(TemenosConstants.BUYCURRENCY).toString().trim().length() > 0) {
			buyCurrency = inputParams.get(TemenosConstants.BUYCURRENCY).toString();
			inputJSON.put(TemenosConstants.BUYCURRENCY, buyCurrency);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.BUYCURRENCY);
		}
		String sellCurrency = null;
		if (inputParams.get(TemenosConstants.SELLCURRENCY) != null
				&& inputParams.get(TemenosConstants.SELLCURRENCY).toString().trim().length() > 0) {
			sellCurrency = inputParams.get(TemenosConstants.SELLCURRENCY).toString();
			inputJSON.put(TemenosConstants.SELLCURRENCY, sellCurrency);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.SELLCURRENCY);
		}

		if (buyCurrency.equalsIgnoreCase(sellCurrency)) {
			return PortfolioWealthUtils.validateMandatoryFields("Invalid Input! FROM and TO Currency cannot be the same.");
		}

		String buyAmount = null;
		if (inputParams.get(TemenosConstants.BUYAMOUNT) != null
				&& inputParams.get(TemenosConstants.BUYAMOUNT).toString().trim().length() > 0) {
			buyAmount = inputParams.get(TemenosConstants.BUYAMOUNT).toString();
			inputJSON.put(TemenosConstants.BUYAMOUNT, buyAmount);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.BUYAMOUNT);
		}
		String sellAmount = null;
		if (inputParams.get(TemenosConstants.SELLAMOUNT) != null
				&& inputParams.get(TemenosConstants.SELLAMOUNT).toString().trim().length() > 0) {
			sellAmount = inputParams.get(TemenosConstants.SELLAMOUNT).toString();
			inputJSON.put(TemenosConstants.SELLAMOUNT, sellAmount);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.SELLAMOUNT);
		}
		String buyAccount = null;
		if (inputParams.get(TemenosConstants.BUYACCOUNT) != null
				&& inputParams.get(TemenosConstants.BUYACCOUNT).toString().trim().length() > 0) {
			buyAccount = inputParams.get(TemenosConstants.BUYACCOUNT).toString();
			inputJSON.put(TemenosConstants.BUYACCOUNT, buyAccount);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.BUYACCOUNT);
		}
		String sellAccount = null;
		if (inputParams.get(TemenosConstants.SELLACCOUNT) != null
				&& inputParams.get(TemenosConstants.SELLACCOUNT).toString().trim().length() > 0) {
			sellAccount = inputParams.get(TemenosConstants.SELLACCOUNT).toString();
			inputJSON.put(TemenosConstants.SELLACCOUNT, sellAccount);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.SELLACCOUNT);
		}
		String portfolioId;
		if (inputParams.get(TemenosConstants.PORTFOLIOID) != null) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputJSON.put(TemenosConstants.PORTFOLIOID, portfolioId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.PORTFOLIOID);
		}
		String validate_only;
		if (inputParams.get(TemenosConstants.VALIDATEONLY) != null) {
			validate_only = inputParams.get(TemenosConstants.VALIDATEONLY).toString();
			inputJSON.put(TemenosConstants.VALIDATEONLY, validate_only);
		} 

		if (Float.parseFloat(buyAmount) < 0 || Float.parseFloat(sellAmount) < 0) {
			alert.prepareError("Error:Invalid input").log();
			JSONObject resultJSON = new JSONObject();
			resultJSON.put("status", "Failure");
			resultJSON.put("error", "Enter valid Amount! Buy Amount and Sell Amount cannot be negative");
			return Utilities.constructResultFromJSONObject(resultJSON);

		} else if (Float.parseFloat(buyAmount) > 999999999.99 || Float.parseFloat(sellAmount) > 999999999.99) {
			alert.prepareError("Error:Invalid input").log();
			JSONObject resultJSON = new JSONObject();
			resultJSON.put("status", "Failure");
			resultJSON.put("error",
					"Enter valid Amount! Buy Amount and Sell Amount cannot be greater than 999999999.99");
			return Utilities.constructResultFromJSONObject(resultJSON);
		}

		List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);

		if (allportfoliosList.contains(portfolioId)) {
			String returnResponse = null;
			String serviceName = ServiceId.WEALTHORCHESTRATION;
			String operationName = OperationName.CREATE_FOREX_ORDERS;

			try {
				returnResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(inputJSON)
						.withRequestHeaders(headersMap).withDataControllerRequest(request).build().getResponse();
				JSONObject resultJSON = new JSONObject(returnResponse);
				return Utilities.constructResultFromJSONObject(resultJSON);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Transact - " + WealthAPIServices.CREATE_FOREX_ORDERS.getOperationName()
						+ "  : " + e).log();
				return null;
			}
		} else {

			return PortfolioWealthUtils.UnauthorizedAccess();

		}
	}

//	private String[] getMonths(String[] arr){
//		String[] resultArray = new String[48];
//		Calendar startDate = Calendar.getInstance();
//		Calendar endDate = Calendar.getInstance();
//		
//		if(arr[0].equalsIgnoreCase("Jan")){
//			
//		}else{
//			startDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 1);
//			startDate.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1);
//		}
//		startDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//	    for (int i = 0; i < 47; i++) {
//	    	startDate.add(Calendar.DATE, 7);
//	    	resultArray[i] = df.format(startDate.getTime());
//	    }
//	    resultArray[47] = df.format(endDate.getTime());
//	    
//	    return resultArray;
//	}

	public static long getUniqueNumber() {
		SecureRandom rnd = new SecureRandom();
		long generatedValue;
		generatedValue = 10000000L + rnd.nextInt(90000);
		return generatedValue;
	}
}
