package com.kony.adminconsole.service.contract.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.contract.resource.api.ContractResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

public class ContractManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    
    private static final String CREATE_CONTRACT_OPERATION_NAME = "createContract";
    private static final String EDIT_CONTRACT_OPERATION_NAME = "editContract";
    private static final String GET_CONTRACT_DETAILS_OPERATION_NAME = "getContractDetails";
    private static final String SEARCH_CONTRACT_OPERATION_NAME = "searchContract";
    private static final String GET_CONTRACT_FEATURE_ACTION_LIMITS_OPERATION_NAME = "getContractFeatureActionLimits";
    private static final String GET_CONTRACT_INFINITY_USERS_OPERATION_NAME = "getContractInfinityUsers";
    private static final String UPDATE_CONTRACT_STATUS_OPERATION_NAME = "updateContractStatus";
    private static final String GET_CONTRACT_LIST_BY_STATUS_OPERATION_NAME = "getListOfContractsByStatus";
    
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
    	
        try {

            if (methodID.equalsIgnoreCase(CREATE_CONTRACT_OPERATION_NAME)) {
                return createContract(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(EDIT_CONTRACT_OPERATION_NAME)) {
                return editContract(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_CONTRACT_DETAILS_OPERATION_NAME)) {
                return getContractDetails(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(SEARCH_CONTRACT_OPERATION_NAME)) {
                return searchContract(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_CONTRACT_FEATURE_ACTION_LIMITS_OPERATION_NAME)) {
                return getContractFeatureActionLimits(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_CONTRACT_INFINITY_USERS_OPERATION_NAME)) {
                return getContractInfinityUsers(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(UPDATE_CONTRACT_STATUS_OPERATION_NAME)) {
            	return updateContractStatus(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(GET_CONTRACT_LIST_BY_STATUS_OPERATION_NAME)) {
            	return getListOfContractsByStatus(methodID, inputArray, requestInstance, responseInstance);
            }
            
            return new Result();
            
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
        
    }

    private Object getContractFeatureActionLimits(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
            ContractResource contractResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ContractResource.class);
            result = contractResource.getContractFeatureActionLimits(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of get contract features and actions: ", e).log();
            return ErrorCodeEnum.ERR_21974.setErrorCode(new Result());
        }
        return result;
    }

    private Object getContractInfinityUsers(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
            ContractResource contractResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ContractResource.class);
            result = contractResource.getContractInfinityUsers(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of get contract features and actions: ", e).log();
            return ErrorCodeEnum.ERR_21974.setErrorCode(new Result());
        }
        return result;
    }

    private Object searchContract(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
            ContractResource contractResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ContractResource.class);
            result = contractResource.searchContract(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of search contract: ", e).log();
            return ErrorCodeEnum.ERR_21973.setErrorCode(new Result());
        }
        return result;
    }

    private Object getContractDetails(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
            ContractResource contractResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ContractResource.class);
            result = contractResource.getContractDetails(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of CreateGroupOperation: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }

    private Object editContract(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
            ContractResource contractResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ContractResource.class);
            result = contractResource.editContract(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of CreateGroupOperation: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }

    private Object createContract(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
            ContractResource contractResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ContractResource.class);
            result = contractResource.createContract(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of CreateGroupOperation: ", e).log();
            return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object updateContractStatus(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
            ContractResource contractResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ContractResource.class);
            result = contractResource.updateContractStatus(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateContractStatus: ", e).log();
            return ErrorCodeEnum.ERR_20511.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getListOfContractsByStatus(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
            ContractResource contractResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(ContractResource.class);
            result = contractResource.getListOfContractsByStatus(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getListOfContractsByStatus: ", e).log();
            return ErrorCodeEnum.ERR_20512.setErrorCode(new Result());
        }
        return result;
    }

}
