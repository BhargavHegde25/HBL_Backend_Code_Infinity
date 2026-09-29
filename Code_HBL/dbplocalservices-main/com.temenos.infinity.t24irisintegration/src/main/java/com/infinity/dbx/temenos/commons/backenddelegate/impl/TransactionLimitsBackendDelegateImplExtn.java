package com.infinity.dbx.temenos.commons.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.commons.backenddelegate.impl.TransactionLimitsBackendDelegateImpl;
import com.temenos.dbx.product.forexservices.resource.api.ForexResource;

public class TransactionLimitsBackendDelegateImplExtn extends TransactionLimitsBackendDelegateImpl{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	@Deprecated
	public Double fetchConvertedAmount(String currency, String amount, DataControllerRequest request) {
		try {
			String response = getConversionRate(currency, request);
			JSONObject jsonRsponse = new JSONObject(response);
		    JSONArray arr = jsonRsponse.getJSONArray("rates");
			if(arr != null) {
				JSONObject json = (arr.getJSONObject(0));
				String midRate = json.optString("midRevalRate");
				String quotationCode = json.optString("quotationCode");
				double amountValue = Double.parseDouble(amount);
				double midRateValue = Double.parseDouble(midRate);
				
				if(StringUtils.isBlank(quotationCode)) {
					return amountValue/midRateValue;
				}else if(quotationCode.equals("0")) {
					return amountValue * midRateValue;
				}
			}
		}
		catch (Exception e) {
			alert.prepareError("Failed to fetch converted amount: ", e).log();
			return null;
		}
		return null;
	}
	@Override
	public Double fetchNewConvertedAmount(Double amount, String fromCurrency, String toCurrency, DataControllerRequest dcr){
		try {
			Double fromCurrencyRatio = fetchConversionRatio(fromCurrency, dcr);
			Double toCurrencyRatio = fetchConversionRatio(toCurrency, dcr);
			alert.prepareError("HBL::fetchNewConvertedAmount:fromCurrency:"+fromCurrency+",CurrencyRatio:" + fromCurrencyRatio).log();
			alert.prepareError("HBL::fetchNewConvertedAmount:toCurrency:"+toCurrency+",CurrencyRatio:" + toCurrencyRatio).log();
			if(fromCurrencyRatio == null || toCurrencyRatio == null){
				return amount;
			}
			else{
				return (toCurrencyRatio/fromCurrencyRatio) * amount;
			}
		}
		catch (Exception e) {
			alert.prepareError("Failed to fetch converted amount: ", e).log();
			return null;
		}
	}
	
	private Double fetchConversionRatio(String currency, DataControllerRequest dcr){
		try {
			String response = getConversionRate(currency, dcr);
			JSONObject jsonRsponse = new JSONObject(response);
			JSONArray arr = jsonRsponse.getJSONArray("rates");
			if(arr != null) {
				JSONObject json = arr.getJSONObject(0);
				String midRate = json.optString("midRevalRate");
				String quotationCode = json.optString("quotationCode");
				double midRateValue = Double.parseDouble(midRate);

				if(StringUtils.isBlank(quotationCode)) {
					return (1.0/midRateValue);
				}else if(quotationCode.equals("0")) {
					return midRateValue;
				}
			}
		}
		catch (Exception e) {
			alert.prepareError("Failed to fetch converted amount: ", e).log();
			return null;
		}
		return null;
	}
	
	private Double fetchConversionRatio_New(String currency, DataControllerRequest request){
		Double convertionRation = null;
		try{
			String market="10 1";
			Map<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("baseCurrencyCode", "NPR");
			inputParams.put("quoteCurrencyCode", currency);
			inputParams.put("market", market);
			inputParams.put("companyCode", "NP0010001");
			String methodId = "fetchCurrencyRates";
			Object [] inputArray = new Object[2];
			inputArray[0] = inputParams;
			inputArray[1] = inputParams;
			DataControllerResponse response = null;
			Result result = fetchCurrencyRates(methodId, inputArray, request, response);
			String dbpErrMsg = result.getParamValueByName("dbpErrMsg");
			String dbpErrCode = result.getParamValueByName("dbpErrCode");
			alert.prepareError("HBL::fetchCurrencyRates:dbpErrMsg:" + dbpErrMsg).log();;
			if (StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
				String responseStr = ResultToJSON.convert(result);
				JSONObject responseObj = new JSONObject(responseStr);
				alert.prepareError("HBL::fetchCurrencyRates:responseObj:" + responseObj).log();
				String currenceCode = responseObj.optString("code");
				String currenceName = responseObj.optString("name");
				JSONObject ratesObj = getCurrencyMarketValues(responseObj);
				String buyRate = ratesObj.optString("buyRate");
				convertionRation=Double.parseDouble(buyRate);
				//convertedAmount = calculateConvertedAmount(amount, buyRate,  transactionCurrency, baseCurrency, request);
			}
		}catch(Exception e){
			alert.prepareError("HBL::Exception Occured at TransactionLimitsBusinessDelegateImplExtn:getConvertedAmount:" + e).log();;
			return null;
		}
		return convertionRation;
	}

	private String getConversionRate(String currency, DataControllerRequest request) {
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("currency",currency);
			alert.prepareError("INPUT to BACKEND: "+new JSONObject(requestParameters).toString()).log();
			return DBPServiceExecutorBuilder.builder().
					withServiceId(TemenosConstants.SERVICE_T24IS_FOREX_DETAILS).
					withObjectId(null).
					withOperationId(TemenosConstants.OP_GET_CONVERTEDRATE).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at  get converted value: ", e).log();
			return "{\"errormsg\":\""+e.getMessage()+"\"}";
		}
	}
	public JSONObject getCurrencyMarketValues(JSONObject response) {
		JSONObject currencyMarketObj = null;
		JSONArray currencyMarkets = response.optJSONArray("markets");
		if (currencyMarkets != null && currencyMarkets.length() > 0) {
			for (int i = 0; i < currencyMarkets.length(); i++) {
				currencyMarketObj = currencyMarkets.getJSONObject(i);
				String currencyMarket = currencyMarketObj.optString("market");
				if (StringUtils.isNotBlank(currencyMarket) && currencyMarket.equalsIgnoreCase("TT")) {
					return currencyMarketObj;
				}
			}
		}
		return currencyMarketObj;
	}
	public Result fetchCurrencyRates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = null;
		try {
			ForexResource forexResource = DBPAPIAbstractFactoryImpl.getResource(ForexResource.class);
			result = forexResource.fetchCurrencyRates(methodID, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking fetchCurrencyRates: "+ e).log();;
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}
}
