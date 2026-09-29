package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
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


public class GetUserScheduledTransactions implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        String transactionResponse = null;
        String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists();
		if (ARRANGEMENTS_BACKEND!= null) {
        if (ARRANGEMENTS_BACKEND.equals("MOCK")) {
        	Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
			 
            transactionResponse = Executor.invokePassThroughServiceAndGetString((InfinityServices)
            		ArrangementsAPIServices.GET_USER_SCHEDULED_TRANSACTIONS_MOCK, inputMap, headerMap);
			return JSONToResult.convert(transactionResponse);
    	}
    
		}
		try {
			
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
    		HashMap<String, Object> inputParams = new HashMap<String, Object>();
    		
			 transactionResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId("ArrangementsOrchServices")
                    .withOperationId("upcomingTransactions")
                    .withRequestParameters(inputParams).withRequestHeaders(headerParams)
                    .withDataControllerRequest(request).build().getResponse();
			return JSONToResult.convert(transactionResponse);
            
        } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20049);
        }
        
    }

}
