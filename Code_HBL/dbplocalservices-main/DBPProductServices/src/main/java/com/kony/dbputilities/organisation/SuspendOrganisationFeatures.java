package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class SuspendOrganisationFeatures implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        Result returnResult = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcessor(inputParams, dcRequest)) {
            String features = inputParams.get("suspendedFeatures");
            if (StringUtils.isBlank(features)) {
                features = dcRequest.getParameter("suspendedFeatures");
            }
            JsonParser parser = new JsonParser();
            JsonArray featuresArray = null;
            try {
                featuresArray = parser.parse(features).getAsJsonArray();
            } catch (JsonParseException e) {
                alert.prepareError(e.getMessage()).log();
            }
            if (null == featuresArray) {
                ErrorCodeEnum.ERR_10206.setErrorCode(returnResult);
                return returnResult;
            }

            returnResult = suspendOrganisationFeature(inputParams, dcRequest, featuresArray);

        }
        if (HelperMethods.hasRecords(returnResult)) {
            HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, returnResult);
            returnResult.removeDatasetById("records");
        }
        return returnResult;

    }

    private Result suspendOrganisationFeature(Map<String, String> inputParams,
            DataControllerRequest dcRequest, JsonArray featuresArray) {
        StringBuilder featuresString = new StringBuilder();
        Result result = new Result();
        String organisationId = inputParams.get("id");
        if (StringUtils.isBlank(organisationId)) {
            organisationId = dcRequest.getParameter("id");
        }
        String organisationType = "TYPE_ID_BUSINESS";
        Map<String, String> input = new HashMap<>();
        for (JsonElement feature : featuresArray) {
            if (JSONUtil.isJsonNotNull(feature) && feature.isJsonPrimitive()) {
                featuresString.append(feature.getAsString());
                featuresString.append(",");
            }
        }
        if (featuresString.length() > 0)
            featuresString.replace(featuresString.length() - 1, featuresString.length(), "");

        input.put("_features", featuresString.toString());
        input.put("_organisationType", organisationType);
        input.put("_organisationId", organisationId);

        try {
            result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATION_FEATURES_SUSPEND_PROC);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        return result;

    }

    private boolean preProcessor(Map<String, String> inputParams, DataControllerRequest dcRequest) {
        return (StringUtils.isNotBlank(inputParams.get("suspendedFeatures"))
                || StringUtils.isNotBlank(dcRequest.getParameter("suspendedFeatures")))
                && (StringUtils.isNotBlank(inputParams.get("id"))
                        || StringUtils.isNotBlank(dcRequest.getParameter("id")));
    }

}
