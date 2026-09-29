package com.kony.dbputilities.mfa;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetLoginOTP implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Result returnResult = new Result();
        if (preProcess(inputParams, dcRequest)) {
            try {
                return HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                        HelperMethods.getHeaders(dcRequest), URLConstants.OTP_GET);

            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

        return returnResult;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest) {
        String securityKey = inputParams.get(MFAConstants.SECURITY_KEY);
        String serviceKey = inputParams.get(MFAConstants.SERVICE_KEY);
        if (StringUtils.isBlank(securityKey)) {
            return false;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(MFAConstants.SECURITY_KEY).append(DBPUtilitiesConstants.EQUAL).append(securityKey);
        sb.append(DBPUtilitiesConstants.AND);
        sb.append(MFAConstants.SERVICE_KEY).append(DBPUtilitiesConstants.EQUAL).append(serviceKey);

        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());

        return true;
    }
}