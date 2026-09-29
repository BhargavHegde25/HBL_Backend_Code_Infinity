package com.kony.dbputilities.customersecurityservices;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerTermsAndConditionsPreLogin implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        String termsAndConditionsCode = null;
        String language = null;
        boolean status = false;
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        language = inputParams.get("languageCode");
        termsAndConditionsCode = inputParams.get("termsAndConditionsCode");
        if (StringUtils.isBlank(termsAndConditionsCode)) {
            ErrorCodeEnum.ERR_10189.setErrorCode(result);
            return result;
        }
        Set<String> scenarios = new HashSet<>();
        scenarios.add(DBPUtilitiesConstants.TNC_ENROLL);
        scenarios.add(DBPUtilitiesConstants.TNC_COMMON);
        scenarios.add(DBPUtilitiesConstants.TNC_HAMBURGER);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING);
        scenarios.add(DBPUtilitiesConstants.TNC_BUSINESSENROLL);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_OFAC);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_ESIGN_AGREEMENT);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_GOOGLEMAPS_DISCLOSURE);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_FUNDING_OTP_DISCLAIMER);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_FUNDING_DISCLAIMER);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_FUNDING_ACKNOWLEDGEMENT_DISCLAIMER);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_IDSCAN_DISCLAIMER);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_PRODUCTDASHBOARD_DISCLAIMER);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_UPDATE_PASSWORD_RULES);
        scenarios.add(DBPUtilitiesConstants.TNC_ONBOARDING_COAPPLICANT_SECTION);
        scenarios.add(DBPUtilitiesConstants.TNC_ORIGINATIONSUBMIT);
        
        for (String scenario : scenarios) {
            if (termsAndConditionsCode.equals(scenario)) {
                status = true;
                break;
            }
        }

        if (!status) {
            ErrorCodeEnum.ERR_10189.setErrorCode(result);
            return result;
        }

        inputParams.put("appId", HelperMethods.getAppId(dcRequest));
        HelperMethods.removeNullValues(inputParams);
        Result termsAndConditionsFromAdmin =
                AdminUtil.invokeAPI(inputParams, URLConstants.GET_TERMS_AND_CONDITIONS, dcRequest);
        if (HelperMethods.hasDBPErrorMSG(termsAndConditionsFromAdmin)) {
            ErrorCodeEnum.ERR_10190.setErrorCode(result);
            return result;
        }

        return termsAndConditionsFromAdmin;
    }
}
