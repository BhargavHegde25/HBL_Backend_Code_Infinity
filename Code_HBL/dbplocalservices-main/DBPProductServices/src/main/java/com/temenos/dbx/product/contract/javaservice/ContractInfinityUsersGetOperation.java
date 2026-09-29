package com.temenos.dbx.product.contract.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.contract.resource.api.ContractResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class ContractInfinityUsersGetOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
            result = resource.getContractInfinityUsers(methodId, inputArray, request, response);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
        } catch (Exception e) {
            ErrorCodeEnum.ERR_10375.setErrorCode(result);
        }

        return result;
    }

}
