package com.temenos.infinity.api.arrangements.javaservice;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPErrorCodeSetter;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.resource.api.MortgageFacilityDetailsResource;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MortgageFacilityDetailsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        @SuppressWarnings("unchecked")
        Map<String, String> params = HelperMethods.getInputParamMap(inputArray);
        alert.prepareError("params >> "+params);
        logDCRequestParams(request, alert);
        inputArray[1] = params;
        Map<String, String> inputParams = (Map<String, String>) inputArray[1];
        try {
        	String methodId="";
        	MortgageFacilityDetailsResource accountsResource =
                    DBPAPIAbstractFactoryImpl.getResource(MortgageFacilityDetailsResource.class);
            result = accountsResource.getMortageFacilityDetails(methodId, inputArray, request, response);
        } catch (ApplicationException e) {
        	alert.prepareError("Unable to fetch records from Mortage Facility" + e).log();
            DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), result);
        }
        return result;
    }
    
    public static void logDCRequestParams(DataControllerRequest dcRequest, Alert classLogger) {
		classLogger.prepareError("Priting DC Request PARAMS ===>").log();
		StringBuilder sb = new StringBuilder();
		dcRequest.getParameterNames().forEachRemaining((param)->{
			sb.append(param).append(" = ").append(dcRequest.getParameter(param)).append(" | ");
		});
		classLogger.prepareError(sb.toString()).log();
		sb.setLength(0);
		classLogger.prepareError("Priting DC Request ATTRS ===>").log();
		dcRequest.getAttributeNames().forEachRemaining((attr)->{
			sb.append(attr).append(" = ").append(dcRequest.getParameter(attr)).append(" | ");
		});
		classLogger.prepareError(sb.toString()).log();
	}

}
