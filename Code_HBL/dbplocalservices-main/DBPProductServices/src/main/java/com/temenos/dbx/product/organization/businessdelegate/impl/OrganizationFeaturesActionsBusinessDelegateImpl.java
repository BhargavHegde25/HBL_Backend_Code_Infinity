package com.temenos.dbx.product.organization.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.dto.OrganizationsFeatureActionsDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationFeaturesActionsBusinessDelegate;

public class OrganizationFeaturesActionsBusinessDelegateImpl implements OrganizationFeaturesActionsBusinessDelegate {

    @Override
    public boolean createOrganizationFeatures(OrganizationsFeatureActionsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        JsonObject featuresJson = null;
        try {
            inputParams.put("_features", dto.getFeatures());
            inputParams.put("_organisationType", dto.getOrganisationType());
            inputParams.put("_organisationId", dto.getOrganisationId());

            featuresJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATION_FEATURES_CREATE_PROC);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10288);
        }

        return JSONUtil.isJsonNotNull(featuresJson)
                && JSONUtil.hasKey(featuresJson, DBPDatasetConstants.DATASET_RECORDS)
                && featuresJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray();
    }

    @Override
    public String createOrganizationActionLimits(OrganizationsFeatureActionsDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        String actions = null;
        try {
            inputParams.put("_features", dto.getFeatures());
            inputParams.put("_organisationType", dto.getOrganisationType());
            inputParams.put("_organisationId", dto.getOrganisationId());

            JsonObject actionsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ORGANISATION_ACTIONS_CREATE_PROC);

            if (JSONUtil.isJsonNotNull(actionsJson)
                    && JSONUtil.hasKey(actionsJson, DBPDatasetConstants.DATASET_RECORDS)
                    && actionsJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
                JsonArray actionsArray = actionsJson.get(DBPDatasetConstants.DATASET_RECORDS)
                        .getAsJsonArray();
                JsonObject object =
                        actionsArray.size() > 0 ? actionsArray.get(0).getAsJsonObject() : new JsonObject();
                actions = object.has("actionslist") ? object.get("actionslist").getAsString() : "";
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10289);
        }
        return actions;
    }

    @Override
    public String getMonetaryActions(String actionsCSV, Map<String, Object> headersMap) throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        String monetaryActions = null;
        if (StringUtils.isBlank(actionsCSV)) {
            return monetaryActions;
        }
        try {
            inputParams.put("_featureActions", actionsCSV);

            JsonObject actionsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.GET_MONETARY_ACTIONS);

            if (JSONUtil.isJsonNotNull(actionsJson)
                    && JSONUtil.hasKey(actionsJson, DBPDatasetConstants.DATASET_RECORDS)
                    && actionsJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
                JsonArray actionsArray = actionsJson.get(DBPDatasetConstants.DATASET_RECORDS)
                        .getAsJsonArray();
                JsonObject object =
                        actionsArray.size() > 0 ? actionsArray.get(0).getAsJsonObject() : new JsonObject();
                monetaryActions = object.has("monetaryActions") ? object.get("monetaryActions").getAsString() : "";
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10298);
        }
        return monetaryActions;
    }

    @Override
    public String createCustomerActions(String actionsCSV, String accountsCSV, String customerId, String businessTypeId,
            String groupId, Map<String, Object> headersMap) throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        String actions = null;
        try {
            inputParams.put("_customerActionsCSV", actionsCSV);
            inputParams.put("_accountsCSV", accountsCSV);
            inputParams.put("_customerId", customerId);
            inputParams.put("_businessTypeId", businessTypeId);
            inputParams.put("_groupId", groupId);

            JsonObject actionsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CUSTOMERACTIONS_CREATE_PROC);

            if (JSONUtil.isJsonNotNull(actionsJson)
                    && JSONUtil.hasKey(actionsJson, DBPDatasetConstants.DATASET_RECORDS)
                    && actionsJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {
                JsonArray actionsArray = actionsJson.get(DBPDatasetConstants.DATASET_RECORDS)
                        .getAsJsonArray();
                JsonObject object =
                        actionsArray.size() > 0 ? actionsArray.get(0).getAsJsonObject() : new JsonObject();
                actions = object.has("actionslist") ? object.get("actionslist").getAsString() : "";
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10289);
        }
        return actions;
    }

}
