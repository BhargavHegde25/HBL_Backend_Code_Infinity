package com.kony.adminconsole.service.usermanagement.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.usermanagement.resource.api.UserFeatureResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ManageUserFeaturesService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String GET_INTERNALUSER_FEATURES = "getInternalUserFeatures";
	private static final String GET_INTERNALUSER_FEATURE_ACTIONS = "getInternalUserFeatureActions";
	private static final String UPDATE_INTERNALUSER_FEATURE_ACTIONS = "updateInternalUserFeatureActions";
	private static final String UPDATE_INTERNALUSER_ACTION_STATUS = "updateInternalUserActionStatus";
	private static final String GET_ALL_INTERNAL_FEATURE_ACTION = "getAllInternalFeatureActions";
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
		
		try {

            if (methodID.equalsIgnoreCase(GET_INTERNALUSER_FEATURES)) {
                return getInternalUserFeatures(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_INTERNALUSER_FEATURE_ACTIONS)) {
                return getInternalUserFeatureActions(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_INTERNALUSER_FEATURE_ACTIONS)) {
                return updateInternalUserFeatureActions(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(UPDATE_INTERNALUSER_ACTION_STATUS)) {
                return updateInternalUserActionStatus(methodID, inputArray, requestInstance, responseInstance);
            }else if (methodID.equalsIgnoreCase(GET_ALL_INTERNAL_FEATURE_ACTION)) {
                return getAllInternalFeatureActions(methodID, inputArray, requestInstance, responseInstance);
            }
            
            return new Result();
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
		
	}
	
	private Object getInternalUserFeatures(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	UserFeatureResource userResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(UserFeatureResource.class);
            result = userResource.getInternalUserFeatures(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getInternalUserFeatures: ", e).log();
            return ErrorCodeEnum.ERR_22152.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object getInternalUserFeatureActions(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	UserFeatureResource userResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(UserFeatureResource.class);
            result = userResource.getInternalUserFeatureActions(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getInternalUserFeatureActions: ", e).log();
            return ErrorCodeEnum.ERR_22153.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object getAllInternalFeatureActions(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	UserFeatureResource userResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(UserFeatureResource.class);
            result = userResource.getAllInternalFeatureActions(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getAllInternalUserFeatureActions: ", e).log();
            return ErrorCodeEnum.ERR_22174.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object updateInternalUserFeatureActions(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	UserFeatureResource userResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(UserFeatureResource.class);
            result = userResource.updateInternalUserFeatureActions(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateInternalUserFeatureActions: ", e).log();
            return ErrorCodeEnum.ERR_22156.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object updateInternalUserActionStatus(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	UserFeatureResource userResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(UserFeatureResource.class);
            result = userResource.updateInternalUserActionStatus(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of updateInternalUserActionStatus: ", e).log();
            return ErrorCodeEnum.ERR_22158.setErrorCode(new Result());
        }
        return result;
    }
	

}
