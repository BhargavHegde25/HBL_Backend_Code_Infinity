package com.kony.adminconsole.service.contract.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.contract.resource.api.ContractResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ContractAccountsGetOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = null;
        try {
            ContractResource contractResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ContractResource.class);
            result = contractResource.getContractAccounts(methodId, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getContractAccounts: ", e).log();
            return ErrorCodeEnum.ERR_22024.setErrorCode(new Result());
        }
        return result;
        
	}

}
