package com.kony.dbputilities.mfa;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetLoginCustomerID implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();

        String CustomerID = HelperMethods.getCustomerIdFromKnownUserToken(dcRequest);

        if(StringUtils.isBlank(CustomerID)) {
            CustomerID = HelperMethods.getCustomerIdFromSession(dcRequest);
        }

        result.addParam("customerId", CustomerID);

        return result;
    }

}