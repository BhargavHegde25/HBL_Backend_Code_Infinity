package com.hbl.productservicesExtn.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.dto.LimitsDTOExtn;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commons.businessdelegate.impl.ApplicationBusinessDelegateImpl;
import com.temenos.dbx.product.commons.businessdelegate.impl.LimitGroupBusinessDelegateImpl;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class LimitGroupBusinessDelegateImplExtn extends LimitGroupBusinessDelegateImpl {
	private static final Logger LOG = LogManager.getLogger(LimitGroupBusinessDelegateImplExtn.class);
	@Override
	public LimitsDTOExtn fetchLimits(String customerId, String contractId, String coreCustomerId, String limitGroupId) {			

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		LimitsDTOExtn limitGroupLimits = null;
		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_CUSTOMERLIMITGROUPLIMITS_GET;

		String customer_id = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId ;
		String contract_Id = "contractId" + DBPUtilitiesConstants.EQUAL + contractId ;
		String coreCustomer_Id = "coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId ;
		String limitGroup_Id = "limitGroupId" + DBPUtilitiesConstants.EQUAL + limitGroupId ;

		String filter = String.join(DBPUtilitiesConstants.AND ,customer_id, contract_Id, coreCustomer_Id, limitGroup_Id);        
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		LOG.debug("HBL::LimitGroupBusinessDelegateImplExtn::fetchLimits::customerlimitgrouplimits: payload:"+ requestParameters.toString());

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
			LOG.debug("HBL::LimitGroupBusinessDelegateImplExtn::fetchLimits::customerlimitgrouplimits: response:"+ limitGroupLimitsArray.toString());
			limitGroupLimits = _fetchLimitsDTO(limitGroupLimitsArray);

		} catch (Exception e) {
			LOG.error("Exception caught while fetching limit group limits", e);
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
	private LimitsDTOExtn _fetchLimitsDTO(JSONArray roleLimitArray) {

		LimitsDTOExtn limitsDTO = new LimitsDTOExtn();

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
					limitsDTO.setMonthlyLimit(limitValue);
					break;
				case "MB_MAX_TRANSACTION_LIMIT":
					limitsDTO.setMaxMBTransactionLimit(limitValue);
					break;
				case "MB_DAILY_LIMIT":
					limitsDTO.setDailyMBLimit(limitValue);
					break;
				case "MB_WEEKLY_LIMIT":
					limitsDTO.setWeeklyMBLimit(limitValue);
					limitsDTO.setMonthlyMBLimit(limitValue);
					break;
				case "MB_MIN_TRANSACTION_LIMIT":
					limitsDTO.setMinMBTransactionLimit(limitValue);
					break;
				default:
					break;
				}
			}
		} catch (JSONException e) {
			LOG.error("Failed to fetch Limit group limits from DB: " + e);
			return null;
		}

		return limitsDTO;
	}

	
	public LimitsDTOExtn fetchExhaustedLimits(String contractId, String coreCustomerId, String customerId, String limitGroupId, String date, String channel) {

		LimitsDTOExtn limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;

		requestParameters.put("companyid", contractId + "_" + coreCustomerId);
		requestParameters.put("customerid", customerId);
		requestParameters.put("limitgroupId", limitGroupId);
		requestParameters.put("date", date);
		requestParameters.put("channel_name", channel);
		LOG.debug("HBL::LimitGroupBusinessDelegateImplExtn::fetchLimits::fetchExhaustedLimits: payload:"+ requestParameters.toString());
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
			LOG.debug("HBL::LimitGroupBusinessDelegateImplExtn::fetchLimits::fetchExhaustedLimits: response:"+ response);
			limitsDTO = JSONUtils.parse(response, LimitsDTOExtn.class);
			//setMBLimits(limitsDTO);
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
	
	private void setMBLimits(LimitsDTOExtn limitsDTO) {
		LOG.debug("HBL::LimitGroupBusinessDelegateImplExtn: assign exhausted mobile limits");
		limitsDTO.setDailyMBLimit(limitsDTO.getDailyLimit());
		limitsDTO.setWeeklyMBLimit(limitsDTO.getWeeklyLimit());
		limitsDTO.setMonthlyMBLimit(limitsDTO.getWeeklyLimit());
		LOG.debug("HBL::LimitGroupBusinessDelegateImplExtn:getMBMonthlyLimits:"+limitsDTO.getMonthlyMBLimit());
	}
}
