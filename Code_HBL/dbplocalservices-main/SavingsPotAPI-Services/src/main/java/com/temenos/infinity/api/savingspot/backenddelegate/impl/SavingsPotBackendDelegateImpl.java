package com.temenos.infinity.api.savingspot.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.temenos.infinity.api.savingspot.config.ServerConfigurations;
import com.temenos.infinity.api.savingspot.constants.MSCertificateConstants;
//import com.temenos.infinity.api.savingspot.constant.MSCertificateConstants;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.savingspot.backenddelegate.api.SavingsPotBackendDelegate;
import com.temenos.infinity.api.savingspot.config.SavingsPotAPIServices;
import com.temenos.infinity.api.savingspot.constants.TemenosConstants;
import com.temenos.infinity.api.savingspot.dto.SavingsPotCategoriesDTO;
import com.temenos.infinity.api.savingspot.dto.SavingsPotDTO;

public class SavingsPotBackendDelegateImpl implements SavingsPotBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public List<SavingsPotDTO> getAllSavingsPot(String fundingAccountId, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(TemenosConstants.FUNDINGACCOUNTHOLDINGSID, fundingAccountId);
		String serviceResponse = null;
		List<SavingsPotDTO> savingsPotList = null;
		headersMap = generateSecurityHeadersForSavingsPot(headersMap);
		try {
			serviceResponse = Executor.invokeService(SavingsPotAPIServices.SAVINGSPOTJSON_GETALLSAVINGSPOT, inputMap,
					headersMap);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_GETALLSAVINGSPOT.getOperationName() + "  : " + e).log();
			return null;
		}
		try {
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
			if(serviceResponseJSON.has(TemenosConstants.ERRCODE) && serviceResponseJSON.get(TemenosConstants.ERRCODE).equals(TemenosConstants.NORECORDS_ERRCODE))
			{
			  return savingsPotList;
			}
			if (serviceResponseJSON.has(TemenosConstants.SAVINGSPOT)
					&& serviceResponseJSON.getJSONArray(TemenosConstants.SAVINGSPOT) != null) {
				for (int i = 0; i < serviceResponseJSON.getJSONArray(TemenosConstants.SAVINGSPOT).length(); i++) {
					serviceResponseJSON.getJSONArray(TemenosConstants.SAVINGSPOT).getJSONObject(i)
							.remove("extensionData");
				}
			}
			JSONArray savingsPotJSONArr = serviceResponseJSON.optJSONArray(TemenosConstants.SAVINGSPOT);
			if(savingsPotJSONArr!=null)
			{
				savingsPotList = JSONUtils.parseAsList(savingsPotJSONArr.toString(),
						SavingsPotDTO.class);
			}
			
			return savingsPotList;

		} catch (Exception e) {
			alert.prepareError("Service Error - while formatting response in Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_GETALLSAVINGSPOT.getOperationName() + "  : " + e).log();
			return null;
		}
	}

	@Override
	public List<SavingsPotCategoriesDTO> getCategories(String type,Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(TemenosConstants.TYPE, type);
		String serviceResponse = null;
		headersMap = generateSecurityHeadersForSavingsPot( headersMap);
		try {
			serviceResponse = Executor.invokeService(SavingsPotAPIServices.SAVINGSPOTJSON_GETCATEGORIES, inputMap,
					headersMap);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_GETCATEGORIES.getOperationName() + "  : " + e).log();
			return null;
		}
		try {
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
			JSONArray categoriesJSONArr = serviceResponseJSON.optJSONArray(TemenosConstants.REFERENCES);
			List<SavingsPotCategoriesDTO> categories = JSONUtils.parseAsList(categoriesJSONArr.toString(),
					SavingsPotCategoriesDTO.class);
			return categories;

		} catch (Exception e) {
			alert.prepareError("Service Error - while formatting response in Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_GETCATEGORIES.getOperationName() + "  : " + e).log();
			return null;
		}
	}

	@Override
	public SavingsPotDTO createSavingsPot(String fundingAccountHoldingId, String partyId, String productId,
			String potAccountId, String potName, String potType, String savingsType, String currency,
			String targetAmount, String targetPeriod, String frequency, String startDate, String endDate,
			String periodicContribution,Map<String, Object> headersMap) {
		SavingsPotDTO savingsPotDTO = null;
		String fundingAccountId = null;
		if (fundingAccountHoldingId.contains("-")) {
			fundingAccountId = fundingAccountHoldingId.substring(fundingAccountHoldingId.indexOf("-") + 1);
		} else {
			fundingAccountId = fundingAccountHoldingId;
		}
		Map<String, Object> inputJSON = new HashMap<>();
		inputJSON.put(TemenosConstants.FUNDINGACCOUNTHOLDINGSID, fundingAccountHoldingId);
		inputJSON.put(TemenosConstants.PARTYID, partyId);
		inputJSON.put(TemenosConstants.PRODUCTID, productId);
		inputJSON.put(TemenosConstants.POTACCOUNTID, potAccountId);
		inputJSON.put(TemenosConstants.POTNAME, potName);
		inputJSON.put(TemenosConstants.POTTYPE, potType);
		inputJSON.put(TemenosConstants.SAVINGSTYPE, savingsType);
		inputJSON.put(TemenosConstants.CURRENCY, currency);
		inputJSON.put(TemenosConstants.STARTDATE, startDate);
		inputJSON.put(TemenosConstants.ENDDATE, endDate);
		inputJSON.put(TemenosConstants.FREQUENCY, frequency);
		inputJSON.put(TemenosConstants.TARGETAMOUNT, targetAmount);
		inputJSON.put(TemenosConstants.TARGETPERIOD, targetPeriod);
		inputJSON.put(TemenosConstants.PERIODICCONTRIBUTION, periodicContribution);
		inputJSON.put(TemenosConstants.SAVINGSPOTID, "");
		inputJSON.put(TemenosConstants.PORTFOLIOID, "");
		inputJSON.put(TemenosConstants.FUNDINGACCOUNTID, fundingAccountId);
		// sending inputJSON as input to create savings Pot service will fetch us the
		// following response
		String serviceResponse = null;
		headersMap = generateSecurityHeadersForSavingsPot(headersMap);
		try {
			if(potType.equalsIgnoreCase(TemenosConstants.GOAL))
			{
				serviceResponse = Executor.invokeService(SavingsPotAPIServices.SAVINGSPOTJSON_CREATESAVINGSPOT, inputJSON,
						headersMap);
			}
			else if(potType.equalsIgnoreCase(TemenosConstants.BUDGET))
			{
				serviceResponse = Executor.invokeService(SavingsPotAPIServices.SAVINGSPOTJSON_CREATEBUDGET, inputJSON,
						headersMap);
			}
			
		} catch (Exception e) {
			alert.prepareError("Error while invoking Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_CREATESAVINGSPOT.getOperationName() + "  : " + e).log();
			return null;
		}
		try {
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
			savingsPotDTO = JSONUtils.parse(serviceResponseJSON.toString(), SavingsPotDTO.class);
		} catch (Exception e) {
			alert.prepareError("Service Error - while formatting response in Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_CREATESAVINGSPOT.getOperationName() + "  : " + e).log();
			return null;
		}
		return savingsPotDTO;
	}

	@Override
	public SavingsPotDTO closeSavingsPot(String savingsPotId,Map<String, Object> headersMap) {
		SavingsPotDTO savingsPotDTO = null;
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(TemenosConstants.SAVINGSPOTID, savingsPotId);
		String serviceResponse = null;
		headersMap = generateSecurityHeadersForSavingsPot(headersMap);
		try {
			serviceResponse = Executor.invokeService(SavingsPotAPIServices.SAVINGSPOTJSON_CLOSESAVINGSPOT, inputMap,
					headersMap);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_CLOSESAVINGSPOT.getOperationName() + "  : " + e).log();
			return null;
		}
		try {
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
			savingsPotDTO = JSONUtils.parse(serviceResponseJSON.toString(), SavingsPotDTO.class);
		} catch (Exception e) {
			alert.prepareError("Service Error - while formatting response in Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_CLOSESAVINGSPOT.getOperationName() + "  : " + e).log();
			return null;
		}
		return savingsPotDTO;
	}

	@Override
	public SavingsPotDTO updateSavingsPotBalance(String savingsPotId, String amount, String isCreditDebit,Map<String, Object> headersMap) {
		SavingsPotDTO savingsPotDTO = null;
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(TemenosConstants.SAVINGSPOTID, savingsPotId);
		inputMap.put(TemenosConstants.AMOUNT, amount);
		inputMap.put(TemenosConstants.ISCREDITDEBIT, isCreditDebit);
		String serviceResponse = null;
		headersMap = generateSecurityHeadersForSavingsPot(headersMap);
		try {
			serviceResponse = Executor.invokeService(SavingsPotAPIServices.SAVINGSPOTJSON_UPDATESAVINGSPOTBALANCE,
					inputMap, headersMap);
			
		} catch (Exception e) {
			alert.prepareError("Error while invoking Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_UPDATESAVINGSPOTBALANCE.getOperationName() + "  : " + e).log();
			return null;
		}
		try {
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
			savingsPotDTO = JSONUtils.parse(serviceResponseJSON.toString(), SavingsPotDTO.class);
		} catch (Exception e) {
			alert.prepareError("Service Error - while formatting response in Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_UPDATESAVINGSPOTBALANCE.getOperationName() + "  : " + e).log();
			return null;
		}
		return savingsPotDTO;
	}

	@Override
	public SavingsPotDTO updateSavingsPot(Map<String, Object> updateSavingsPotMap,Map<String, Object> headersMap) {
		SavingsPotDTO savingsPotDTO = null;
		String serviceResponse = null;
		String potType = (String) updateSavingsPotMap.get("potType");
		headersMap = generateSecurityHeadersForSavingsPot(headersMap);
		try {
			if(potType.equalsIgnoreCase(TemenosConstants.BUDGET))
			{
				serviceResponse = Executor.invokeService(SavingsPotAPIServices.SAVINGSPOTJSON_UPDATESAVINGSBUDGET, updateSavingsPotMap,
						headersMap);
			} else {
			serviceResponse = Executor.invokeService(SavingsPotAPIServices.SAVINGSPOTJSON_UPDATESAVINGSPOT,
					updateSavingsPotMap, headersMap);
			}
		} catch (Exception e) {
			alert.prepareError("Error while invoking Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_UPDATESAVINGSPOT.getOperationName() + "  : " + e).log();
			return null;
		}
		try {
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
			savingsPotDTO = JSONUtils.parse(serviceResponseJSON.toString(), SavingsPotDTO.class);
		} catch (Exception e) {
			alert.prepareError("Service Error - while formatting response in Microservice - "
					+ SavingsPotAPIServices.SAVINGSPOTJSON_UPDATESAVINGSPOT.getOperationName() + "  : " + e).log();
			return null;
		}
		return savingsPotDTO;
	}
	private	Map<String, Object> generateSecurityHeadersForSavingsPot( Map<String, Object> headerMap) {
	      //  headerMap.put("Authorization", authToken);
	        if (StringUtils.isNotEmpty(ServerConfigurations.SAVINGSPOT_HOST_DEPLOYMENT_PLATFORM.getValueIfExists())) {
	            if (StringUtils.equalsIgnoreCase(ServerConfigurations.SAVINGSPOT_HOST_DEPLOYMENT_PLATFORM.getValueIfExists(),
	                    MSCertificateConstants.AWS))
	                headerMap.put("x-api-key", ServerConfigurations.SAVINGSPOT_HOST_AUTHORIZATION_KEY.getValueIfExists());
	            else if (StringUtils.equalsIgnoreCase(ServerConfigurations.SAVINGSPOT_HOST_DEPLOYMENT_PLATFORM.getValueIfExists(),
	                    MSCertificateConstants.AZURE))
	                headerMap.put("x-functions-key", ServerConfigurations.SAVINGSPOT_HOST_AUTHORIZATION_KEY.getValueIfExists());
	        }
	        headerMap.put("roleId", ServerConfigurations.ACCAGG_ROLE_ID.getValueIfExists());
	        return headerMap;
	    }
		

	
}
