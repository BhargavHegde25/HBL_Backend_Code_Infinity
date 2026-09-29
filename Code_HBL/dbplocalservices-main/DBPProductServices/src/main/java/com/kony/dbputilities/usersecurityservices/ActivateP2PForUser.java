package com.kony.dbputilities.usersecurityservices;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.UserAgentUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ActivateP2PForUser implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.USER_UPDATE);
        }
        if (!HelperMethods.hasError(result)) {
            result = new Result();
            Param success = new Param(DBPUtilitiesConstants.RESULT_MSG, "Successful",
                    DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            result.addParam(success);
        }
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, JSONException, UnsupportedEncodingException {
        boolean status = true;
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        status = isP2pActivated(dcRequest, userId);
        if (!status) {
            makeEntryInTnC(inputParams, dcRequest);
        }
        inputParams.remove("userName");
        inputParams.put(DBPUtilitiesConstants.U_ID, userId);
        inputParams.put(DBPUtilitiesConstants.IS_P2P_ACTIVE, String.valueOf(!status));
        return true;
    }

    /*
     * private boolean isP2pActivated(String userId) throws HttpCallException { String query = "Id" +
     * DBPUtilitiesConstants.EQUAL + userId; JsonObject response = LocalServiceValidations.callGetApi(
     * URLConstants.USER_GET, query); JsonArray ele = (JsonArray) response.get(DBPUtilitiesConstants.DS_USER); if (null
     * != ele && 0 != ele.size()) { JsonObject jobj = (JsonObject) ele.get(0); return null != jobj.get("isP2PActivated")
     * && jobj.get("isP2PActivated").getAsBoolean(); } return false; }
     */

    private boolean isP2pActivated(DataControllerRequest dcRequest, String userId) throws HttpCallException {
        String query = "Id" + DBPUtilitiesConstants.EQUAL + userId;
        Result user = HelperMethods.callGetApi(dcRequest, query, HelperMethods.getHeaders(dcRequest),
                URLConstants.USER_GET);
        String isP2PActivated = HelperMethods.getFieldValue(user, "isP2PActivated");
        return StringUtils.isBlank(isP2PActivated) ? false : Boolean.parseBoolean(isP2PActivated);
    }

    private void makeEntryInTnC(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException, JSONException, UnsupportedEncodingException {
        String termsAndConditionsCode = null;
        String language = DBPUtilitiesConstants.TNC_DEFAULT_LANGUAGE;
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        Result result = new Result();
        termsAndConditionsCode = DBPUtilitiesConstants.TNC_LOGIN;

        if (StringUtils.isNotBlank(inputParams.get("languageCode"))) {
            language = inputParams.get("languageCode");
        }

        inputParams.put("termsAndConditionsCode", termsAndConditionsCode);
        inputParams.put("languageCode", language);
        inputParams.put("appId", HelperMethods.getAppId(dcRequest));

        Result termsAndConditionsFromAdmin =
                AdminUtil.invokeAPI(inputParams, URLConstants.GET_TERMS_AND_CONDITIONS, dcRequest);
        if (HelperMethods.hasDBPErrorMSG(termsAndConditionsFromAdmin)) {
            ErrorCodeEnum.ERR_10185.setErrorCode(result);
            return;
        }

        UserAgentUtil ua = new UserAgentUtil(dcRequest);
        Map<String, Object> params = new HashMap<>();

        params.clear();
        params.put("partyId", customerId);

        String authToken = AdminUtil.getForceLoginAccessToken();
        Result partyConsentDetails = ServiceCallHelper.invokeServiceAndGetResult(params,
                HelperMethods.getHeaders(dcRequest), URLConstants.GET_PARTY_CONSENT_DETAILS, authToken);

        if (partyConsentDetails != null) {
            Dataset ds = partyConsentDetails.getDatasetById("partyConsentDetails");
            if (ds != null && ds.getAllRecords().size() > 0) {
                String contentId = ds.getAllRecords().get(0).getParamValueByName("termsNConditionContentId");
                String globalContentId = termsAndConditionsFromAdmin.getParamValueByName("contentId");
                if (contentId != null && globalContentId != null && contentId.equalsIgnoreCase(globalContentId)) {
                    result.addParam(new Param("alreadySigned", "true", DBPUtilitiesConstants.STRING_TYPE));
                    return;
                }
            }
        }

        params.clear();
        params.put("masterConsentId", DBPUtilitiesConstants.CONSENT_MASTER_ID);
        params.put("bankName", "Infinity Bank");
        params.put("externalUserId", customerId);
        params.put("retentionPeriod", "30");
        params.put("status", "ACTIVE");
        params.put("backendIdentifier", customerId);
        params.put("channelId", ua.getChannel());
        params.put("termsNConditionContentId",
                termsAndConditionsFromAdmin.getParamValueByName("contentId"));
        params.put("consentGiven", "true");
        params.put("partyId", customerId);

        HelperMethods.removeNullValues(params);
        result = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, params,
                HelperMethods.getHeaders(dcRequest), URLConstants.CREATE_PARTY_CONSENT_DETAILS);

        return;
    }

    private String getAppID(DataControllerRequest dcRequest) {
        Map<String, String> headers = HelperMethods.getHeadersWithReportingParams(dcRequest);
        String reportingParams = headers.get(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS);
        String appId = "";
        if (StringUtils.isNotBlank(reportingParams)) {
            JSONObject reportingParamsJson = new JSONObject();
            try {
                reportingParamsJson = new JSONObject(
                        URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
            } catch (JSONException e) {

                alert.prepareError(e.getMessage()).log();
            } catch (UnsupportedEncodingException e) {

                alert.prepareError(e.getMessage()).log();
            }

            appId = reportingParamsJson.optString("aid");
        }

        return appId;
    }

}