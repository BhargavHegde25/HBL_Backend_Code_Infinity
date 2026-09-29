package com.kony.eum.dbputilities.mfa;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetOTP implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest)) {
            result = HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                    HelperMethods.getHeaders(dcRequest), URLConstants.OTP_GET);
        }

        return result;
    }
    
    public Object invoke(Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest)) {
            try {
				result = HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
				        HelperMethods.getHeaders(dcRequest), URLConstants.OTP_GET);
			} catch (HttpCallException e) {
				
				alert.prepareError(e.toString()).log();
			}
        }

        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest) {
        String securityKey = inputParams.get(MFAConstants.SECURITY_KEY);
        String serviceKey = inputParams.get(MFAConstants.SERVICE_KEY);
        if (StringUtils.isBlank(securityKey)) {
            return false;
        }
        
        if(StringUtils.isBlank(serviceKey)) {
        	serviceKey = dcRequest.getParameter("serviceKey");
        }
        if(StringUtils.isBlank(securityKey)) {
        	securityKey = dcRequest.getParameter("securityKey");
        }
        

        String filter = "";

        if (StringUtils.isNotBlank(securityKey)) {
            filter += "securityKey" + DBPUtilitiesConstants.EQUAL + securityKey;
        }

        if (StringUtils.isNotBlank(serviceKey)) {
            if (!filter.isEmpty()) {
                filter += DBPUtilitiesConstants.AND;
            }
            filter += "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey;
        }

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        return true;
    }
    
}