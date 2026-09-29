package com.temenos.dbx.eum.product.contract.javaservice;
import com.hbl.infinity.accounts.perf.invalidation.GetListCacheInvalidator;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.contract.resource.api.ContractResource;
import com.temenos.dbx.eum.product.usermanagement.resource.api.InfinityUserManagementResource;

public class ContractEnrollOperation implements JavaService2 {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        try {
            return invokeOperation(methodId, inputArray, request, response);
        } finally {
            // getList cache: this operation changes data getList returns. Runs even after a part-way
            // failure, because some rows may already be written.
            GetListCacheInvalidator.permissionsChanged();
        }
    }

    private Object invokeOperation(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            // added isDefaultActionsEnabled param in request to create default actions for features while creating the
            // contract
            request.addRequestParam_("isDefaultActionsEnabled", "true");
//            String legalEntityId = EnvironmentConfigurationsHandler
//    				.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
//            HelperMethods.getInputParamMap(inputArray).put("legalEntityId",legalEntityId);
//            request.addRequestParam_("legalEntityId", legalEntityId);
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
            alert.prepareError("Exception occured while creating a contract ",e).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while creating a contract " ,e).log();
            ErrorCodeEnum.ERR_10390.setErrorCode(result);
        }

        return result;
    }

}
