package com.temenos.dbx.eum.product.contract.javaservice;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.contract.resource.api.ContractResource;

public class CoreCustomerBasedContractDetailsGetOperation implements JavaService2 {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
            result = resource.getCoreCustomerBasedContractDetails(methodId, inputArray, request, response);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured while fetching the core customer based contract details" + e.getMessage()).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the core customer based contract details" + e.getMessage()).log();
            ErrorCodeEnum.ERR_10781.setErrorCode(result);
        }
        return result;
    }

}
