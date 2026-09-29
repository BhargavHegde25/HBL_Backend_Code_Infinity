package com.kony.dbputilities.accountservices;

import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.kony.dbputilities.customersecurityservices.createOrgEmployeeAccounts;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.UserAgentUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateUserAccountSettings implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        try {
            return invokeOperation(methodID, inputArray, dcRequest, dcResponse);
        } finally {
            // getList cache: this operation changes data getList returns. Runs even after a part-way
            // failure, because some rows may already be written.
            GetListCacheInvalidator.sessionCustomerChanged(dcRequest);
        }
    }

    private Object invokeOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> businessUserAccountUpdate = new HashMap<>();

        Map<String, String> userProfile = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String loggedInUser = userProfile.get("UserName");
        String loggedInUserID = userProfile.get("customer_id");

        if (StringUtils.isBlank(loggedInUser)) {
            HelperMethods.setValidationMsgwithCode(ErrorConstants.SECURITY_ERROR, ErrorCodes.SECURITY_ERROR, result);
            return result;
        }

        String customerType = userProfile.get("customerType");

        if (preProcess(inputParams, businessUserAccountUpdate, dcRequest, customerType, loggedInUserID, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_UPDATE);
        }

        if (StringUtils.isNotBlank(businessUserAccountUpdate.get("id"))) {
            businessUserAccountUpdate.put("modifiedby", loggedInUser);
            businessUserAccountUpdate.put("lastmodifiedts", HelperMethods.getCurrentTimeStamp());
            HelperMethods.callApi(dcRequest, businessUserAccountUpdate, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERACCOUNTS_UPDATE);
        }

        if (result.hasParamByName("updatedRecords")) {
            try {
                if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0)
                    result.addStringParam("status", "success");
            } catch (Exception e) {
                diagnostic.prepareDebug("Couldn't Parse updated records Integer from String").log();
            }
        }

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, Map businessUserAccountUpdate, DataControllerRequest dcRequest,
            String customerType, String userid, Result result) throws HttpCallException, JSONException, UnsupportedEncodingException {

        String accountId = (String) inputParams.get("accountID");
        String favouriteStatus = (String) inputParams.get("favouriteStatus");
        if (StringUtils.isBlank(accountId)) {
            HelperMethods.setValidationMsgwithCode(ErrorConstants.INVALID_DETAILS,
                    ErrorCodes.ERROR_SEARCHING_RECORD_MANDATORY_INFORMATION_MISS, result);
            return false;
        }

        inputParams.put("Account_id", accountId);
        inputParams.put("NickName", inputParams.get("nickName"));
        inputParams.put("FavouriteStatus", favouriteStatus);
        inputParams.put("EStatementmentEnable", inputParams.get("eStatementEnable"));
        inputParams.remove("accountID");
        boolean setFavourite = ("1".equals(favouriteStatus) || "true".equals(favouriteStatus)) ? true : false;
        if (inputParams.get("eStatementEnable") == "1") {
            makeEntryInTnC(inputParams, dcRequest);
        }
        String filter = DBPUtilitiesConstants.CUSTOMER_ID + DBPUtilitiesConstants.EQUAL + userid
                + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.ACCOUNT_ID + DBPUtilitiesConstants.EQUAL
                + accountId;

        createOrgEmployeeAccounts accountsHelper = new createOrgEmployeeAccounts();
        Result existingAccounts = accountsHelper.getExistingAccounts(filter, userid, dcRequest);
        if (!HelperMethods.hasRecords(existingAccounts)) {
            HelperMethods.setValidationMsgwithCode(ErrorConstants.INVALID_ACCOUNT_ACCESS, ErrorCodes.SECURITY_ERROR,
                    result);
            return false;
        }
        List<Record> accounts = existingAccounts.getAllDatasets().get(0).getAllRecords();
        for (Record accountRecord : accounts) {
            String customer_acct_mapping_id = HelperMethods.getFieldValue(accountRecord,
                    DBPUtilitiesConstants.UN_ID);
            String account_id_retrieved = HelperMethods.getFieldValue(accountRecord,
                    DBPUtilitiesConstants.ACCOUNT_ID);
            String favourite_status_retrieved = HelperMethods.getFieldValue(accountRecord, "FavouriteStatus");
            boolean fav_status_retrieved = ("1".equals(favourite_status_retrieved)
                    || "true".equalsIgnoreCase(favourite_status_retrieved)) ? true : false;

            if (accountId.equals(account_id_retrieved)) {
                businessUserAccountUpdate.put("id", customer_acct_mapping_id);
                businessUserAccountUpdate.put("FavouriteStatus", inputParams.get("favouriteStatus"));
                businessUserAccountUpdate.put("NickName", inputParams.get("nickName"));
                businessUserAccountUpdate.put("FavouriteStatus", favouriteStatus);
                businessUserAccountUpdate.put("EStatementmentEnable", inputParams.get("eStatementEnable"));
                businessUserAccountUpdate.put("email", inputParams.get("email"));
                return true;
            }
        }
        return false;
    }

    private void makeEntryInTnC(Map<String,String> inputParams, DataControllerRequest dcRequest)
            throws JSONException, UnsupportedEncodingException, HttpCallException {
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
