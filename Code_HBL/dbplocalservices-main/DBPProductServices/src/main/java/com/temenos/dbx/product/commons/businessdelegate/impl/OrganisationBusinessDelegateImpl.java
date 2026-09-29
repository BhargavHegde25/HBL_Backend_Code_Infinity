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
import com.temenos.dbx.product.commons.businessdelegate.api.OrganisationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.businessdelegate.impl.BillPayTransactionBusinessDelegateImpl;

public class OrganisationBusinessDelegateImpl implements OrganisationBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public String fetchActiveFeatureActions(String companyId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ORGANIZATION_ACTIONS_PROC;

		String response = null;
		String featureActionList = "";
		
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			
			requestParameters.put("_organizationId", companyId);
			requestParameters.put("_actionType", "");
			requestParameters.put("_actionId", "");
			
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			if(response == null) {
				return null;
			}
			
			JSONObject responseJson = new JSONObject(response);
			if(!responseJson.has("opstatus") || responseJson.getInt("opstatus") != 0 || !responseJson.has("records")) {
				return null;
			}
			
			JSONArray featureActions = responseJson.getJSONArray("records");
			
			if(featureActions != null && featureActions.length() > 0) {
				
				for(Object permissionObject : featureActions) {
					JSONObject permission = (JSONObject) permissionObject;
					String fiFeatureStatus = permission.getString(Constants.FI_FEATURESTATUS);
					
					String orgFeatureStatus = null;
					if(permission.has(Constants.ORG_FEATURESTATUS))
						orgFeatureStatus = permission.getString(Constants.ORG_FEATURESTATUS);
					
					String actionId = permission.getString(Constants.ACTIONID);
					
					if(Constants.SID_FEATURE_ACTIVE.equals(fiFeatureStatus) && (orgFeatureStatus == null || orgFeatureStatus == "")) {
						featureActionList = featureActionList + ","+actionId;
					}
				}
				if(featureActionList.length() > 0) {
					featureActionList = featureActionList.substring(1);
				}
			}
			else {
				alert.prepareError("No active feature actions exists for the given organisation in DB: ").log();
				return null;
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch Organisation feature action entry from DB: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception whiel Organisation feature action entry from DB: " + e).log();
			return null;
		}
		
		return featureActionList;
	}

	@Override
	public LimitsDTO fetchLimits(String companyId, String featureActionID) {
		
		LimitsDTO limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_ORGANIZATION_ACTIONS_PROC;
		
		requestParameters.put("_organizationId", companyId);
		requestParameters.put("_actionType", "");
		requestParameters.put("_actionId", featureActionID);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			if(response == null) {
				limitsDTO = null;
			}
			JSONObject companyLimitsJSON = new JSONObject(response);
			JSONArray companyLimitArray = companyLimitsJSON.getJSONArray("records");
			limitsDTO = _fetchLimitsDTO(companyLimitArray);
			
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching Organization limits", e).log();
			return null;
		}
		return limitsDTO;
	}
	
	/**
	 * Fetches the company level limits for a feature action by using the most restrictive rule between organization and FI
	 * @param limitsArray
	 * @return {@link LimitsDTO}
	 */
	private LimitsDTO _fetchLimitsDTO(JSONArray limitsArray) {
		
		LimitsDTO limitsDTO = new LimitsDTO();
		
		try {
			for(Object obj: limitsArray) {
	
				JSONObject limitsObj = (JSONObject) obj;
	
				String limitType = (limitsObj.has("limitTypeId")) ? limitsObj.getString("limitTypeId"): "";
				Double fiLimitValue = (limitsObj.has("fiLimitValue")) ? limitsObj.getDouble("fiLimitValue"): 0;
				Double orgLimitValue = (limitsObj.has("orgLimitValue")) ? limitsObj.getDouble("orgLimitValue"): 0;
				Double restrictiveLimit = Math.min(fiLimitValue, orgLimitValue);
	
				switch (limitType) {
	
					case Constants.MAX_TRANSACTION_LIMIT:
						limitsDTO.setMaxTransactionLimit(restrictiveLimit);
						break;
					case Constants.DAILY_LIMIT:
						limitsDTO.setDailyLimit(restrictiveLimit);
						break;
					case Constants.WEEKLY_LIMIT:
						limitsDTO.setWeeklyLimit(restrictiveLimit);
						break;
					default:
						break;
				}
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch Organisation limits from DB: " + e).log();
			return null;
		}
		
		return limitsDTO;
	}
	
	@Override
	public LimitsDTO fetchExhaustedLimits(String companyId, String featureActionID, String date) {
		
		LimitsDTO limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;
		
		requestParameters.put("featureactionid", featureActionID);
		requestParameters.put("companyid", companyId);
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
			alert.prepareError("Exception caught while fetching Organization limits", e).log();
			return null;
		}
		return limitsDTO;
	}
	
}

