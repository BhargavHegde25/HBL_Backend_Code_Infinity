package com.hbl.productservicesExtn.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.dto.LimitsDTOExtn;
import com.hbl.productservicesExtn.dto.UserLimitsDTOExtn;
import com.temenos.dbx.product.commons.businessdelegate.impl.CustomerBusinessDelegateImpl;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.commons.dto.UserLimitsDTO;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class CustomerBusinessDelegateImplExtn extends CustomerBusinessDelegateImpl{
	
	private static final Logger LOG = LogManager.getLogger(CustomerBusinessDelegateImplExtn.class);
	@Override
	public UserLimitsDTOExtn fetchCustomerLimits(String customerId, String featureActionID, String accountId) {
		
		UserLimitsDTOExtn userLimitsDTO = null;
		String serviceId = ServiceId.DBP_PRODUCT_SERVICES;
		//String operationId = OperationName.GET_CUSTOMER_ACCOUNT_ACTION_LIMITS;
		String operationId = "HBLGetCustomerAccountActionLimits";
		Map<String, Object> requestParams = new HashMap<String, Object>();
		
		requestParams.put("customerId", customerId);
		requestParams.put("action", featureActionID);
		requestParams.put("accountId", accountId);
		LOG.debug("HBL::CustomerBusinessDelegateImplExtn::fetchCustomerLimits::customerAccountActionLimits: payload:"+ requestParams.toString());
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParams).
					build().getResponse();

			if(response == null) {
				userLimitsDTO = null;
			}
			LOG.debug("HBL::CustomerBusinessDelegateImplExtn::fetchCustomerLimits::customerAccountActionLimits: response:"+ response);
			JSONObject responseObj = new JSONObject(response).getJSONObject("limits");
			userLimitsDTO = JSONUtils.parse(responseObj.toString(), UserLimitsDTOExtn.class);
			userLimitsDTO.setMonthlyLimit(userLimitsDTO.getWeeklyLimit());
			userLimitsDTO.setMonthlyMBLimit(userLimitsDTO.getWeeklyMBLimit());
		} catch (Exception e) {
			LOG.error("Exception caught while fetching user limits", e);
			return null;
		}
		return userLimitsDTO;
	}
	
	private void setMBExahustedLimits(LimitsDTOExtn limitsDTO) {
		LOG.debug("HBL::CustomerBusinessDelegateImplExtn: assign exhausted mobile limits");
		limitsDTO.setDailyMBLimit(limitsDTO.getDailyLimit());
		limitsDTO.setWeeklyMBLimit(limitsDTO.getWeeklyLimit());
		limitsDTO.setMonthlyMBLimit(limitsDTO.getWeeklyLimit());
		LOG.debug("HBL::CustomerBusinessDelegateImplExtn:getMBMonthlyLimits:"+limitsDTO.getMonthlyMBLimit());
	}
	

	public LimitsDTOExtn fetchExhaustedLimits(String customerId, String featureActionID, String date, String accountId, String channel) {
		LimitsDTOExtn limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;
		
		requestParameters.put("featureactionid", featureActionID);
		requestParameters.put("customerid", customerId);
		requestParameters.put("accountid", accountId);
		requestParameters.put("date", date);
		requestParameters.put("channel_name", channel);
		LOG.debug("HBL::CustomerBusinessDelegateImplExtn::fetchCustomerLimits::fetchExhaustedLimits: payload:"+ requestParameters.toString());
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
			LOG.debug("HBL::CustomerBusinessDelegateImplExtn::fetchCustomerLimits::fetchExhaustedLimits: response:"+ response);
			limitsDTO = JSONUtils.parse(response, LimitsDTOExtn.class);
			//setMBExahustedLimits(limitsDTO);
			if(channel.equals(HBLConstants.ONLINE_BANKING) && ( limitsDTO.getDbpErrCode() == null 
					&& (limitsDTO.getDailyLimit() == null || limitsDTO.getWeeklyLimit() == null || limitsDTO.getMonthlyLimit() == null) )) {
				return null;
			}
			else if(channel.equals(HBLConstants.MOBILE_BANKING) && ( limitsDTO.getDbpErrCode() == null 
					&& (limitsDTO.getDailyMBLimit() == null || limitsDTO.getWeeklyMBLimit() == null || limitsDTO.getMonthlyMBLimit() == null) )) {
				return null;
			}
			
		} catch (Exception e) {
			LOG.error("Exception caught while fetching customer limits", e);
			return null;
		}
		return limitsDTO;
	}
	


}
