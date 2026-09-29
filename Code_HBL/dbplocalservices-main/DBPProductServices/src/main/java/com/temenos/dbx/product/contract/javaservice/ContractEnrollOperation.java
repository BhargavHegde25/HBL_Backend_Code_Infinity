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
import com.temenos.dbx.product.usermanagement.resource.api.InfinityUserManagementResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class ContractEnrollOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            // added isDefaultActionsEnabled param in request to create default actions for features while creating the
            // contract
            request.addRequestParam_("isDefaultActionsEnabled", "true");
            ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
            InfinityUserManagementResource userManagementResource =
                    DBPAPIAbstractFactoryImpl.getResource(InfinityUserManagementResource.class);
            boolean status = resource.validateEnrollContractPayload(methodId, inputArray, request, response);
            if (status) {
                result = resource.createContract(methodId, inputArray, request, response);

                userManagementResource.createAUserAndAssignTOGivenContract(methodId, inputArray, request, response);
            } else {
                ErrorCodeEnum.ERR_10328.setErrorCode(result);
            }
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while creating a contract " + e.getStackTrace()).log();
            ErrorCodeEnum.ERR_10390.setErrorCode(result);
        }

        return result;
    }

}
