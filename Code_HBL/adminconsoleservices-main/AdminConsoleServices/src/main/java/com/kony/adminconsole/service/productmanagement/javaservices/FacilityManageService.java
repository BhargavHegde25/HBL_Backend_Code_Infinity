package com.kony.adminconsole.service.productmanagement.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.productmanagement.resource.api.FacilityResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FacilityManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String CREATE_FACILITY = "createFacility";
	private static final String EDIT_FACILITY = "editFacility";
	private static final String GET_FACILITY = "getFacilities";
			
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
		
		try {
			requestInstance.addRequestParam_("legalEntityId", "");
            if (methodID.equalsIgnoreCase(CREATE_FACILITY)) {
                return createFacility(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(EDIT_FACILITY)) {
                return editFacility(methodID, inputArray, requestInstance, responseInstance);
            } else if (methodID.equalsIgnoreCase(GET_FACILITY)) {
                return getFacility(methodID, inputArray, requestInstance, responseInstance);
            }
            
            return new Result();
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
	}
	
	private Object createFacility(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	FacilityResource facilityResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(FacilityResource.class);
            result = facilityResource.createFacility(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of createFacility: ", e).log();
            return ErrorCodeEnum.ERR_22124.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object editFacility(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	FacilityResource facilityResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(FacilityResource.class);
            result = facilityResource.editFacility(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of editFacility: ", e).log();
            return ErrorCodeEnum.ERR_22125.setErrorCode(new Result());
        }
        return result;
    }
	
	private Object getFacility(String methodID, Object[] inputArray,
            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
        
        Result result = null;
        try {
        	FacilityResource facilityResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(FacilityResource.class);
            result = facilityResource.getFacility(methodID, inputArray, requestInstance,
                    responseInstance);
        } catch (Exception e) {
            alert.prepareError("Caught exception at invoke of getFacility: ", e).log();
            return ErrorCodeEnum.ERR_22126.setErrorCode(new Result());
        }
        return result;
    }

}
