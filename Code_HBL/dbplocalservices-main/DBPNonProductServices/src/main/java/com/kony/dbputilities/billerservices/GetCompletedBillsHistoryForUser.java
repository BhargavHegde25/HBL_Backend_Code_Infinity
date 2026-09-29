package com.kony.dbputilities.billerservices;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetCompletedBillsHistoryForUser implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Param success = new Param(DBPUtilitiesConstants.RESULT_MSG, DBPUtilitiesConstants.SUCCESS_MSG,
                DBPUtilitiesConstants.STRING_TYPE);
        result.addParam(success);
        return result;
    }
}