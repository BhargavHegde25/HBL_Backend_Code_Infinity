package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;

public class GetBlockedFundsJavaService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
    	Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String transactionResponse = null;
        String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
		if (ARRANGEMENTS_BACKEND!= null) {
        if (ARRANGEMENTS_BACKEND.equals("MOCK")) {
        	Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
			 
            transactionResponse = Executor.invokePassThroughServiceAndGetString((InfinityServices)
            		ArrangementsAPIServices.GET_BLOCKED_FUNDS_MOCK, inputMap, headerMap);
			return JSONToResult.convert(transactionResponse);
    	}
    
		}
		try {
			
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
    		//HashMap<String, Object> inputParams = new HashMap<String, Object>();
    		HashMap<String, Object> inputParams1 = new HashMap<String, Object>();
			for ( Map.Entry<String, String> entry : inputParams.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    inputParams1.put(key, value);    
			}
    		//inputParams.put("accountID", request.getParameter("accountID"));
			 transactionResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId("ArrangementsT24Services")
                    .withOperationId("getBlockedFunds")
                    .withRequestParameters(inputParams1).withRequestHeaders(headerParams)
                    .withDataControllerRequest(request).build().getResponse();
			return JSONToResult.convert(transactionResponse);
            
        } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20049);
        }
        
    }
    
}
