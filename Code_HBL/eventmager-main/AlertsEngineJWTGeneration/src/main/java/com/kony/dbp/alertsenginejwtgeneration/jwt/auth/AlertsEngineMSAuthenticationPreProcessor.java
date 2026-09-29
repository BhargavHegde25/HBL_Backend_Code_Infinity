package com.kony.dbp.alertsenginejwtgeneration.jwt.auth;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AlertsEngineMSAuthenticationPreProcessor implements DataPreProcessor2 {	
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {		
		try {
			Authentication authentication = Authentication.getInstance();
			request.addRequestParam_("Authorization", authentication.getAuthToken(request));
			} catch (Exception e) {
			alert.prepareError("Error while getting authtoken"+ e).log();
			result.addErrMsgParam("Error while fetching auth token "+ e.getMessage());
			return false;
		}
		return true;
	}

}
