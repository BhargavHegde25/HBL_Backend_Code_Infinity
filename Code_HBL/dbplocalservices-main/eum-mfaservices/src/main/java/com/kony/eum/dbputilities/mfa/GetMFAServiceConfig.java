package com.kony.eum.dbputilities.mfa;

import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetMFAServiceConfig implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();

        if (dcRequest.getParameter("serviceName") != null) {

            String filter = MFAConstants.SERVICE_NAME + DBPUtilitiesConstants.EQUAL
                    + dcRequest.getParameter("serviceName");

            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.MFA_SERVICE_CONFIG_GET);

        }

        return result;
    }

}