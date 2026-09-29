/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.preprocessor.validations;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.*;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.dataobject.Result;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author k.meiyazhagan
 */
public class FeatureActionIdValidation implements ObjectProcessorTask {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        Result result;
        Map<String, String> dataMap = new HashMap<>();

        if (StringUtils.isBlank(HelperMethods.getCustomerIdFromSession(fabricRequestManager))) {
            updateErrorCode(fabricResponseManager);
            return false;
        }

        try {
            JSONObject userPermissions = LegalEntityUtil.getUserCurrentLegalEntityFeaturePermissions(fabricRequestManager);
            OperationData operationData = fabricRequestManager.getServicesManager().getOperationData();
            String filter = "operation" + DBPUtilitiesConstants.EQUAL + operationData.getOperationId();
            filter += DBPUtilitiesConstants.AND + "object_name" + DBPUtilitiesConstants.EQUAL + operationData.getObjectId();
            dataMap.put(DBPUtilitiesConstants.FILTER, filter);
            result = HelperMethods.callApi(fabricRequestManager, dataMap, HelperMethods.getHeaders(fabricRequestManager), URLConstants.SERVICE_PERMISSION_MAPPER_READ);

            String permission = result.getAllDatasets().get(0).getAllRecords().get(0).getParamValueByName("permissions");
            if (getPermittedActionIds(userPermissions, Arrays.asList(permission.split(","))) == null) {
                updateErrorCode(fabricResponseManager);
                return false;
            }
            return true;
        } catch (Exception e) {
            alert.prepareError("Error occurred while validating feature actions. " + e).log();
            updateErrorCode(fabricResponseManager);
            return false;
        }
    }

    private void updateErrorCode(FabricResponseManager fabricResponseManager) {
        JsonObject resPayload = null;
        if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
            resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        }
        resPayload = ErrorCodeEnum.ERR_12007.setErrorCode(resPayload);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
    }

    public static String getPermittedActionIds(JSONObject userPermissions, List<String> requiredActionIds) {
        try {
            Object permissionsObj = userPermissions.get("permissions");

            if (permissionsObj != null) {
                String activeActions = "";
                String permissions = permissionsObj.toString();
                permissions = permissions.replaceAll("\"", "");
                permissions = permissions.substring(1, permissions.length() - 1);

                List<String> permissionList = Arrays.asList(permissions.split(","));
                Set<String> result = permissionList.stream().distinct().filter(requiredActionIds::contains).collect(Collectors.toSet());

                if (!result.isEmpty()) {
                    activeActions = result.toString();
                    activeActions = activeActions.replaceAll("\\s", "");
                    activeActions = activeActions.substring(1, activeActions.length() - 1);
                    return activeActions;
                }
            }

        } catch (NullPointerException e) {
            alert.prepareError("Error occurred while fetching feature actions. " + e).log();
        }
        return null;
    }
}