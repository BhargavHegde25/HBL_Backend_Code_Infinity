package com.kony.dbpalerts.alertsprocess;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.util.JSONUtils;
import com.kony.dbpalerts.alertsutils.AlertConstants;
import com.kony.dbpalerts.alertsutils.AlertsUtils;
import com.kony.dbpalerts.alertsutils.RecipientTypeDTO;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetRecipientsService implements JavaService2 {	
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public Object invoke(String paramString, Object[] paramArrayOfObject,
			DataControllerRequest paramDataControllerRequest, DataControllerResponse paramDataControllerResponse)
			throws Exception {
		Result result = new Result();
		try {
			Map<String, String> inputParams = (HashMap<String, String>) paramArrayOfObject[1];
			String inputStr = inputParams.get(AlertConstants.INPUT_RECIPIENTINFO);
			RecipientTypeDTO recipientDTO = null;
			
			if (inputStr != null) {
				 recipientDTO = JSONUtils.parse(inputStr, RecipientTypeDTO.class);
			}

			if (recipientDTO == null || recipientDTO.getServiceName() == null 
					|| recipientDTO.getOperationName() == null) {
				diagnostic.prepareDebug("No input is passed").log();
				result.addParam(AlertConstants.ERRMSG, AlertConstants.GETRECIPIENTSERVICE_INPUT_EMPTY);
				return result;
			}		
			
			
			try {
				result = AlertsUtils.callInternalService(recipientDTO.getInputMap(), recipientDTO.getServiceName(),
						recipientDTO.getOperationName(),null);

			} catch (Exception e) {
				diagnostic.prepareDebug("Error occured while invoking service", e).log();
				result.addParam(new Param(AlertConstants.ERRMSG, 
						"Error occured while invoking service "+ recipientDTO.getServiceName()));
			}
			result.addParam(new Param(AlertConstants.EVENTID, recipientDTO.getEventID()));
			
			
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured while processing Java service", e).log();
			result.addParam(new Param(AlertConstants.ERRMSG, "Error occured while processing GetRecipientsService"));
		}
		
		return result;
		
	}
	
}
