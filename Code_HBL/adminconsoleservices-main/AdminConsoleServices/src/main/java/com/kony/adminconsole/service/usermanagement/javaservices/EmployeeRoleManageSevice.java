package com.kony.adminconsole.service.usermanagement.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.usermanagement.resource.api.EmployeeRoleResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class EmployeeRoleManageSevice implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String GET_EMPLOYEE_ROLES = "getEmployeeRoles";
	private static final String GET_EMPLOYEE_ROLE_DETAILS = "getEmployeeRoleDetails";
	private static final String UPDATE_EMPLOYEE_ROLE_DETAILS = "updateEmployeeRoleDetails";
	private static final String UPDATE_EMPLOYEE_ROLE_STATUS = "updateEmployeeRoleStatus";
	private static final String CREATE_EMPLOYEE_ROLES = "createEmployeeRole";
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
		
		try {

            if (methodID.equalsIgnoreCase(GET_EMPLOYEE_ROLES)) {
                return getEmployeeRoles(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_EMPLOYEE_ROLE_DETAILS)) {
                return getEmployeeRoleDetails(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(CREATE_EMPLOYEE_ROLES)) {
                return createEmployeeRole(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_EMPLOYEE_ROLE_DETAILS)) {
                return updateEmployeeRoleDetails(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_EMPLOYEE_ROLE_STATUS)) {
                return updateEmployeeRoleStatus(methodID, inputArray, requestInstance, responseInstance);
            }
            
            return new Result();
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
		
	}
	
	private Object getEmployeeRoles(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeRoleResource roleResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeRoleResource.class);
            result = roleResource.getEmployeeRoles(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getEmployeeRoles: ", e).log();
            return ErrorCodeEnum.ERR_22191.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object getEmployeeRoleDetails(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeRoleResource roleResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeRoleResource.class);
            result = roleResource.getEmployeeRoleDetails(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getEmployeeRoleDetails: ", e).log();
            return ErrorCodeEnum.ERR_22160.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object createEmployeeRole(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeRoleResource roleResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeRoleResource.class);
            result = roleResource.createEmployeeRole(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createEmployeeRole: ", e).log();
            return ErrorCodeEnum.ERR_22184.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object updateEmployeeRoleDetails(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeRoleResource roleResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeRoleResource.class);
            result = roleResource.updateEmployeeRoleDetails(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateEmployeeRoleDetails: ", e).log();
            return ErrorCodeEnum.ERR_22189.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object updateEmployeeRoleStatus(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeeRoleResource roleResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeeRoleResource.class);
            result = roleResource.updateEmployeeRoleStatus(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateEmployeeRoleStatus: ", e).log();
            return ErrorCodeEnum.ERR_22190.setErrorCode(new Result());
        }
        return result;
    }
	
}
