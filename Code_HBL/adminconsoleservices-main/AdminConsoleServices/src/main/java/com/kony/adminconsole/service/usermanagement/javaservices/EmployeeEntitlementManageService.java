package com.kony.adminconsole.service.usermanagement.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.usermanagement.resource.api.EmployeeEntitlementResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class EmployeeEntitlementManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String CREATE_ENTITLEMENT_BY_USERID = "createEntitlementByUserId";
	private static final String GET_ENTITLEMENT_BY_USERID = "getEntitlementByUserId";
	private static final String UPDATE_ENTITLEMENT_BY_USERID = "updateEntitlementByUserId";
	private static final String GET_ENTITLEMENT = "getEntitlement";
	private static final String UPDATE_ENTITLEMENT = "updateEntitlement";
	private static final String DELETE_ENTITLEMENT = "deleteEntitlement";
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
		
		try {

            if (methodID.equalsIgnoreCase(CREATE_ENTITLEMENT_BY_USERID)) {
                return createEntitlementByUserId(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_ENTITLEMENT_BY_USERID)) {
                return getEntitlementByUserId(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(UPDATE_ENTITLEMENT_BY_USERID)) {
                return updateEntitlementByUserId(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_ENTITLEMENT)) {
                return getEntitlement(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_ENTITLEMENT)) {
                return updateEntitlement(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(DELETE_ENTITLEMENT)) {
                return deleteEntitlement(methodID, inputArray, requestInstance, responseInstance);
            }
            
            return new Result();
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
		
	}
	
	private Object createEntitlementByUserId(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeEntitlementResource employeeEntitlementResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeEntitlementResource.class);
            result = employeeEntitlementResource.createEntitlementByUserId(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createEntitlementByUserId: ", e).log();
            return ErrorCodeEnum.ERR_22180.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object getEntitlementByUserId(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeEntitlementResource employeeEntitlementResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeEntitlementResource.class);
            result = employeeEntitlementResource.getEntitlementByUserId(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getEntitlementByUserId: ", e).log();
            return ErrorCodeEnum.ERR_22179.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object updateEntitlementByUserId(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeEntitlementResource employeeEntitlementResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeEntitlementResource.class);
            result = employeeEntitlementResource.updateEntitlementByUserId(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateEntitlementByUserId: ", e).log();
            return ErrorCodeEnum.ERR_22178.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object getEntitlement(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeEntitlementResource employeeEntitlementResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeEntitlementResource.class);
            result = employeeEntitlementResource.getEntitlement(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getEntitlement: ", e).log();
            return ErrorCodeEnum.ERR_22177.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object updateEntitlement(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeEntitlementResource employeeEntitlementResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeEntitlementResource.class);
            result = employeeEntitlementResource.updateEntitlement(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateEntitlement: ", e).log();
            return ErrorCodeEnum.ERR_22176.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object deleteEntitlement(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeEntitlementResource employeeEntitlementResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeEntitlementResource.class);
            result = employeeEntitlementResource.deleteEntitlement(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of deleteEntitlement: ", e).log();
            return ErrorCodeEnum.ERR_22175.setErrorCode(new Result());
        }
        return result;
    }
	
}