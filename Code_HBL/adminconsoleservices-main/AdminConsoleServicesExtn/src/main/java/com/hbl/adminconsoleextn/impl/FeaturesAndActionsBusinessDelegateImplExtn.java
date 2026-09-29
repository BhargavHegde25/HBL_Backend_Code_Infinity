package com.hbl.adminconsoleextn.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.hbl.adminconsoleextn.dto.ActionsDTOExtn;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.service.featuresandactions.businessdelegate.impl.FeaturesAndActionsBusinessDelegateImpl;
import com.kony.adminconsole.service.featuresandactions.dto.ActionsDTO;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class FeaturesAndActionsBusinessDelegateImplExtn extends FeaturesAndActionsBusinessDelegateImpl{
	private static final Logger LOG = Logger.getLogger(FeaturesAndActionsBusinessDelegateImplExtn.class);
	public List<ActionsDTOExtn> getActionsLimitsHBL(String actionId) {
		LOG.debug("HBL::FeaturesAndActionsBusinessDelegateImplExtn:getActionsLimitsNew");
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_ACTIONLIMIT_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "Action_id eq '" + actionId + "'";
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String response = null;
		List<ActionsDTOExtn> actions = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = responseObj.optJSONArray("actionlimit");
			LOG.debug("HBL::FeaturesAndActionsBusinessDelegateImplExtn:jsonArray:"+jsonArray.toString());
			actions = JSONUtils.parseAsList(jsonArray.toString(), ActionsDTOExtn.class);
			LOG.debug("HBL::FeaturesAndActionsBusinessDelegateImplExtn:actions:"+actions.toString());
		}
		catch (JSONException e) {
			LOG.error("Failed to fetch existing action details: " + e);
			return null;
		}
		catch (Exception e) {
			LOG.error("Caught exception at getActionDetails: " + e);
			return null;
		}
		
		return actions;
	}
	
	public boolean editActionLimitsHBL(ActionsDTOExtn actionsDTO) {
		LOG.debug("HBL::FeaturesAndActionsBusinessDelegateImplExtn:editActionLimits");
		String serviceName = ServiceId.CRUDLAYER;
		//String operationName = OperationName.DB_ACTION_LIMITS_UPDATE_PROC;
		String operationName = OperationName.SCHEMA_NAME + "_hbl_feature_action_limits_update_proc";

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(actionsDTO).toString(), String.class, Object.class);
			requestParameters.put("_action",actionsDTO.getId());
			requestParameters.put("_minTxLimit",actionsDTO.getMinTxLimit());
			requestParameters.put("_maxTxLimit",actionsDTO.getMaxTxLimit());
			requestParameters.put("_dailyLimit",actionsDTO.getDailyLimit());
			requestParameters.put("_weeklyLimit",actionsDTO.getWeeklyLimit());
			requestParameters.put("_companyLegalUnit",actionsDTO.getCompanyLegalUnit());
			requestParameters.put("_minMBTxLimit",actionsDTO.getMB_minTxLimit());
			requestParameters.put("_maxMBTxLimit",actionsDTO.getMB_maxTxLimit());
			requestParameters.put("_dailyMBLimit",actionsDTO.getMB_dailyLimit());
			requestParameters.put("_weeklyMBLimit",actionsDTO.getMB_weeklyLimit());
			
			LOG.debug("HBL::FeaturesAndActionsBusinessDelegateImplExtn:editActionLimits:requestParameters:"+requestParameters.toString());
		} catch (IOException e) {
			LOG.error("Error occured while fetching the input params: " + e);
			return false;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			LOG.debug("HBL::FeaturesAndActionsBusinessDelegateImplExtn:editActionLimitsHBL:responseObj:"+responseObj.toString());
			if((responseObj != null)
					&& responseObj.has(FabricConstants.OPSTATUS)
					&& responseObj.getInt(FabricConstants.OPSTATUS) == 0) {
				return true;
			}else {
				return false;
			}
		} catch (JSONException e) {
			LOG.error("HBL::updateLimitsHBL:Unable to edit MB action limits " + e);
			return false;
		} catch (Exception e) {
			LOG.error("HBL::updateLimitsHBL:Caught exception at MB editActionLimits method: " + e);
			return false;
		}

	}
	public boolean updateLimitsHBL(ActionsDTOExtn actionsDTO) {
		LOG.debug("HBL::FeaturesAndActionsBusinessDelegateImplExtn:updateLimits");
		String serviceName = ServiceId.CRUDLAYER;
		//String operationName = OperationName.DB_LIMITS_UPDATE_PROC;
		String operationName = OperationName.SCHEMA_NAME + "_hbl_cummulative_limits_update_proc";

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(actionsDTO).toString(), String.class, Object.class);
			requestParameters.put("_action",actionsDTO.getId());
			requestParameters.put("_maxTxLimit",actionsDTO.getMaxTxLimit());
			requestParameters.put("_dailyLimit",actionsDTO.getDailyLimit());
			requestParameters.put("_weeklyLimit",actionsDTO.getWeeklyLimit());
			requestParameters.put("_companyLegalUnit",actionsDTO.getCompanyLegalUnit());
			
			requestParameters.put("_minMBTxLimit",actionsDTO.getMB_minTxLimit());
			requestParameters.put("_maxMBTxLimit",actionsDTO.getMB_maxTxLimit());
			requestParameters.put("_dailyMBLimit",actionsDTO.getMB_dailyLimit());
			requestParameters.put("_weeklyMBLimit",actionsDTO.getMB_weeklyLimit());
			LOG.debug("HBL::FeaturesAndActionsBusinessDelegateImplExtn:updateLimits:requestParameters:"+requestParameters.toString());
		} catch (IOException e) {
			LOG.error("HBL::updateLimitsHBL:Error occured while MB fetching the input params: " + e);
			return false;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			LOG.debug("HBL::FeaturesAndActionsBusinessDelegateImplExtn:updateLimitsHBL:responseObj:"+responseObj.toString());
			if((responseObj != null)
					&& responseObj.has(FabricConstants.OPSTATUS)
					&& responseObj.getInt(FabricConstants.OPSTATUS) == 0) {
				return true;
			}else {
				return false;
			}
		} catch (JSONException e) {
			LOG.error("HBL::updateLimitsHBL:Unable to edit MB action limits " + e);
			return false;
		} catch (Exception e) {
			LOG.error("HBL::updateLimitsHBL:Caught exception at MBupdateLimits method: " + e);
			return false;
		}
	}


}
