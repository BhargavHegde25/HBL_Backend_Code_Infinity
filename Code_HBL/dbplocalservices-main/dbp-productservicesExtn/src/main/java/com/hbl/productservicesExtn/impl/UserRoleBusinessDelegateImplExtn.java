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
import com.temenos.dbx.product.commons.businessdelegate.impl.UserRoleBusinessDelegateImpl;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class UserRoleBusinessDelegateImplExtn extends UserRoleBusinessDelegateImpl {
	private static final Logger LOG = LogManager.getLogger(UserRoleBusinessDelegateImplExtn.class);
	 @Override
	    public LimitsDTOExtn fetchLimits(String userRole, String featureActionID) {

	        Map<String, Object> requestParameters = new HashMap<String, Object>();
	        LimitsDTOExtn limitsDTO = null;
	        String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
	        String operationId = OperationName.DB_GROUP_ACTIONS_PROC;

	        requestParameters.put("_groupId", userRole);
	        requestParameters.put("_actionId", featureActionID);
	        requestParameters.put("_actionType", "");
	        requestParameters.put("_isOnlyPremissions", "");
	        LOG.debug("HBL::UserRoleBusinessDelegateImplExtn::fetchLimits::GroupActionsLimits: payload:"+ requestParameters.toString());
	        try {
	            String roleLimitsResponse = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceId).
						withObjectId(null).
						withOperationId(operationId).
						withRequestParameters(requestParameters).
						build().getResponse();
	            
	            if (roleLimitsResponse == null) {
	                limitsDTO = null;
	            }
	            LOG.debug("HBL::UserRoleBusinessDelegateImplExtn::fetchLimits::GroupActionsLimits: response:"+ roleLimitsResponse);
	            JSONObject roleLimitsJSON = new JSONObject(roleLimitsResponse);
	            JSONArray roleLimitArray = roleLimitsJSON.getJSONArray("records");
	            limitsDTO = _fetchLimitsDTO(roleLimitArray);

	        } catch (Exception e) {
	            LOG.error("Exception caught while fetching user role limits", e);
	            return null;
	        }
	        return limitsDTO;
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

	                String limitType = (limitsObj.has("limitTyeId")) ? limitsObj.getString("limitTyeId") : "";
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
	            LOG.error("Failed to fetch Role level limits from DB: " + e);
	            return null;
	        }

	        return limitsDTO;
	    }


	    public LimitsDTOExtn fetchExhaustedLimits(String contractId, String coreCustomerId, String userRoleId, String featureActionID, String date, String customerId, String channel) {

	    	LimitsDTOExtn limitsDTO = null;
			Map<String, Object> requestParameters = new HashMap<String, Object>();

			String serviceId = ServiceId.TRANSACTIONSLIMIT;
			String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;
			
			requestParameters.put("featureactionid", featureActionID);
			requestParameters.put("companyid", contractId + "_" + coreCustomerId);
			requestParameters.put("roleid", userRoleId);
			requestParameters.put("date", date);
			requestParameters.put("customerid", customerId);
			requestParameters.put("channel_name", channel);
			LOG.debug("HBL::UserRoleBusinessDelegateImplExtn::fetchExhaustedLimits::: payload:"+ requestParameters.toString());
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
				LOG.debug("HBL::UserRoleBusinessDelegateImplExtn::fetchExhaustedLimits::: response:"+ response);
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
				LOG.error("Exception caught while fetching role limits", e);
				return null;
			}
			return limitsDTO;
	    }
	    private void setMBLimits(LimitsDTOExtn limitsDTO) {
			LOG.debug("HBL::UserRoleBusinessDelegateImplExtn: assign exhausted mobile limits");
			limitsDTO.setDailyMBLimit(limitsDTO.getDailyLimit());
			limitsDTO.setWeeklyMBLimit(limitsDTO.getWeeklyLimit());
			limitsDTO.setMonthlyMBLimit(limitsDTO.getWeeklyLimit());
			LOG.debug("HBL::UserRoleBusinessDelegateImplExtn:getMBMonthlyLimits:"+limitsDTO.getMonthlyMBLimit());
		}
}
