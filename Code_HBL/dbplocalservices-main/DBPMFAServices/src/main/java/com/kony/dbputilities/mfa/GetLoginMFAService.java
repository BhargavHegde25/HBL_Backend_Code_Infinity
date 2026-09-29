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

public class GetLoginMFAService implements JavaService2 {
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
                        HelperMethods.getHeaders(dcRequest), URLConstants.MFA_SERVICE_GET);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

        return returnResult;

    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest) {
        String serviceKey = inputParams.get(MFAConstants.SERVICE_KEY);
        String serviceName = inputParams.get(MFAConstants.SERVICE_NAME);
        if (StringUtils.isBlank(serviceKey) || StringUtils.isBlank(serviceName)) {
            return false;
        }

        String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey + DBPUtilitiesConstants.AND
                + MFAConstants.SERVICE_NAME + DBPUtilitiesConstants.EQUAL + serviceName;
        diagnostic.prepareDebug("GetLoginMFAService filter ##"+ filter).log();

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        return true;
    }
}