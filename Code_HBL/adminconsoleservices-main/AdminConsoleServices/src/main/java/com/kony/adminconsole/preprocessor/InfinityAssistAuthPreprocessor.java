package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class InfinityAssistAuthPreprocessor implements DataPreProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		String identityResponse = null;
		String claimToken = null;
		try {	
			identityResponse = DBPServiceExecutorBuilder.builder().withServiceId("InfinityAssistIdentityService")				    
					.withOperationId("getClaimToken")						
					.build().getResponse();		
			JSONObject identityObject = new JSONObject(identityResponse);
			claimToken = (String) identityObject.get("Claims_Token");
			System.out.println("AuthToken CorporateLOS "+ claimToken);
			request.addRequestParam_("X-Kony-Authorization", claimToken);			
			return true;
		}
		catch(Exception e) {
			alert.prepareError("[InfinityAssistAuthPreprocessor] Error occured in PreProcessor", e).log();
			return false;
		}		
	}
}
