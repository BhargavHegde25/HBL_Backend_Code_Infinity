package com.temenos.auth.admininteg.operation;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class Logout implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
//        dcRequest.getSession().invalidate();
//        dcRequest.getSession().setMaxInactiveInterval(1);
//        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
//        MemoryManager.removeFromCache(DBPUtilitiesConstants.ACCOUNTS_POSTLOGIN_CACHE_KEY + customerId);
        Result result = new Result();
//        Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, DBPUtilitiesConstants.SUCCESS_MSG,
//                DBPUtilitiesConstants.STRING_TYPE);
//        result.addParam(p);
        return result;
    }
}