package com.kony.testscripts.getscripts;

import java.util.HashSet;
import java.util.Set;

import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GenerateRandomId implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        int numberOfRandomStrings = Integer.valueOf(request.getParameter("count"));
        Result result = new Result();
        Set<String> randomIds = new HashSet<>();
        for (int i = 0; i < numberOfRandomStrings; i++) {
            randomIds.add(HelperMethods.getUniqueNumericString(9));
        }
        result.addStringParam("randomIds", randomIds.toString());
        return result;
    }

}
