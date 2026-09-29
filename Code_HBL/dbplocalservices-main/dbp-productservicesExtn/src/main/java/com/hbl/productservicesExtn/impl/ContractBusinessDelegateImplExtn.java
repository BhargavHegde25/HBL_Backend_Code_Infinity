package com.hbl.productservicesExtn.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.dto.LimitsDTOExtn;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.impl.ContractBusinessDelegateImpl;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class ContractBusinessDelegateImplExtn extends ContractBusinessDelegateImpl{
	private static final Logger LOG = LogManager.getLogger(ContractBusinessDelegateImplExtn.class);
	FeatureActionBusinessDelegateImplExtn featureActionDelegate = new FeatureActionBusinessDelegateImplExtn();
	public LimitsDTOExtn fetchAllLimits(String contractId, String coreCustomerId, String actionId, String legalEntityId) {
		LimitsDTOExtn limitsDTO = null;
		LimitsDTOExtn featureLimits = null;
		
		JSONArray limitsArray = fetchContractActionLimits(contractId, coreCustomerId, actionId);
		limitsDTO = _fetchLimitsDTO(limitsArray);
		LOG.debug("HBL::ContractBusinessDelegateImplExtn::contractactionlimits:: response:"+ limitsDTO.toString());
		JSONArray globalLimitsDTO = featureActionDelegate.fetchAllLimits(actionId, legalEntityId);
		featureLimits = _fetchLimitsDTO(globalLimitsDTO);
		LOG.debug("HBL::ContractBusinessDelegateImplExtn::actionlimits:: response:"+ limitsDTO.toString());
		limitsDTO.setMaxTransactionLimit(Math.min(featureLimits.getMaxTransactionLimit(), limitsDTO.getMaxTransactionLimit()));
		limitsDTO.setDailyLimit(Math.min(featureLimits.getDailyLimit(), limitsDTO.getDailyLimit()));
		limitsDTO.setWeeklyLimit(Math.min(featureLimits.getWeeklyLimit(), limitsDTO.getWeeklyLimit()));
		limitsDTO.setMonthlyLimit(Math.min(featureLimits.getMonthlyLimit(), limitsDTO.getMonthlyLimit()));
		limitsDTO.setMinTransactionLimit(featureLimits.getMinTransactionLimit());
		
		limitsDTO.setMaxMBTransactionLimit(Math.min(featureLimits.getMaxMBTransactionLimit(), limitsDTO.getMaxMBTransactionLimit()));
		limitsDTO.setDailyMBLimit(Math.min(featureLimits.getDailyMBLimit(), limitsDTO.getDailyMBLimit()));
		limitsDTO.setWeeklyMBLimit(Math.min(featureLimits.getWeeklyMBLimit(), limitsDTO.getWeeklyMBLimit()));
		limitsDTO.setMonthlyMBLimit(Math.min(featureLimits.getMonthlyMBLimit(), limitsDTO.getMonthlyMBLimit()));
		limitsDTO.setMinMBTransactionLimit(featureLimits.getMinMBTransactionLimit());
			
		return limitsDTO;
	}
	
	private LimitsDTOExtn _fetchLimitsDTO(JSONArray limitsArray) {
		
		LimitsDTOExtn limitsDTO = new LimitsDTOExtn();
		
		try {
			for(Object obj: limitsArray) {
	
				JSONObject limitsObj = (JSONObject) obj;
	
				String limitType = (limitsObj.has("limitTypeId")) ? limitsObj.getString("limitTypeId"): (limitsObj.has("LimitType_id")) ? limitsObj.getString("LimitType_id"):"";
				Double limit = (limitsObj.has("value")) ? limitsObj.getDouble("value"): 0;
	
				switch (limitType) {
	
					case Constants.MAX_TRANSACTION_LIMIT:
						limitsDTO.setMaxTransactionLimit(limit);
						break;
					case Constants.DAILY_LIMIT:
						limitsDTO.setDailyLimit(limit);
						break;
					case Constants.WEEKLY_LIMIT:
						limitsDTO.setWeeklyLimit(limit);
						limitsDTO.setMonthlyLimit(limit);
						break;
					case Constants.MIN_TRANSACTION_LIMIT:
						limitsDTO.setMinTransactionLimit(limit);
						break;
					
					case "MB_MAX_TRANSACTION_LIMIT":
						limitsDTO.setMaxMBTransactionLimit(limit);
						break;
					case "MB_DAILY_LIMIT":
						limitsDTO.setDailyMBLimit(limit);
						break;
					case "MB_WEEKLY_LIMIT":
						limitsDTO.setWeeklyMBLimit(limit);
						limitsDTO.setMonthlyMBLimit(limit);
						break;
					case "MB_MIN_TRANSACTION_LIMIT":
						limitsDTO.setMinMBTransactionLimit(limit);
						break;
					default:
						break;
				}
			}
		}
		catch (JSONException e) {
			LOG.error("Failed to fetch contract-corecustomer limits from DB: ", e);
			return null;
		}
		
		return limitsDTO;
	}
	
	
	public LimitsDTOExtn fetchExhaustedLimits(String contractId, String coreCustomerId, String featureActionID, String date, String channel) {
		
		LimitsDTOExtn limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;
		
		requestParameters.put("featureactionid", featureActionID);
		requestParameters.put("companyid", contractId + "_" + coreCustomerId);
		requestParameters.put("date", date);
		requestParameters.put("channel_name", channel);
		LOG.debug("HBL::ContractBusinessDelegateImplExtn: fetchExhaustedLimits: payload:"+requestParameters.toString());
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
			LOG.debug("HBL::ContractBusinessDelegateImplExtn: fetchExhaustedLimits: response:"+response);
			limitsDTO = JSONUtils.parse(response, LimitsDTOExtn.class);
			//setMBLimits(limitsDTO);
			if(channel.equals(HBLConstants.ONLINE_BANKING) && (limitsDTO.getDbpErrCode() == null 
					&& (limitsDTO.getDailyLimit() == null || limitsDTO.getWeeklyLimit() == null || limitsDTO.getMonthlyLimit() == null))) {
				return null;
			}
			else if(channel.equals(HBLConstants.MOBILE_BANKING) && (limitsDTO.getDbpErrCode() == null 
					&& (limitsDTO.getDailyMBLimit() == null || limitsDTO.getWeeklyMBLimit() == null || limitsDTO.getMonthlyMBLimit() == null))) {
				return null;
			}
			
		} catch (Exception e) {
			LOG.error("Exception caught while fetching Organization limits", e);
			return null;
		}
		return limitsDTO;
	}
	private void setMBLimits(LimitsDTOExtn limitsDTO) {
		LOG.debug("HBL::ContractBusinessDelegateImplExtn: assign exhausted mobile limits");
		limitsDTO.setDailyMBLimit(limitsDTO.getDailyMBLimit());
		limitsDTO.setWeeklyMBLimit(limitsDTO.getWeeklyMBLimit());
		limitsDTO.setMonthlyMBLimit(limitsDTO.getMonthlyMBLimit());
		LOG.debug("HBL::ContractBusinessDelegateImplExtn:getMBMonthlyLimits:"+limitsDTO.getMonthlyMBLimit());
	}

	private JSONArray fetchContractActionLimits(String contractId, String coreCustomerId, String actionId) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_CONTRACTACTIONLIMIT_GET;
		
		String filter = "contractId" + DBPUtilitiesConstants.EQUAL + contractId + DBPUtilitiesConstants.AND +
				"coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId;
		
		if(StringUtils.isNotEmpty(actionId))
			filter = filter + DBPUtilitiesConstants.AND + "actionId" + DBPUtilitiesConstants.EQUAL + actionId;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		LOG.debug("HBL::ContractBusinessDelegateImplExtn::fetchContractActionLimits(contractactionlimit):: payload:"+ requestParameters.toString());
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
			LOG.debug("HBL::ContractBusinessDelegateImplExtn::fetchContractActionLimits(contractactionlimit):: response:"+ response);
			JSONObject actionObj = new JSONObject(response);
			JSONArray actions = CommonUtils.getFirstOccuringArray(actionObj);
			
			return actions;
		}
		catch(JSONException e) {
			LOG.error("Failed to fetch contract action limits from DB: " + e);
		}
		catch(Exception e) {
			LOG.error("Failed to fetch contract action limits from DB: " + e);
		}
		return null;
	}
}
