package com.kony.dbputilities.transservices;

import java.math.BigDecimal;
import java.util.Map;

import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class IsSecondFactorAuthenticationRequired implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        String transAmount = (String) inputParams.get("amount");
        BigDecimal amount = new BigDecimal(1000);
        Param p = new Param();
        p.setName("result");
        if (new BigDecimal(transAmount).compareTo(amount) < 0) {
            p.setValue("false");
        } else {
            p.setValue("true");
        }
        return result;
    }
}
