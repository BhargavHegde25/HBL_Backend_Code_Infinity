package com.kony.adminconsole.service.usermanagement.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.usermanagement.resource.api.EmployeePermissionResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class EmployeePermissionManageSevice implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String GET_EMPLOYEE_PERMISSIONS = "getEmployeePermissions";
	private static final String GET_EMPLOYEE_PERMISSION_DETAILS = "getEmployeePermissionDetails";
	private static final String UPDATE_EMPLOYEE_PERMISSION_DETAILS = "updateEmployeePermissionDetails";
	private static final String UPDATE_EMPLOYEE_PERMISSION_STATUS = "updateEmployeePermissionStatus";
	private static final String CREATE_EMPLOYEE_PERMISSIONS = "createEmployeePermission";
	private static final String FETCH_LEGAL_ENTITY_LIST = "fetchLegalEntityList";
	private static final String GET_PERMISSION_BY_LEGAL_ENTITIES = "getPermissionsByLegalEntities";
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
		
		try {

            if (methodID.equalsIgnoreCase(GET_EMPLOYEE_PERMISSIONS)) {
                return getEmployeePermissions(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_EMPLOYEE_PERMISSION_DETAILS)) {
                return getEmployeePermissionDetails(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(CREATE_EMPLOYEE_PERMISSIONS)) {
                return createEmployeePermission(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_EMPLOYEE_PERMISSION_DETAILS)) {
                return updateEmployeePermissionDetails(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_EMPLOYEE_PERMISSION_STATUS)) {
                return updateEmployeePermissionStatus(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(FETCH_LEGAL_ENTITY_LIST)) {
                return fetchLegalEntityList(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_PERMISSION_BY_LEGAL_ENTITIES)) {
                return getPermissionsByLegalEntities(methodID, inputArray, requestInstance, responseInstance);
            }
            
            return new Result();
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
		
	}
	
	private Object getPermissionsByLegalEntities(String methodID, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
		 Result result = null;
	        try {
	        	EmployeePermissionResource permissionResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeePermissionResource.class);
	            result = permissionResource.getPermissionsByLegalEntities(methodID, inputArray, requestInstance,
	                    responseInstance);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception at invoke of getEmployeePermissionsByLegalEntities: ", e).log();
	            return ErrorCodeEnum.ERR_22159.setErrorCode(new Result());
	        }
	        return result;
	}

	private Object getEmployeePermissions(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeePermissionResource permissionResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeePermissionResource.class);
            result = permissionResource.getEmployeePermissions(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getEmployeePermissions: ", e).log();
            return ErrorCodeEnum.ERR_22159.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object getEmployeePermissionDetails(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeePermissionResource permissionResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeePermissionResource.class);
            result = permissionResource.getEmployeePermissionDetails(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getEmployeePermissionDetails: ", e).log();
            return ErrorCodeEnum.ERR_22160.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object createEmployeePermission(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeePermissionResource permissionResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeePermissionResource.class);
            result = permissionResource.createEmployeePermission(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createEmployeePermission: ", e).log();
            return ErrorCodeEnum.ERR_22163.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object updateEmployeePermissionDetails(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeePermissionResource permissionResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeePermissionResource.class);
            result = permissionResource.updateEmployeePermissionDetails(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateEmployeePermissionDetails: ", e).log();
            return ErrorCodeEnum.ERR_22161.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object updateEmployeePermissionStatus(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeePermissionResource permissionResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeePermissionResource.class);
            result = permissionResource.updateEmployeePermissionStatus(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateEmployeePermissionStatus: ", e).log();
            return ErrorCodeEnum.ERR_22162.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object fetchLegalEntityList(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	EmployeePermissionResource permissionResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(EmployeePermissionResource.class);
            result = permissionResource.fetchLegalEntityList(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of fetchLegalEntityList: ", e).log();
            return ErrorCodeEnum.ERR_22171.setErrorCode(new Result());
        }
        return result;
    }
	
}