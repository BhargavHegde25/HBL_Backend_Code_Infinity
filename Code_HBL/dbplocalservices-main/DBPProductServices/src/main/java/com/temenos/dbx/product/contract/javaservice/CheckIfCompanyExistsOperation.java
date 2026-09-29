package com.temenos.dbx.product.contract.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.contract.resource.api.CoreCustomerResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class CheckIfCompanyExistsOperation implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            CoreCustomerResource resource = DBPAPIAbstractFactoryImpl.getResource(CoreCustomerResource.class);
            result = resource.checkCoreRelativeCustomerForCompany(methodId, inputArray, request, response);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
        } catch (Exception e) {
            ErrorCodeEnum.ERR_10230.setErrorCode(result);
        }
        return result;
    }

}
