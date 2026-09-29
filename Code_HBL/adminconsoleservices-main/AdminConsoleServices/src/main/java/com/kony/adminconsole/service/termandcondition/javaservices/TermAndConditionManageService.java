package com.kony.adminconsole.service.termandcondition.javaservices;

/**
 * Service to Manage Requests related to TermAndCondition
 * 
 * @author Chandan Gupta - KH2516
 *
 */
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;

import com.kony.adminconsole.service.termandcondition.resource.api.TnCResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

import com.konylabs.middleware.dataobject.Result;
public class TermAndConditionManageService implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String EDIT_TERMS_AND_CONDITIONS_METHOD_NAME = "editTermsAndConditions";
    private static final String GET_TERMS_AND_CONDITIONS_METHOD_NAME = "getTermsAndConditions";
    private static final String GET_ALL_TERMS_AND_CONDITIONS_METHOD_NAME = "getAllTermsAndConditions";
    private static final String CREATE_TERMS_AND_CONDITIONS_VERSION_METHOD_NAME = "createTermsAndConditionsVersion";
    private static final String DELETE_TERMS_AND_CONDITIONS_VERSION_METHOD_NAME = "deleteTermsAndConditionsVersion";
    private static final String GET_REQUIRED_TERMS_AND_CONDITIONS_VERSION_METHOD_NAME = "getRequiredTermsAndConditions";

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
    	
        try {

            if (methodID.equalsIgnoreCase(CREATE_TERMS_AND_CONDITIONS_VERSION_METHOD_NAME)) {
                return createTermsAndConditionsVersion(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(EDIT_TERMS_AND_CONDITIONS_METHOD_NAME)) {
                return editTermsAndConditions(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(DELETE_TERMS_AND_CONDITIONS_VERSION_METHOD_NAME)) {
                return deleteTermsAndConditionsVersion(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(GET_ALL_TERMS_AND_CONDITIONS_METHOD_NAME)) {
            	return getAllTermsAndConditions(methodID, inputArray, requestInstance, responseInstance);
            } else if(methodID.equalsIgnoreCase(GET_TERMS_AND_CONDITIONS_METHOD_NAME)) {
            	return getTermsAndConditions(methodID, inputArray, requestInstance, responseInstance);
			} else if (methodID.equalsIgnoreCase(GET_REQUIRED_TERMS_AND_CONDITIONS_VERSION_METHOD_NAME)) {
				return getRequiredTermsAndConditions(methodID, inputArray, requestInstance, responseInstance);
			}      
            return new Result();
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
        
    }

    private Object createTermsAndConditionsVersion(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	TnCResource tncResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(TnCResource.class);
            result = tncResource.createTermsAndConditionsVersion(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createTermsAndConditionsVersion: ", e).log();
            return ErrorCodeEnum.ERR_20277.setErrorCode(new Result());
        }
        return result;
    }

    private Object editTermsAndConditions(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	TnCResource tncResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(TnCResource.class);
            result = tncResource.editTermsAndConditions(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of editTermsAndConditions: ", e).log();
            return ErrorCodeEnum.ERR_22089.setErrorCode(new Result());
        }
        return result;
    }

    private Object deleteTermsAndConditionsVersion(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	TnCResource tncResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(TnCResource.class);
            result = tncResource.deleteTermsAndConditionsVersion(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of deleteTermsAndConditionsVersion: ", e).log();
            return ErrorCodeEnum.ERR_22090.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getAllTermsAndConditions(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	TnCResource tncResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(TnCResource.class);
            result = tncResource.getAllTermsAndConditions(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getAllTermsAndConditions: ", e).log();
            return ErrorCodeEnum.ERR_22094.setErrorCode(new Result());
        }
        return result;
    }
    
    private Object getTermsAndConditions(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	TnCResource tncResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(TnCResource.class);
            result = tncResource.getTermsAndConditions(methodID, inputArray, requestInstance, responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getTermsAndConditions: ", e).log();
            return ErrorCodeEnum.ERR_22095.setErrorCode(new Result());
        }
        return result;
    }
    
	private Object getRequiredTermsAndConditions(String methodID, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse responseInstance) {

		Result result = null;
		try {
			TnCResource tncResource = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(ResourceFactory.class)
					.getResource(TnCResource.class);
			result = tncResource.getRequiredTermsAndConditions(methodID, inputArray, requestInstance, responseInstance);
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke of getRequiredTermsAndConditions: ", e).log();
			return ErrorCodeEnum.ERR_20264.setErrorCode(new Result());
		}
		return result;
	}
}