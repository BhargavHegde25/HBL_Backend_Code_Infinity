package com.kony.eventdispatcher.service;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.util.JSONUtils;
import com.kony.eventdispatcher.dto.EventTriggerConfig;
import com.kony.eventdispatcher.operations.ProcessDispatchEvent;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ProcessEvent implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		String input = request.getParameter(URLConstants.INPUTEVENTS);
		diagnostic.prepareDebug("input "+input).log();
		EventTriggerConfig event = null;
		try {
			if (input != null) {
				event = JSONUtils.parse(input, EventTriggerConfig.class);
			}
		} catch (Exception e) {
			alert.prepareError("Exception ",e).log();
		}

		if (event == null) {
			alert.prepareError("No input is passed").log();
			return result;
		}
		ProcessDispatchEvent.checkConfigurationsAndDispatchEvent(event);
		return result;
	}

}
