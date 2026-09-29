package com.kony.adminconsole.service.customer.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.customer.resource.api.PartyUserManagementResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class PartyUserManageService implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String CREATE_PARTY_USER_OPERATION_NAME = "createPartyUser";
    private static final String UPDATE_PARTY_USER_OPERATION_NAME = "updatePartyUser";
    private static final String SEARCH_PARTY_USER_OPERATION_NAME = "searchPartyUser";

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		
		Result result = null;
		try {

            if (methodId.equalsIgnoreCase(CREATE_PARTY_USER_OPERATION_NAME)) {
                return createPartyUser(methodId, inputArray, request, response);
            } else if (methodId.equalsIgnoreCase(UPDATE_PARTY_USER_OPERATION_NAME)) {
                return updatePartyUser(methodId, inputArray, request, response);
            } else if (methodId.equalsIgnoreCase(SEARCH_PARTY_USER_OPERATION_NAME)) {
                return searchPartyUser(methodId, inputArray, request, response);
            }

        } catch (Exception exp) {
            result = new Result();
            diagnostic.prepareDebug("Runtime Exception in PartyUserManageService.Exception Trace:", exp).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
        }
		
		return result;
	}
	
	private Object createPartyUser(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            PartyUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(PartyUserManagementResource.class);
            result = customerResource.createPartyUser(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createPartyUser: ", e).log();
            return ErrorCodeEnum.ERR_20555.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object updatePartyUser(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            PartyUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(PartyUserManagementResource.class);
            result = customerResource.updatePartyUser(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updatePartyUser: ", e).log();
            return ErrorCodeEnum.ERR_20556.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object searchPartyUser(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            PartyUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(PartyUserManagementResource.class);
            result = customerResource.searchPartyUser(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of searchPartyUser: ", e).log();
            return ErrorCodeEnum.ERR_20557.setErrorCode(new Result());
        }
        return result;
    }
	
	
}
