package com.kony.dbputilities.accountservices;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsPostLogin implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        GetAccouts accountsService = new GetAccouts();

        Result result = new Result();

        result = (Result) accountsService.invoke(methodID, inputArray, dcRequest, dcResponse);

        return result;
    }
}