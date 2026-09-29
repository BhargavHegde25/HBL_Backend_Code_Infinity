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
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class RemoveOrganisationFeatures {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private RemoveOrganisationFeatures() {

    }

    public static Result invoke(Map<String, String> inputParams, DataControllerRequest dcRequest,
            String organisationType) {

        Result returnResult = new Result();
        if (preProcessor(inputParams, dcRequest, organisationType)) {
            String features = inputParams.get("removedFeatures");
            if (StringUtils.isBlank(features)) {
                features = dcRequest.getParameter("removedFeatures");
            }
            JsonParser parser = new JsonParser();
            JsonArray featuresArray = null;
            try {
                featuresArray = parser.parse(features).getAsJsonArray();
            } catch (JsonParseException e) {
                alert.prepareError(e.getMessage()).log();
            }
            if (null == featuresArray) {
                ErrorCodeEnum.ERR_10205.setErrorCode(returnResult);
                return returnResult;
            }

            returnResult = removeOrganisationFeatureActions(inputParams, dcRequest, organisationType, featuresArray);

        }
        return returnResult;

    }

    private static Result removeOrganisationFeatureActions(Map<String, String> inputParams,
            DataControllerRequest dcRequest,
            String organisationType, JsonArray featuresArray) {
        StringBuilder featuresString = new StringBuilder();
        Result result = null;
        String organisationId = inputParams.get("id");
        if (StringUtils.isBlank(organisationId)) {
            organisationId = dcRequest.getParameter("id");
        }
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
                    URLConstants.ORGANISATION_FEATURES_DELETE_PROC);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }

        try {
            result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATION_ACTIONS_DELETE_PROC);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        return result;

    }

    private static boolean preProcessor(Map<String, String> inputParams, DataControllerRequest dcRequest,
            String organisationType) {
        return ((StringUtils.isNotBlank(inputParams.get("removedFeatures"))
                || StringUtils.isNotBlank(dcRequest.getParameter("removedFeatures")))
                && StringUtils.isNotBlank(organisationType));
    }

}
