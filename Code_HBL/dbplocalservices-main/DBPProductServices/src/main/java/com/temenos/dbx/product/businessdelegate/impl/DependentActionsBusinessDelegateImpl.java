package com.temenos.dbx.product.businessdelegate.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.businessdelegate.api.DependentActionsBusinessDelegate;
import com.temenos.dbx.product.dto.DBXResult;

public class DependentActionsBusinessDelegateImpl implements DependentActionsBusinessDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public DBXResult getDependentActions(Map<String, Object> headersMap) throws ApplicationException {
        DBXResult result = new DBXResult();
        try {
            Map<String, Object> inputParams = new HashMap<>();
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.DEPENDENTACTIONS);
            if (response != null && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_DEPENDENTACTIONS)
                    && response.get(DBPDatasetConstants.DATASET_DEPENDENTACTIONS) != null
                    && response.get(DBPDatasetConstants.DATASET_DEPENDENTACTIONS).getAsJsonArray().size() > 0) {
                result.setResponse(processDependenActions(
                        response.get(DBPDatasetConstants.DATASET_DEPENDENTACTIONS).getAsJsonArray()));
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching dependant actions" + e.getMessage()).log();
        }
        return result;
    }

    private Map<String, Set<String>> processDependenActions(JsonArray jsonArray) {
        Map<String, Set<String>> dependantActions = new HashMap<>();
        for (JsonElement jsonelement : jsonArray) {
            String primaryAction = JSONUtil.getString(jsonelement.getAsJsonObject(), "actionId");
            String dependantAction = JSONUtil.getString(jsonelement.getAsJsonObject(), "dependentactionId");
            Set<String> actions = new HashSet<>();
            if (dependantActions.containsKey(primaryAction))
                actions = dependantActions.get(primaryAction);
            actions.add(dependantAction);
            dependantActions.put(primaryAction, actions);
        }
        return dependantActions;
    }
}