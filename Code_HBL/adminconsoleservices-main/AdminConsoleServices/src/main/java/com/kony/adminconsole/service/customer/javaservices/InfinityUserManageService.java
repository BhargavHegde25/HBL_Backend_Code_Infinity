package com.kony.adminconsole.service.customer.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.customer.resource.api.InfinityUserManagementResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class InfinityUserManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String CREATE_INFINITY_USER_OPERATION_NAME = "createInfinityUser";
    private static final String EDIT_INFINITY_USER_OPERATION_NAME = "editInfinityUser";
    private static final String GET_INFINITY_USER_OPERATION_NAME = "getInfinityUser";
    private static final String GET_INFINITY_USER_CONTRACT_DETAILS = "getInfinityUserContractDetails";
    private static final String GET_INFINITY_USER_ACCOUNTS = "getInfinityUserAccounts";
    private static final String GET_INFINITY_USER_FEATUREACTIONS = "getInfinityUserFeatureActions";
    private static final String GET_INFINITY_USER_LIMITS = "getInfinityUserLimits";
    private static final String GET_INFINITY_USER_ACCOUNTS_FOR_CORECUSTOMER = "getInfinityUserAccountsForCorecustomer";
    private static final String GET_INFINITY_USER_SERVICEDEFS_ROLES = "getInfinityUserServiceDefsRoles";
    
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
        try {

            if (methodID.equalsIgnoreCase(CREATE_INFINITY_USER_OPERATION_NAME)) {
                return createInfinityUser(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(EDIT_INFINITY_USER_OPERATION_NAME)) {
                return editInfinityUser(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_INFINITY_USER_OPERATION_NAME)) {
                return getInfinityUser(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(GET_INFINITY_USER_CONTRACT_DETAILS)) {
                return getInfinityUserContractDetails(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(GET_INFINITY_USER_ACCOUNTS)) {
                return getInfinityUserAccounts(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(GET_INFINITY_USER_FEATUREACTIONS)) {
                return getInfinityUserFeatureActions(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(GET_INFINITY_USER_LIMITS)) {
                return getInfinityUserLimits(methodID, inputArray, requestInstance, responseInstance);
        	}else if (methodID.equalsIgnoreCase(GET_INFINITY_USER_ACCOUNTS_FOR_CORECUSTOMER)) {
        		return getInfinityUserAccountsForCorecustomer(methodID, inputArray, requestInstance, responseInstance);
        	}else if (methodID.equalsIgnoreCase(GET_INFINITY_USER_SERVICEDEFS_ROLES)) {
        		return getInfinityUserServicesdefsRoles(methodID, inputArray, requestInstance, responseInstance);
        	}
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
        return new Result();
    }

    private Object getInfinityUser(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            InfinityUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityUserManagementResource.class);
            result = customerResource.getInfinityUser(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getInfinityUserDetails: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }

    private Object editInfinityUser(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            InfinityUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityUserManagementResource.class);
            result = customerResource.editInfinityUser(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of editInfinityUser: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }

    private Object createInfinityUser(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            InfinityUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityUserManagementResource.class);
            result = customerResource.createInfinityUser(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createInfinityUser: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getInfinityUserContractDetails(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            InfinityUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityUserManagementResource.class);
            result = customerResource.getInfinityUserContractDetails(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getInfinityUserContractDetails: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getInfinityUserAccounts(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            InfinityUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityUserManagementResource.class);
            result = customerResource.getInfinityUserAccounts(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createInfinityUser: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getInfinityUserFeatureActions(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            InfinityUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityUserManagementResource.class);
            result = customerResource.getInfinityUserFeatureActions(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createInfinityUser: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getInfinityUserLimits(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = new Result();
        try {
            InfinityUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityUserManagementResource.class);
            result = customerResource.getInfinityUserLimits(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createInfinityUser: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getInfinityUserAccountsForCorecustomer(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {       

        Result result = new Result();
        try {
            InfinityUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityUserManagementResource.class);
            result = customerResource.getInfinityUserAccountsForCorecustomer(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getInfinityUserAccountsForCorecustomer: ", e).log();
            return ErrorCodeEnum.ERR_22078.setErrorCode(new Result());
        }
        return result;
    }        

    private Object getInfinityUserServicesdefsRoles(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {

        Result result = new Result();
        try {
            InfinityUserManagementResource customerResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(InfinityUserManagementResource.class);
            result = customerResource.getInfinityUserServicedefsRoles(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getInfinityUserAccountsForCorecustomer: ", e).log();
            return ErrorCodeEnum.ERR_22078.setErrorCode(new Result());
        }
        return result;
    }    
    

}
