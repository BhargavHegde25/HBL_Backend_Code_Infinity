package com.kony.adminconsole.service.customer.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.customer.resource.api.InfinityCustomerResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class InfinityCustomerServiceOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String GET_INFINITY_ACCOUNTS_OPERATION_NAME = "getInfinityAccounts";
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			
			if (methodId.equalsIgnoreCase(GET_INFINITY_ACCOUNTS_OPERATION_NAME)) {
                return getInfinityAccounts(methodId, inputArray, request, response);
            }
			
		} catch (Exception exp) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Exception in InfinityCustomerServiceOperation.Exception Trace:", exp).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
		 return new Result();
	}
	
	
	private Object getInfinityAccounts(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        
        Result result = new Result();
        try {
        	InfinityCustomerResource infinityCustomerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityCustomerResource.class);
            result = infinityCustomerResource.getInfinityAccounts(methodID, inputArray, request, response);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getInfinityAccounts: ", e).log();
            return ErrorCodeEnum.ERR_22130.setErrorCode(new Result());
        }
        return result;
    }

}
