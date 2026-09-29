package com.temenos.dbx.product.commons.businessdelegate.impl;

import java.util.ArrayList;
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
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commons.businessdelegate.api.LimitGroupBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.LimitGroupDTO;
import com.temenos.dbx.product.commons.dto.LimitGroupDisplayDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class LimitGroupBusinessDelegateImpl implements LimitGroupBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public List<LimitGroupDTO> fetchLimitGroups() {
		
		List<LimitGroupDTO> groups = new ArrayList<LimitGroupDTO>();
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_LIMITGROUP_GET).
					withRequestParameters(null).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray limitgroups = CommonUtils.getFirstOccuringArray(res);
			groups = JSONUtils.parseAsList(limitgroups.toString(), LimitGroupDTO.class);
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
		}
		return groups;
	}	

	@Override
	public LimitsDTO fetchLimits(String customerId, String contractId, String coreCustomerId, String limitGroupId) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		LimitsDTO limitGroupLimits = null;
		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_CUSTOMERLIMITGROUPLIMITS_GET;

		String customer_id = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId ;
		String contract_Id = "contractId" + DBPUtilitiesConstants.EQUAL + contractId ;
		String coreCustomer_Id = "coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId ;
		String limitGroup_Id = "limitGroupId" + DBPUtilitiesConstants.EQUAL + limitGroupId ;

		String filter = String.join(DBPUtilitiesConstants.AND ,customer_id, contract_Id, coreCustomer_Id, limitGroup_Id);        
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			String limitGroupLimitsResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();

			if (limitGroupLimitsResponse == null) {
				limitGroupLimits = null;
			}
			JSONObject limitGroupLimitsJSON = new JSONObject(limitGroupLimitsResponse);
			JSONArray limitGroupLimitsArray = limitGroupLimitsJSON.getJSONArray("customerlimitgrouplimits");
			limitGroupLimits = _fetchLimitsDTO(limitGroupLimitsArray);

		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching limit group limits", e).log();
			return null;
		}

		return limitGroupLimits;		
	}

	/**
	 * Fetches the LimitsDTO from the given JSONArray
	 * 
	 * @param roleLimitArray
	 * @return {@link LimitsDTO}
	 */
	private LimitsDTO _fetchLimitsDTO(JSONArray roleLimitArray) {

		LimitsDTO limitsDTO = new LimitsDTO();

		try {
			for (Object obj : roleLimitArray) {

				JSONObject limitsObj = (JSONObject) obj;

				String limitType = (limitsObj.has("LimitType_id")) ? limitsObj.getString("LimitType_id") : "";
				Double limitValue = (limitsObj.has("value")) ? limitsObj.getDouble("value") : 0;

				switch (limitType) {

				case Constants.MAX_TRANSACTION_LIMIT:
					limitsDTO.setMaxTransactionLimit(limitValue);
					break;
				case Constants.DAILY_LIMIT:
					limitsDTO.setDailyLimit(limitValue);
					break;
				case Constants.WEEKLY_LIMIT:
					limitsDTO.setWeeklyLimit(limitValue);
					break;
				default:
					break;
				}
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to fetch Limit group limits from DB: " + e).log();
			return null;
		}

		return limitsDTO;
	}

	@Override
	public LimitsDTO fetchExhaustedLimits(String contractId, String coreCustomerId, String customerId, String limitGroupId, String date) {

		LimitsDTO limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;

		requestParameters.put("companyid", contractId + "_" + coreCustomerId);
		requestParameters.put("customerid", customerId);
		requestParameters.put("limitgroupId", limitGroupId);
		requestParameters.put("date", date);

		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();

			if(response == null) {
				return null;
			}
			limitsDTO = JSONUtils.parse(response, LimitsDTO.class);

			if(limitsDTO.getDbpErrCode() == null 
					&& (limitsDTO.getDailyLimit() == null || limitsDTO.getWeeklyLimit() == null)) {
				return null;
			}

		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching customer limits", e).log();
			return null;
		}
		return limitsDTO;
	}
	
	@Override
	public List<LimitGroupDTO> fetchLimitGroupsWithLanguageId(String localeId) {
		
		List<LimitGroupDTO> limitGroupDTOs = fetchLimitGroups();
		List<LimitGroupDisplayDTO> limitGroupDisplayDTOs = new ArrayList<LimitGroupDisplayDTO>();
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "localeId" + DBPUtilitiesConstants.EQUAL + localeId ;        
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_LIMITGROUPDISPLAYNAMEDESCRIPTION_GET).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray limitgroups = CommonUtils.getFirstOccuringArray(res);
			limitGroupDisplayDTOs = JSONUtils.parseAsList(limitgroups.toString(), LimitGroupDisplayDTO.class);
			limitGroupDTOs = (new FilterDTO()).merge(limitGroupDTOs, limitGroupDisplayDTOs, "limitGroupId=limitGroupId","limitGroupName");
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
		}
		return limitGroupDTOs;
	}

}
