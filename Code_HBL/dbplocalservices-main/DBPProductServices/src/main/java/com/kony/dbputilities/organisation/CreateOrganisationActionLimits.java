package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateOrganisationActionLimits {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private CreateOrganisationActionLimits() {

    }

    public static Result invoke(Map<String, String> inputParams, DataControllerRequest dcRequest,
            String organisationType, Set<String> actionsSet) {

        Result returnResult = new Result();
        if (preProcess(inputParams, dcRequest, organisationType)) {

            String actionLimits = inputParams.get(DBPUtilitiesConstants.ORG_ACTIONLIMITS);
            if (StringUtils.isBlank(actionLimits)) {
                actionLimits = dcRequest.getParameter(DBPUtilitiesConstants.ORG_ACTIONLIMITS);
            }
            JsonParser parser = new JsonParser();
            JsonArray actionLimitsArray = null;
            try {
                actionLimitsArray = parser.parse(actionLimits).getAsJsonArray();
            } catch (JsonParseException e) {
                alert.prepareError(e.getMessage()).log();

            }
            if (null == actionLimitsArray) {
                ErrorCodeEnum.ERR_10199.setErrorCode(returnResult);
                return returnResult;
            }

            updateOrganisationActionLimits(inputParams, dcRequest, actionLimitsArray, organisationType, actionsSet);

        }
        Dataset dataset = new Dataset();
        Record record = new Record();
        record.addParam(new Param("success", "success", DBPUtilitiesConstants.STRING_TYPE));
        dataset.addRecord(record);
        returnResult.addDataset(dataset);
        return returnResult;

    }

    private static void updateOrganisationActionLimits(Map<String, String> inputParams,
            DataControllerRequest dcRequest, JsonArray actionLimitsArray, String organisationType,
            Set<String> actionsSet) {
        String actionId = null;
        JsonArray limits = null;
        Result result = null;
        String limitId = null;
        String limitValue = null;
        String orgId = inputParams.get("id");
        if (StringUtils.isBlank(orgId)) {
            orgId = dcRequest.getParameter("id");
        }
        Map<String, String> input = new HashMap<>();
        input.put("Organisation_id", orgId);

        for (JsonElement element : actionLimitsArray) {
            JsonObject object = (JSONUtil.isJsonNotNull(element) && element.isJsonObject()) ? element.getAsJsonObject()
                    : null;
            if (JSONUtil.isJsonNotNull(object)) {
                actionId = object.get("id").getAsString();
                limits = object.get("limits").getAsJsonArray();
                input.put("Action_id", actionId);
            }
            if (JSONUtil.isJsonNotNull(limits) && StringUtils.isNotBlank(actionId)) {
                for (JsonElement limit : limits) {
                    limitId = JSONUtil.hasKey(limit.getAsJsonObject(), "id")
                            ? limit.getAsJsonObject().get("id").getAsString()
                            : null;
                    limitValue = JSONUtil.hasKey(limit.getAsJsonObject(), "id")
                            ? limit.getAsJsonObject().get("value").getAsString()
                            : null;
                    if (!checkIfLimitValueisAcceptable(actionId, limitId, limitValue, dcRequest)) {
                        continue;
                    }
                    input.put("LimitType_id", limitId);
                    input.put("value", limitValue);
                    if (actionPresentInOrgActionsList(actionId, actionsSet)) {
                        String filter = "Organisation_id" + DBPUtilitiesConstants.EQUAL + orgId
                                + DBPUtilitiesConstants.AND
                                + "Action_id" + DBPUtilitiesConstants.EQUAL + actionId
                                + DBPUtilitiesConstants.AND
                                + "LimitType_id" + DBPUtilitiesConstants.EQUAL + limitId;

                        try {
                            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                                    URLConstants.ORGANISATION_ACTION_LIMIT_GET);
                        } catch (HttpCallException e) {
                            alert.prepareError(e.getMessage()).log();
                        }

                        String id = HelperMethods.getFieldValue(result, "id");
                        if (StringUtils.isNotBlank(id)) {
                            input.put("id", id);
                            try {
                                result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                                        URLConstants.ORGANISATION_ACTION_LIMIT_UPDATE);
                            } catch (HttpCallException e) {
                                alert.prepareError(e.getMessage()).log();
                            }
                        }

                    }

                }

            }

        }

    }

    private static boolean checkIfLimitValueisAcceptable(String actionId, String limitId, String limitValue,
            DataControllerRequest dcRequest) {
        Result result = null;
        Double inputLimitValue = null;
        Double fiLimitValue = null;
        if (StringUtils.isBlank(limitId) || StringUtils.isBlank(limitValue)) {
            return false;
        }
        String filter = "Action_id" + DBPUtilitiesConstants.EQUAL + actionId
                + DBPUtilitiesConstants.AND
                + "LimitType_id" + DBPUtilitiesConstants.EQUAL + limitId;
        try {
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACTION_LIMIT_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        String fiLevelLimit = HelperMethods.getFieldValue(result, "value");
        inputLimitValue = Double.parseDouble(limitValue);
        fiLimitValue = StringUtils.isNotBlank(fiLevelLimit) ? Double.parseDouble(fiLevelLimit) : 0;

        return inputLimitValue < fiLimitValue;
    }

    private static boolean actionPresentInOrgActionsList(String actionId, Set<String> actionsSet) {
        return (actionsSet == null || actionsSet.contains(actionId));
    }

    private static boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest,
            String organisationType) {
        return ((StringUtils.isNotBlank(inputParams.get(DBPUtilitiesConstants.ORG_ACTIONLIMITS))
                || StringUtils.isNotBlank(dcRequest.getParameter(DBPUtilitiesConstants.ORG_ACTIONLIMITS)))
                && StringUtils.isNotBlank(organisationType));
    }

}
