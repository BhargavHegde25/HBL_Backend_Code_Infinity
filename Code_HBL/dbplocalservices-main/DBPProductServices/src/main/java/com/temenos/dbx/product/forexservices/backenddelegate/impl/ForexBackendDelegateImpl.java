package com.temenos.dbx.product.forexservices.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.forexservices.backenddelegate.api.ForexBackendDelegate;
import com.temenos.dbx.product.forexservices.dto.CurrencyDTO;

public class ForexBackendDelegateImpl implements ForexBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public CurrencyDTO getBaseCurrencyFromBackend(DataControllerRequest dcRequest, String methodID, String market, String companyCode, String countryCode) {
		
		if( countryCode == null ) {
			return null;
		}
		
		String serviceName = ServiceId.DBPNPFOREXSERVICES;
		String operationName = OperationName.FETCH_BASE_CURRENCY;

		Map<String, Object> requestParameters = new HashMap<String, Object>();				
	    requestParameters.put("CountryCode", countryCode);
		
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject jsonRsponse = new JSONObject(response);
			CurrencyDTO baseCurrency = JSONUtils.parse(jsonRsponse.toString(), CurrencyDTO.class);
			
			return baseCurrency;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch base currency", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getBaseCurrencyFromBackend: ", e).log();
			return null;
		}		
	}
	
	@Override
	public List<CurrencyDTO> getCurrencyListFromBackend() {
		
		String serviceName = ServiceId.DBPNPFOREXSERVICES;
		String operationName = OperationName.FETCH_CURRENCY_LIST;
		
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(null)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			
			List<CurrencyDTO> currencies = JSONUtils.parseAsList(jsonArray.toString(), CurrencyDTO.class);			
			return currencies;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch base currency", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getBaseCurrencyFromBackend: ", e).log();
			return null;
		}		
	}
	
	@Override
	public CurrencyDTO getCurrencyRatesFromBackend(DataControllerRequest dcRequest, String methodID, String baseCurrencyCode, String quoteCurrencyCode, String market, String companyCode) {
		
		String serviceName = ServiceId.DBPNPFOREXSERVICES;
		String operationName = OperationName.FETCH_FOREX_RATES;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();				
	    requestParameters.put("baseCurrencyCode", baseCurrencyCode);
	    requestParameters.put("quoteCurrencyCode", quoteCurrencyCode);
	    
	    if(market != null) {
	    	requestParameters.put("market", market);
	    }
	    	    
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();

			JSONObject jsonRsponse = new JSONObject(response);
			CurrencyDTO currency = JSONUtils.parse(jsonRsponse.toString(), CurrencyDTO.class);
			
			return currency;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch currency rates", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getCurrencyRatesFromBackend: ", e).log();
			return null;
		}		
	}	

	@Override
	public List<CurrencyDTO> getRecentOrPopularCurrencyRatesFromBackend(DataControllerRequest dcRequest, String methodID, String baseCurrencyCodes, String quoteCurrencyCodes, String markets, String companyCode, int loopCount) {
		
		String serviceName = ServiceId.DBPFOREXORCH;
		String operationName = OperationName.FETCH_DASHBOARD_FOREX_RATES;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();				
	    requestParameters.put("baseCurrencyCode", baseCurrencyCodes);
	    requestParameters.put("quoteCurrencyCode", quoteCurrencyCodes);
	    
	    if(markets != null && !markets.equals("")) {
	    	requestParameters.put("market", markets);
	    }
	    requestParameters.put("loop_count", loopCount);	    
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();

			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			
			List<CurrencyDTO> currencies = JSONUtils.parseAsList(jsonArray.toString(), CurrencyDTO.class);			
			return currencies;
            
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch recent or popular currency rates", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getRecentOrPopularCurrencyRatesFromBackend: ", e).log();
			return null;
		}		
	}	
}
