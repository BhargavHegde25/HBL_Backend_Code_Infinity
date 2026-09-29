package com.kony.adminconsole.sca;

import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;

public class SCAServices {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");


	private static final String SERVICE_ID = "SCAServices";
	private static final String OPERATION_UPDATE_USER_STATUS = "updateUserStatus";
	
	public static JSONObject updateUserStatus(Map payload) {
		DBPServiceExecutor serviceExecutor;
		JSONObject responseInJsonObject = new JSONObject();
		String status = (String) payload.get("status");
		if(status.equalsIgnoreCase("ACTIVE")) {
			payload.put("status", "UNSUSPEND");
		} else if(status.equalsIgnoreCase("SUSPENDED")) {
			payload.put("status", "SUSPEND");
		} else if(status.equalsIgnoreCase("INACTIVE")) {
			payload.put("status", "REVOKED");
		} else {
			return responseInJsonObject;
		}
		try {
			serviceExecutor = DBPServiceExecutorBuilder.builder()
														.withServiceId(SERVICE_ID)
														.withOperationId(OPERATION_UPDATE_USER_STATUS)
														.withRequestParameters(payload)
														.build();
			String response = serviceExecutor.getResponse();
			responseInJsonObject = CommonUtilities.getStringAsJSONObject(response);
		} catch (DBPApplicationException e) {
			alert.prepareError("Error occurred: ", e).log();
		}
		return responseInJsonObject;
	}
}
