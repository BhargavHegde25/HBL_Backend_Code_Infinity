package com.bct.javaservices;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.commons.backenddelegate.api.TransactionLimitsBackendDelegate;
import com.temenos.dbx.product.forexservices.resource.api.ForexResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class GetConvertedAmount implements JavaService2 {
	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(GetConvertedAmount.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static String midRevalRate="";
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String fromAccountCurrency = inputParams.get("fromAccountCurrency")!=null?inputParams.get("fromAccountCurrency").toString():"";
		String transactionCurrency = inputParams.get("transactionCurrency")!=null?inputParams.get("transactionCurrency").toString():"";
		String amountString = inputParams.get("transactionAmount")!=null?inputParams.get("transactionAmount").toString():"";
		double transactionAmount = Double.parseDouble(amountString);
		String baseCurrency = EnvironmentConfigurationsHandler.getServerProperty("HBL_BASE_CURRENCY");
		Double convertedAmount = 0.0;
		String market="10 1"; 
		
			
		if (StringUtils.isEmpty(transactionCurrency))
			transactionCurrency = baseCurrency;
		if (StringUtils.isEmpty(fromAccountCurrency))
			fromAccountCurrency = transactionCurrency;

		if (!fromAccountCurrency.equalsIgnoreCase(baseCurrency)) {
			try {
				inputParams.put("baseCurrencyCode", baseCurrency);
				inputParams.put("quoteCurrencyCode", fromAccountCurrency);
				inputParams.put("market", market);
				inputParams.put("companyCode", "NP0010001");
				methodId = "fetchCurrencyRates";
				inputArray[1] = new Object();
				inputArray[1] = inputParams;
				result = fetchCurrencyRates(methodId, inputArray, request, response);
				LOG.debug("HBL::fetchCurrencyRates:result:" + ResultToJSON.convert(result));
				String dbpErrMsg = result.getParamValueByName("dbpErrMsg");
				String dbpErrCode = result.getParamValueByName("dbpErrCode");
				LOG.debug("HBL::fetchCurrencyRates:dbpErrMsg:" + dbpErrMsg);
				if (StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
					String responseStr = ResultToJSON.convert(result);
					JSONObject responseObj = new JSONObject(responseStr);
					String currenceCode = responseObj.optString("code");
					String currenceName = responseObj.optString("name");
					// convertedAmount = fetchNewConvertedAmount(transactionAmount,
					// transactionCurrency, baseCurrency, request);
					// convertedAmount = (double) Math.round(convertedAmount * 100.0)/100.0;
					// result.addParam(new Param("convertedAmount", convertedAmount.toString()));
					// result.addParam(new Param("midRevalRate", midRevalRate));
					JSONObject ratesObj = getCurrencyMarketValues(responseObj);
					LOG.debug("HBL::GetConvertedAmount::ratesObj:" + ratesObj);
					if (ratesObj != null && !ratesObj.isEmpty()) {
						for (String key : ratesObj.keySet()) {
							result.addParam(new Param(key, ratesObj.optString(key)));
						}
					}
					String buyRate = ratesObj.optString("buyRate");
					convertedAmount = calculateConvertedAmount(transactionAmount, buyRate, fromAccountCurrency, transactionCurrency, baseCurrency, request);
					//LOG.debug("HBL::convertedAmount:" + convertedAmount.toString());
					if (convertedAmount == null) {
						LOG.error("Failed to calculateConvertedAmount:", convertedAmount);
						ErrorCodeEnum.ERR_27016.setErrorCode(result);
						result.addParam(new Param("success", "false"));
					} else {
						if(transactionCurrency.equalsIgnoreCase(fromAccountCurrency)) {
							currenceCode=transactionCurrency;
						}else {
							currenceCode=fromAccountCurrency;
						}
						result.addParam(new Param("convertedAmount", convertedAmount.toString()));
						result.addParam(new Param("currenceCode", currenceCode));
						result.addParam(new Param("currenceName", currenceName));
						result.addHttpStatusCodeParam("200");
						result.addParam(new Param("success", "true"));
					}
				} else {
					result.addParam(new Param("success", "false"));
				}
			} catch (Exception e) {
				LOG.error("Failed to fetch converted amount", e);
				ErrorCodeEnum.ERR_27016.setErrorCode(result);
				result.addParam(new Param("success", "false"));
				result.addParam(new Param("message", e.getMessage()));
				result.addHttpStatusCodeParam("500");
				return result;
			}
		} else if (fromAccountCurrency.equalsIgnoreCase("NPR") && transactionCurrency.equalsIgnoreCase("USD")) {
			inputParams.put("baseCurrencyCode", baseCurrency);
			inputParams.put("quoteCurrencyCode", transactionCurrency);
			inputParams.put("market", market);
			inputParams.put("companyCode", "NP0010001");
			methodId = "fetchCurrencyRates";
			inputArray[1] = new Object();
			inputArray[1] = inputParams;
			result = fetchCurrencyRates(methodId, inputArray, request, response);
			LOG.debug("HBL::fetchCurrencyRates:result:" + ResultToJSON.convert(result));
			String dbpErrMsg = result.getParamValueByName("dbpErrMsg");
			String dbpErrCode = result.getParamValueByName("dbpErrCode");
			LOG.debug("HBL::fetchCurrencyRates:dbpErrMsg:" + dbpErrMsg);

			if (StringUtils.isBlank(dbpErrCode) && StringUtils.isBlank(dbpErrMsg)) {
				String responseStr = ResultToJSON.convert(result);
				JSONObject responseObj = new JSONObject(responseStr);
				String currenceCode = responseObj.optString("code");
				String currenceName = responseObj.optString("name");
				JSONObject ratesObj = getCurrencyMarketValues(responseObj);
				LOG.debug("HBL::GetConvertedAmount::ratesObj:" + ratesObj);
				if (ratesObj != null && !ratesObj.isEmpty()) {
					for (String key : ratesObj.keySet()) {
						result.addParam(new Param(key, ratesObj.optString(key)));
					}
				}
				String buyRate = ratesObj.optString("buyRate");
				convertedAmount = convertNprToUsd(transactionAmount, Double.parseDouble(buyRate));
				if (convertedAmount == null) {
					LOG.error("Failed to calculateConvertedAmount:", convertedAmount);
					ErrorCodeEnum.ERR_27016.setErrorCode(result);
					result.addParam(new Param("success", "false"));
				} else {
					if (transactionCurrency.equalsIgnoreCase(fromAccountCurrency)) {
						currenceCode = transactionCurrency;
					} else {
						currenceCode = fromAccountCurrency;
					}
					result.addParam(new Param("convertedAmount", convertedAmount.toString()));
					result.addParam(new Param("currenceCode", currenceCode));
					result.addParam(new Param("currenceName", currenceName));
					result.addHttpStatusCodeParam("200");
					result.addParam(new Param("success", "true"));
				}
			} else {
				result.addParam(new Param("success", "false"));
			}

		}else {
		
			result.addParam(new Param("message", "from account currency and transaction currency should not be same."));
			result.addParam(new Param("success", "false"));
			return result;
		}
		return result;
	}
	/*private Double getNewConvertedAmount(Double amount, String transactionCurrency, String baseCurrency, DataControllerRequest request){
		try{
			TransactionLimitsBackendDelegate transactionLimitsBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(TransactionLimitsBackendDelegate.class);
			return transactionLimitsBackendDelegate.fetchNewConvertedAmount(amount, transactionCurrency, baseCurrency, request);
		}catch(Exception e){
			return null;
		}
	}*/
	
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
	public Double calculateConvertedAmount(Double amount, String convertionRate, String fromAccountCurrency, String transactionCurrency, String baseCurrency, DataControllerRequest dcr){
		Double convertedAmount = null;
		LOG.debug("calculateConvertedAmount ###"+ amount.toString());
		try {
			if(amount == null || StringUtils.isBlank(convertionRate)){
				return null;
			}
			else{
				//Converting fromAccountCurrency to Base Currency amount
				if(transactionCurrency.equalsIgnoreCase(fromAccountCurrency)) {
				Double exchangeRate = Double.parseDouble(convertionRate);
				convertedAmount = exchangeRate * amount;
				convertedAmount= (double) Math.round(convertedAmount * 100.0)/100.0;
				}
				else {
					//Converting Base Currency amount to fromAccountCurrency 
					Double exchangeRate = Double.parseDouble(convertionRate);
					convertedAmount = amount/exchangeRate;
					convertedAmount= (double) Math.round(convertedAmount * 100.0)/100.0;
					}
				LOG.debug("calculateConvertedAmount convertedAmount ###"+ convertedAmount.toString());
			}
		}
		catch (Exception e) {
			alert.prepareError("Failed to fetch converted amount: ", e).log();
			return null;
		}
		return convertedAmount;
	}
	
	public static Double convertNprToUsd(Double nprAmount, Double rate) {
		Double convertedAmount = null;
		LOG.debug("calculateConvertedAmount ###" + nprAmount.toString());
		try {
			if (nprAmount == null || StringUtils.isBlank(rate.toString())) {
				return null;
			} else {
				convertedAmount = nprAmount / rate;
				BigDecimal bd = new BigDecimal(convertedAmount);
		        bd = bd.setScale(4, RoundingMode.HALF_UP); 
		        convertedAmount = bd.doubleValue();
			}
		} catch (Exception e) {
			alert.prepareError("Failed to fetch converted amount: ", e).log();
			return null;
		}
		return convertedAmount;
	}
	
	public Double fetchNewConvertedAmount(Double amount, String toCurrency, String baseCurrency, DataControllerRequest dcr){
		try {
			Double toCurrencyRatio = fetchConversionRatio(toCurrency, dcr);
			Double fromCurrencyRatio = fetchConversionRatio(baseCurrency, dcr);
			midRevalRate=String.valueOf(toCurrencyRatio);
			if(fromCurrencyRatio == null || toCurrencyRatio == null){
				return amount;
			}
			else{
				return (fromCurrencyRatio/toCurrencyRatio) * amount;
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
				LOG.debug("HBL::fetchConversionRatio:currency:"+currency+":midRateValue:"+midRateValue);
				LOG.debug("HBL::fetchConversionRatio:currency:"+currency+":quotationCode:"+quotationCode);
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
	public Result fetchCurrencyRates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = null;
		try {
			ForexResource forexResource = DBPAPIAbstractFactoryImpl.getResource(ForexResource.class);
			result = forexResource.fetchCurrencyRates(methodID, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking fetchCurrencyRates: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}
	
}
