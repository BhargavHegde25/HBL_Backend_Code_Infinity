package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

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

public class CreateCustomerTNCForAccountClosure implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        String termsAndConditionsCode = null;
        String language = DBPUtilitiesConstants.TNC_DEFAULT_LANGUAGE;
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        termsAndConditionsCode = DBPUtilitiesConstants.AccountClosure_TnC_TnC;

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
            return result;
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
                    return result;
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
        params.put("channelId", "Online");
        params.put("termsNConditionContentId",
                termsAndConditionsFromAdmin.getParamValueByName("contentId"));
        params.put("consentGiven", "true");
        params.put("partyId", customerId);

        alert.prepareError("create party consent details params " + params).log();
        diagnostic.prepareDebug("create party consent details params " + params).log();
        HelperMethods.removeNullValues(params);
        result = ServiceCallHelper.invokeServiceAndGetResult(dcRequest, params,
                HelperMethods.getHeaders(dcRequest), URLConstants.CREATE_PARTY_CONSENT_DETAILS);

        if (result.hasParamByName("partyConsentId")) {
            result.removeParamByName("dbpErrCode");
            result.removeParamByName("dbpErrMsg");
        }
        return result;
    }

}
