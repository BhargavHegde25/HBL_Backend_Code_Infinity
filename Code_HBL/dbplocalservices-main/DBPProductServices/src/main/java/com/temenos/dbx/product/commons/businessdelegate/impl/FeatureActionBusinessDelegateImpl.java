package com.temenos.dbx.product.commons.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.LimitGroupBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.commons.dto.FeatureDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.LimitGroupDTO;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class FeatureActionBusinessDelegateImpl implements FeatureActionBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	LimitGroupBusinessDelegate limitGroupDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(LimitGroupBusinessDelegate.class);
	FeatureBusinessDelegate featureDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureBusinessDelegate.class);
	
	@Override
	public List<FeatureActionDTO> fetchFeatureActions() {
		
		List<FeatureActionDTO> actionList = new ArrayList<FeatureActionDTO>();
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_FEATUREACTION_GET).
					withRequestParameters(null).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray actions = CommonUtils.getFirstOccuringArray(res);
			actionList = JSONUtils.parseAsList(actions.toString(), FeatureActionDTO.class);
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
		}
		return actionList;	
	}



	@Override
	public List<FeatureActionDTO> fetchFeatureActionsWithLimitGroupDetails() {
		
		List<FeatureActionDTO> actionList = fetchFeatureActionsWithFeatureDetails();
		List<LimitGroupDTO> groups = limitGroupDelegate.fetchLimitGroups();
		
		actionList = (new FilterDTO()).merge(actionList, groups, "limitGroupId=limitGroupId", "limitGroupName");
		
		return actionList;
	}
	
	@Override
	public String getLimitGroupId(String featureActionID,String legalEntityId) {

		Map<String, Object> requestParameters = new HashMap<String, Object>();	
		String filter = "id" + DBPUtilitiesConstants.EQUAL + featureActionID + DBPUtilitiesConstants.AND +
				"companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		JSONObject responseObj = getFeatureActionData(requestParameters);
		if(responseObj == null) {
			alert.prepareError("Error while fetching the limit group id").log();
			return null;
		}

		JSONArray featureActionArray = CommonUtils.getFirstOccuringArray(responseObj);	
		if(featureActionArray == null || featureActionArray.length() != 1) {
			alert.prepareError("Error while fetching the limit group id").log();
			return null;
		}

		JSONObject featureAction = (JSONObject) featureActionArray.get(0);
		String limitGroupId = featureAction.optString("limitgroupId");
		return limitGroupId;					
	}
	
	@Override
	public List<String> getFeatureActionsForLimitGroupId(String limitgroupId) {

		List<String> featureActions = new ArrayList<String>();

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "limitgroupId" + DBPUtilitiesConstants.EQUAL + limitgroupId;							
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		JSONObject responseObj = getFeatureActionData(requestParameters);
		if(responseObj == null) {
			alert.prepareError("Error while fetching the feature actions for the respective limit group id").log();
			return null;
		}

		JSONArray featureActionArray = CommonUtils.getFirstOccuringArray(responseObj);
		if(featureActionArray == null || featureActionArray.length() == 0) {
			alert.prepareError("Error while fetching the feature actions for the respective limit group id").log();
			return null;
		}

		for(int i = 0; i < featureActionArray.length(); i++) {
			JSONObject featureAction = (JSONObject) featureActionArray.get(i);
			featureActions.add(featureAction.optString("id"));									
		}				
		return featureActions;
	}
	
	@Override
	public LimitsDTO fetchExhaustedLimitsOfFeatureAction(String companyId, String customerId, String featureActionID, String date) {

		LimitsDTO limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.TRANSACTIONSLIMIT;
		String operationId = OperationName.GET_TRANSACTIONS_AMOUNT;

		requestParameters.put("featureactionid", featureActionID);
		requestParameters.put("companyId", companyId);
		requestParameters.put("customerid", customerId);
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
			alert.prepareError("Exception caught while fetching the exhausted customer limits", e).log();
			return null;
		}
		return limitsDTO;
	}
	
	private JSONObject getFeatureActionData(Map<String, Object> requestParameters) {

		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_FEATUREACTION_GET;
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();

			JSONObject responseObj = new JSONObject(response);				
			return responseObj;									
		} catch (Exception e) {
			alert.prepareError("Error while fetching the records from featureaction table", e).log();
			return null;
		}	
	}
	
	@Override	
	public String getApproveFeatureAction(String givenFeatuerAcion) {
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "";
		if(StringUtils.isNotEmpty(givenFeatuerAcion)) {
			filter = "id" + DBPUtilitiesConstants.EQUAL + givenFeatuerAcion;
		}
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		FeatureActionDTO actionDTO;
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_FEATUREACTION_GET).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(res);
			actionDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), FeatureActionDTO.class);
			return actionDTO.getApproveFeatureAction();
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while getApproveFeatureAction properties", e).log();
			return null;
		}
	}
	
	@Override	
	public Set<String> getApprovalMatrixFeatureActions(Set<String> features) {
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "";
		Set<String> approvalMatrixActions = new HashSet<String>();
		
		if(CollectionUtils.isNotEmpty(features)) {
			filter = DBPUtilitiesConstants.OPEN_BRACE + 
					"Feature_id" + DBPUtilitiesConstants.EQUAL + 
					String.join(DBPUtilitiesConstants.OR + "Feature_id" + DBPUtilitiesConstants.EQUAL, features) 
					+ DBPUtilitiesConstants.CLOSE_BRACE + 
					DBPUtilitiesConstants.AND;
		}
		
		filter = filter + "approveFeatureAction" + DBPUtilitiesConstants.NOT_EQ + "NULL";
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		List<FeatureActionDTO> actionsDTO;
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_FEATUREACTION_GET).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(res);
			actionsDTO = JSONUtils.parseAsList(responseArray.toString(), FeatureActionDTO.class);
			
			if(actionsDTO != null) {
				for(FeatureActionDTO action: actionsDTO) {
					approvalMatrixActions.add(action.getFeatureActionId());
				}
				return approvalMatrixActions;
			}
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while getApproveFeatureAction properties", e).log();
		}
		
		return null;
	}
	
	@Override	
	public FeatureActionDTO getFeatureActionById(String id) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "";
		if(StringUtils.isNotEmpty(id)) {
			filter = "id" + DBPUtilitiesConstants.EQUAL + id;
		}
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		JSONObject res = getFeatureActionData(requestParameters) ;
		JSONArray responseArray = CommonUtils.getFirstOccuringArray(res);
		FeatureActionDTO actionDTO = null;
		try {
			actionDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), FeatureActionDTO.class);
		} catch (Exception e) {
			alert.prepareError("Exception caught while getApproveFeatureAction properties", e).log();
			return null;
		}
		return actionDTO;
	}

	@Override
	public List<FeatureActionDTO> fetchFeatureActionsWithFeatureDetails() {
		
		List<FeatureActionDTO> actionList = fetchFeatureActions();
		List<FeatureDTO> features = featureDelegate.fetchFeatures();
		
		actionList = (new FilterDTO()).merge(actionList, features, "featureId=featureId", "featureName");
		
		return actionList;
	}

	@Override
	public LimitsDTO fetchLimits(String actionId, String legalEntityId) {
		LimitsDTO limitsDTO = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();

		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_ACTIONLIMIT_GET;
		
		String filter = "Action_id" + DBPUtilitiesConstants.EQUAL + actionId + DBPUtilitiesConstants.AND + "companyLegalUnit" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
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
			JSONObject limitsJSON = new JSONObject(response);
			JSONArray limitsArray = CommonUtils.getFirstOccuringArray(limitsJSON);
			limitsDTO = _fetchLimitsDTO(limitsArray);
			
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching global FI level limits", e).log();
			return null;
		}
		return limitsDTO;
	}
	
	private LimitsDTO _fetchLimitsDTO(JSONArray limitsArray) {
		
		LimitsDTO limitsDTO = new LimitsDTO();
		
		try {
			for(Object obj: limitsArray) {
	
				JSONObject limitsObj = (JSONObject) obj;
	
				String limitType = (limitsObj.has("LimitType_id")) ? limitsObj.getString("LimitType_id"): "";
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
						break;
					case Constants.MIN_TRANSACTION_LIMIT:
						limitsDTO.setMinTransactionLimit(limit);
						break;
					default:
						break;
				}
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch global FI level limits from DB: ", e).log();
			return null;
		}
		
		return limitsDTO;
	}

}
