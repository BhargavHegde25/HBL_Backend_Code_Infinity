package com.temenos.infinity.api.arrangements.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.resource.api.GetAccountsBySearchResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class GetAccountsByCoreCustomerIdSearchOperation implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            GetAccountsBySearchResource resourceInstance =
                    DBPAPIAbstractFactoryImpl.getResource(GetAccountsBySearchResource.class);
            result = resourceInstance.getAccountsByCoreCustomerIdSearch(methodID, inputArray, request, response);
        } catch (Exception e) {
            ErrorCodeEnum.ERR_20057.setErrorCode(result);
        }
        return result;
    }

}
