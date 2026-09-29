package com.temenos.dbx.product.contract.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.contract.backenddelegate.api.ContractFeatureActionsBackendDelegate;

public class ContractFeatureActionsBackendDelegateImpl implements ContractFeatureActionsBackendDelegate {

    @Override
    public Set<String> createContractFeatures(Set<String> featuresList, String contractId, String customerId,
            String serviceTypeID, String isDefaultActionsEnabled, Map<String, Object> headersMap)
            throws ApplicationException {
        StringBuilder featuresString = new StringBuilder();
        Set<String> featuresCreated = null;
        JsonObject featuresJson;
        for (String feature : featuresList) {
            if (StringUtils.isNotBlank(feature)) {
                featuresString.append(feature);
                featuresString.append(DBPUtilitiesConstants.COMMA_SEPERATOR);
            }

        }
        if (featuresString.length() > 0) {
            featuresString.replace(featuresString.length() - 1, featuresString.length(), "");
        } else {
            return featuresCreated;
        }

        if (StringUtils.isBlank(isDefaultActionsEnabled)) {
            isDefaultActionsEnabled = "false";
        }
        Map<String, Object> inputParams = new HashMap<>();
        try {
            inputParams.put("_features", featuresString.toString());
            inputParams.put("_contractId", contractId);
            inputParams.put("_customerId", customerId);
            inputParams.put("_serviceTypeId", serviceTypeID);
            inputParams.put("_defaultActionsEnabled", isDefaultActionsEnabled);
            inputParams.put("_legalEntityId", "GB0010001");

            featuresJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACTFEATURES_CREATE_PROC);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10358);
        }

        if (JSONUtil.isJsonNotNull(featuresJson)
                && JSONUtil.hasKey(featuresJson, DBPDatasetConstants.DATASET_RECORDS)
                && featuresJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
            JsonObject resultFeaturesJson =
                    featuresJson.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().size() > 0
                            ? featuresJson.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray().get(0)
                                    .getAsJsonObject()
                            : null;
            if (resultFeaturesJson != null && JSONUtil.hasKey(resultFeaturesJson, "featuresList")) {
                String featuresCreatedCSV = JSONUtil.getString(resultFeaturesJson, "featuresList");
                featuresCreated = HelperMethods.splitString(featuresCreatedCSV, DBPUtilitiesConstants.COMMA_SEPERATOR);

            }
        }
        return featuresCreated;
    }

    @Override
    public void createContractActionLimits(StringBuilder queryString, Map<String, Object> headersMap)
            throws ApplicationException {

    	try {
        	String sb;
            for (int i = 0; i < queryString.length(); i++) {
            	
            		char c = queryString.charAt(i);
            		if (c == '|') {
            			sb= queryString.substring(0,i);
            			
            			if(sb.length() > 2000) {           				
            				Map<String, Object> inputParams = new HashMap<>();
            				
            				inputParams.put("_queryInput", sb.toString());
            				
            				ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
            	                    URLConstants.CONTRACTACTIONLIMITS_CREATE_PROC);
            				
            				sb = new String("");
            				queryString.delete(0,i+1);           				
            			}
            		}
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10359);
        }

    }

    @Override
    public String getActionsWithApproveFeatureAction(String actionsCSV, Map<String, Object> headersMap)
            throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        String actions = null;
        if (StringUtils.isBlank(actionsCSV)) {
            return actions;
        }
        try {
            inputParams.put("_featureActions", actionsCSV);

            JsonObject actionsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.GET_ACTIONS_WITH_APPROVEFEATUREACTION);

            if (JSONUtil.isJsonNotNull(actionsJson)
                    && JSONUtil.hasKey(actionsJson, DBPDatasetConstants.DATASET_RECORDS)
                    && actionsJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
                JsonArray actionsArray = actionsJson.get(DBPDatasetConstants.DATASET_RECORDS)
                        .getAsJsonArray();
                JsonObject object =
                        actionsArray.size() > 0 ? actionsArray.get(0).getAsJsonObject() : new JsonObject();
                actions = object.has("actions") ? object.get("actions").getAsString() : "";
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10394);
        }
        return actions;
    }

    @Override
    public void decreaseUserLimits(StringBuilder queryString, Map<String, Object> headersMap)
            throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        try {
            inputParams.put("_contractActionLimit", queryString);

            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CONTRACT_ACTION_LIMIT_UPDATE_PROC);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10407);
        }

    }

}
