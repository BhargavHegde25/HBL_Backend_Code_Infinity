package com.kony.adminconsole.service.customer.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.service.customer.backenddelegate.api.InfinityCustomerBackendDelegate;
import com.kony.adminconsole.service.customer.businessdelegate.api.InfinityCustomerBusinessDelegate;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class InfinityCustomerBusinessDelegateImpl implements InfinityCustomerBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	
	@Override
	public JSONObject getInfinityAccounts(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		/*
		 * Get The Base Currency
		 */
		
		String baseCurrency = StringUtils.EMPTY;
		JSONObject errorJsonObject = null;
		try {
			String operationName = OperationName.DB_APPLICATION_GET;
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.CRUDLAYER).
					withObjectId(null).
					withOperationId(operationName).
					build().getResponse();
			
			JSONObject responseObj = CommonUtilities.getStringAsJSONObject(response);
			if (responseObj != null && responseObj.has(FabricConstants.OPSTATUS) 
					&& responseObj.getInt(FabricConstants.OPSTATUS) == 0
					&& responseObj.has("application")) {
				JSONObject jsonObject = responseObj.optJSONArray("application").optJSONObject(0);
				baseCurrency = jsonObject.optString("currencyCode");
			}else{
				alert.prepareError("Failed to fetch base currency response: "+responseObj).log();
				errorJsonObject = new JSONObject();
				errorJsonObject.append("dbpErrCode", ErrorCodeEnum.ERR_22132.getErrorCodeAsString());
				errorJsonObject.append("dbpErrMsg", ErrorCodeEnum.ERR_22132.getMessage());
				
				return errorJsonObject;
			}
		}
		catch (Exception exp) {
			alert.prepareError("Exception while fetching base currency: "+ exp).log();
			errorJsonObject = new JSONObject();
			errorJsonObject.append("dbpErrCode", ErrorCodeEnum.ERR_22132.getErrorCodeAsString());
			errorJsonObject.append("dbpErrMsg", ErrorCodeEnum.ERR_22132.getMessage());
			
			return errorJsonObject;
		}

		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETINFINITYACCOUNTS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        
        JSONObject serviceResponseModified = CommonUtilities.getStringAsJSONObject(serviceResponse);
        
        InfinityCustomerBackendDelegate infinityCustomerBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InfinityCustomerBackendDelegate.class);
        
        if(null != serviceResponseModified && serviceResponseModified.has("Accounts")) {
        	JSONArray respJsonArray = serviceResponseModified.optJSONArray("Accounts");
        	
        	Map<String, Object> parametersMap = null;
        	double totalBalance = 0.00;
        	if(null != respJsonArray && respJsonArray.length() > 0) {
        		
        		for(int i=0; i<respJsonArray.length(); i++) {
        			JSONObject jsonObject = respJsonArray.optJSONObject(i);
        			parametersMap = new HashMap<>();
        			String currencyCode = jsonObject.optString("currencyCode");
        			String currentBalance = jsonObject.optString("currentBalance");
        			if(currencyCode.equalsIgnoreCase(baseCurrency) || currentBalance.equalsIgnoreCase("0")) {
        				jsonObject.put("convertedAmount", currentBalance);
        				totalBalance = Double.parseDouble(currentBalance) > 0 ? 
        						totalBalance + Double.parseDouble(currentBalance) : totalBalance;
        				continue;
        			}
        			
        			parametersMap.put("fromCurrency", currencyCode);
        			parametersMap.put("amount", currentBalance);
        			parametersMap.put("toCurrency", baseCurrency);
        			
        			JSONObject convertedAmountApiResp = infinityCustomerBackendDelegate.getConvertedAmount(
        					parametersMap, null);
        			if(null != convertedAmountApiResp && convertedAmountApiResp.has("convertedAmount") ) {
        				String convertedAmount = convertedAmountApiResp.optString("convertedAmount");
        				jsonObject.put("convertedAmount", convertedAmount);
        				totalBalance = Double.parseDouble(convertedAmount) > 0 ? 
        						totalBalance + Double.parseDouble(convertedAmount) : totalBalance;
        			}
        			
        		}
        	}
        	serviceResponseModified.put("totalBalance", String.valueOf(totalBalance));
        	
        } else if(null != serviceResponseModified && serviceResponseModified.has("contracts")) {
        	
        	JSONArray contractArray = serviceResponseModified.optJSONArray("contracts");
        	
        	Map<String, Object> parametersMap = null;
			
        	if(null != contractArray && contractArray.length() > 0) {
        		
        		for(int i=0; i<contractArray.length(); i++) {
        			JSONObject contractArrayObject = contractArray.optJSONObject(i);
        			if(null !=contractArrayObject && contractArrayObject.has("contractCustomers") ) {
        				JSONArray contractCustomers = contractArrayObject.optJSONArray("contractCustomers");
        				if(null != contractCustomers && contractCustomers.length() > 0) {
        					for(int j=0; j<contractCustomers.length(); j++) {
	        					JSONObject contractCustomersObject = contractCustomers.optJSONObject(j);
	        					double totalBalance = 0.00;
	        					if(null !=contractCustomersObject && contractCustomersObject.has("coreCustomerAccounts") ) {
	        						
	        						JSONArray coreCustomerAccountsArray = contractCustomersObject.optJSONArray("coreCustomerAccounts");
	        						if(null != coreCustomerAccountsArray && coreCustomerAccountsArray.length() > 0) {
	        							
	        							for(int k=0; k<coreCustomerAccountsArray.length(); k++) {
	        								JSONObject coreCustomerAccountsObject = coreCustomerAccountsArray.optJSONObject(k);
	        								parametersMap = new HashMap<>();
	        			        			String currencyCode = coreCustomerAccountsObject.optString("currencyCode");
	        			        			String currentBalance = coreCustomerAccountsObject.optString("currentBalance");
	        			        			if(currencyCode.equalsIgnoreCase(baseCurrency) || currentBalance.equalsIgnoreCase("0")) {
	        			        				coreCustomerAccountsObject.put("convertedAmount", currentBalance);
	        			        				
	        			        				totalBalance = Double.parseDouble(currentBalance) > 0 ? 
	        			        						totalBalance + Double.parseDouble(currentBalance) : totalBalance;
	        			        						
	        			        				continue;
	        			        			}
	        			        			
	        			        			parametersMap.put("fromCurrency", currencyCode);
	        			        			parametersMap.put("amount", currentBalance);
	        			        			parametersMap.put("toCurrency", baseCurrency);
	        			        			JSONObject convertedAmountApiResp = infinityCustomerBackendDelegate.getConvertedAmount(
	        			        					parametersMap, null);
	        			        			if(null != convertedAmountApiResp && convertedAmountApiResp.has("convertedAmount") ) {
	        			        				String convertedAmount = convertedAmountApiResp.optString("convertedAmount");
	        			        				coreCustomerAccountsObject.put("convertedAmount", convertedAmount);
	        			        				
	        			        				totalBalance = Double.parseDouble(convertedAmount) > 0 ? 
	        			        						totalBalance + Double.parseDouble(convertedAmount) : totalBalance;
	        			        			}
	        			        			
	        							}
	        							
	        						}
	        						
	        					}

	        					contractCustomersObject.put("totalBalance", String.valueOf(totalBalance));
	        					
        					}
        					
        				}
        				
        			}
        			
        		}
        		
        	}
        	
        }
        
        return serviceResponseModified;
	}

}
