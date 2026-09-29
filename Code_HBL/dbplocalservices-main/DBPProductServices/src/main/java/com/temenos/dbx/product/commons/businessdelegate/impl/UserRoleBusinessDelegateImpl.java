package com.temenos.dbx.product.commons.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commons.businessdelegate.api.UserRoleBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class UserRoleBusinessDelegateImpl implements UserRoleBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public LimitsDTO fetchLimits(String userRole, String featureActionID) {

        Map<String, Object> requestParameters = new HashMap<String, Object>();
        LimitsDTO limitsDTO = null;
        String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
        String operationId = OperationName.DB_GROUP_ACTIONS_PROC;

        requestParameters.put("_groupId", userRole);
        requestParameters.put("_actionId", featureActionID);
        requestParameters.put("_actionType", "");
        requestParameters.put("_isOnlyPremissions", "");

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
            JSONObject roleLimitsJSON = new JSONObject(roleLimitsResponse);
            JSONArray roleLimitArray = roleLimitsJSON.getJSONArray("records");
            limitsDTO = _fetchLimitsDTO(roleLimitArray);

        } catch (Exception e) {
            alert.prepareError("Exception caught while fetching user role limits", e).log();
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
    private LimitsDTO _fetchLimitsDTO(JSONArray roleLimitArray) {

        LimitsDTO limitsDTO = new LimitsDTO();

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
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            alert.prepareError("Failed to fetch Role level limits from DB: " + e).log();
            return null;
        }

        return limitsDTO;
    }

    @Override
    public LimitsDTO fetchExhaustedLimits(String contractId, String coreCustomerId, String userRoleId, String featureActionID, String date, String customerId) {

    	LimitsDTO limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;
		
		requestParameters.put("featureactionid", featureActionID);
		requestParameters.put("companyid", contractId + "_" + coreCustomerId);
		requestParameters.put("roleid", userRoleId);
		requestParameters.put("date", date);
		requestParameters.put("customerid", customerId);
		
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
			alert.prepareError("Exception caught while fetching role limits", e).log();
			return null;
		}
		return limitsDTO;
    }

}
