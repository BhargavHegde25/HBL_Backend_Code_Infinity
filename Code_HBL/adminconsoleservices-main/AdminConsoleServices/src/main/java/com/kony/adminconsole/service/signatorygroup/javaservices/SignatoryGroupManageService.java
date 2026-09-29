package com.kony.adminconsole.service.signatorygroup.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.signatorygroup.resource.api.SignatoryGroupResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class SignatoryGroupManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    
    private static final String CREATE_SIGNATORYGROUP_OPERATION_NAME = "createSignatoryGroup";
    private static final String UPDATE_SIGNATORY_GROUP_OPERATION_NAME = "updateSignatoryGroups";
    private static final String DELETE_SIGNATORYGROUP_OPERATION_NAME = "deleteSignatoryGroup";
    
    private static final String GET_NOGROUP_USERS_OPERATION_NAME = "getNoGroupUsers";
    private static final String GET_APPROVAL_PERMISSION_FOR_USERS_OPERATION_NAME = "getApprovalPermissionsForUser";
   
    private static final String GET_ALL_SIGNATORY_GROUP_BY_CORECUSTOMER_OPERATION_NAME = "getAllSignatoryGroupsbyCoreCustomerIds";
    private static final String GET_ALL_SIGNATORY_GROUP_OPERATION_NAME = "getAllSignatoryGroups";
    private static final String GET_SIGNATORYGROUP_DETAILS_OPERATION_NAME = "getSignatoryGroupDetails";
    private static final String FETCH_APPROVAL_MODE_OPERATION_NAME = "fetchApprovalMode";
    private static final String UPDATE_APPROVAL_MODE_OPERATION_NAME = "updateApprovalMode";
    private static final String DELETE_APPROVAL_MODE_OPERATION_NAME = "deleteApprovalMode";
    private static final String IS_SG_ELIGIBLE_FOR_DELETE_OPERATION_NAME = "isSignatoryGroupEligibleForDelete";	
    
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
    	
        try {

            if (methodID.equalsIgnoreCase(CREATE_SIGNATORYGROUP_OPERATION_NAME)) {
                return createSignatoryGroup(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_SIGNATORY_GROUP_OPERATION_NAME)) {
                return updateSignatoryGroups(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(DELETE_SIGNATORYGROUP_OPERATION_NAME)) {
                return deleteSignatoryGroup(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_NOGROUP_USERS_OPERATION_NAME)) {
                return getNoGroupUsers(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_APPROVAL_PERMISSION_FOR_USERS_OPERATION_NAME)) {
                return getApprovalPermissionsForUser(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_ALL_SIGNATORY_GROUP_BY_CORECUSTOMER_OPERATION_NAME)) {
                return getAllSignatoryGroupsbyCoreCustomerIds(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(GET_ALL_SIGNATORY_GROUP_OPERATION_NAME)) {
            	return getAllSignatoryGroups(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(GET_SIGNATORYGROUP_DETAILS_OPERATION_NAME)) {
            	return getSignatoryGroupDetails(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(FETCH_APPROVAL_MODE_OPERATION_NAME)) {
            	return fetchApprovalMode(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(UPDATE_APPROVAL_MODE_OPERATION_NAME)) {
            	return updateApprovalMode(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(DELETE_APPROVAL_MODE_OPERATION_NAME)) {
            	return deleteApprovalMode(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(IS_SG_ELIGIBLE_FOR_DELETE_OPERATION_NAME)) {
            	return isSignatoryGroupEligibleForDelete(methodID, inputArray, requestInstance, responseInstance);
            }
            
            return new Result();
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
        
    }

    private Object createSignatoryGroup(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.createSignatoryGroup(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createSignatoryGroup: ", e).log();
            return ErrorCodeEnum.ERR_22088.setErrorCode(new Result());
        }
        return result;
    }

    private Object updateSignatoryGroups(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.updateSignatoryGroups(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateSignatoryGroups: ", e).log();
            return ErrorCodeEnum.ERR_22089.setErrorCode(new Result());
        }
        return result;
    }

    private Object deleteSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.deleteSignatoryGroup(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of deleteSignatoryGroup: ", e).log();
            return ErrorCodeEnum.ERR_22090.setErrorCode(new Result());
        }
        return result;
    }

    private Object getNoGroupUsers(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.getNoGroupUsers(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getNoGroupUsers: ", e).log();
            return ErrorCodeEnum.ERR_22091.setErrorCode(new Result());
        }
        return result;
    }

    private Object getApprovalPermissionsForUser(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.getApprovalPermissionsForUser(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getApprovalPermissionsForUser: ", e).log();
            return ErrorCodeEnum.ERR_22092.setErrorCode(new Result());
        }
        return result;
    }

    private Object getAllSignatoryGroupsbyCoreCustomerIds(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.getAllSignatoryGroupsbyCoreCustomerIds(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getAllSignatoryGroupsbyCoreCustomerIds: ", e).log();
            return ErrorCodeEnum.ERR_22093.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getAllSignatoryGroups(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.getAllSignatoryGroups(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getAllSignatoryGroups: ", e).log();
            return ErrorCodeEnum.ERR_22094.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getSignatoryGroupDetails(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.getSignatoryGroupDetails(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getSignatoryGroupDetails: ", e).log();
            return ErrorCodeEnum.ERR_22095.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object fetchApprovalMode(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.fetchApprovalMode(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of fetchApprovalMode: ", e).log();
            return ErrorCodeEnum.ERR_22111.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object updateApprovalMode(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.updateApprovalMode(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateApprovalMode: ", e).log();
            return ErrorCodeEnum.ERR_22112.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object deleteApprovalMode(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.deleteApprovalMode(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of deleteApprovalMode: ", e).log();
            return ErrorCodeEnum.ERR_22113.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object isSignatoryGroupEligibleForDelete(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	SignatoryGroupResource signatorygroupResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(SignatoryGroupResource.class);
            result = signatorygroupResource.isSignatoryGroupEligibleForDelete(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of isSignatoryGroupEligibleForDelete: ", e).log();
            return ErrorCodeEnum.ERR_22117.setErrorCode(new Result());
        }
        return result;
    }

}
