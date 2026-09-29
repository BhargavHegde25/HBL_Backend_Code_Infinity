package com.kony.eum.dbputilities.customersecurityservices;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.UserAgentUtil;
import com.kony.eum.dbputilities.customersecurityservices.CreateCustomerTermsandConditionsPreLogin;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class CreateCustomerTermsandConditionsPreLogin {
    private CreateCustomerTermsandConditionsPreLogin() {

    }

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static Result invoke(String customerId, String termsAndConditionsCode, String localeId,
            DataControllerRequest dcRequest) {
        Result returnResult = new Result();

        if (StringUtils.isBlank(customerId) || StringUtils.isBlank(termsAndConditionsCode)) {
            ErrorCodeEnum.ERR_10201.setErrorCode(returnResult);
            return returnResult;
        }
        String language = localeId;
        if (StringUtils.isBlank(localeId)) {
            language = DBPUtilitiesConstants.TNC_DEFAULT_LANGUAGE;
        }
        String versionIdfromAdmin = null;
        Map<String, String> input = new HashMap<>();
        input.put("termsAndConditionsCode", termsAndConditionsCode);
        input.put("languageCode", language);

        Result termsAndConditionsFromAdmin = new Result();
        try {
            termsAndConditionsFromAdmin = AdminUtil.invokeAPI(input, URLConstants.GET_TERMS_AND_CONDITIONS, dcRequest);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        if (HelperMethods.hasDBPErrorMSG(termsAndConditionsFromAdmin)) {
            ErrorCodeEnum.ERR_10202.setErrorCode(returnResult);
            return returnResult;
        }
        versionIdfromAdmin = termsAndConditionsFromAdmin.getParamValueByName("versionId");

        if (StringUtils.isBlank(versionIdfromAdmin)) {
            ErrorCodeEnum.ERR_10202.setErrorCode(returnResult);
            return returnResult;
        }

        UserAgentUtil ua = null;
        try {
            ua = new UserAgentUtil(dcRequest);
        } catch (JSONException | UnsupportedEncodingException e) {
            alert.prepareError(e.getMessage()).log();
        }

        if (null == ua) {
            ErrorCodeEnum.ERR_10202.setErrorCode(returnResult);
            return returnResult;
        }

        input.put("id", UUID.randomUUID().toString());
        input.put("customerId", customerId);
        input.put("versionId", versionIdfromAdmin);
        input.put("languageCode", language);
        input.put("appId", ua.getAppID());
        input.put("channel", ua.getChannel());
        input.put("platform", ua.getPlatform());
        input.put("browser", ua.getBrowser());

        try {
            returnResult = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_TERMSANDCONDITIONS_CREATE);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        return returnResult;

    }

}
