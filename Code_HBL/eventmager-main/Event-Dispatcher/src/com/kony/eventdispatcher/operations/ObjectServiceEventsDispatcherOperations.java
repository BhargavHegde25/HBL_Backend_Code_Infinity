package com.kony.eventdispatcher.operations;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.kony.utils.HelperMethods;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class ObjectServiceEventsDispatcherOperations {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	
	public static void setSessionId(FabricRequestManager fabricRequestManager, JsonObject otherData) {
		String sessionId = null;
		try {
			sessionId = fabricRequestManager.getServicesManager().getIdentityHandler().getSecurityAttributes()
					.get("session_token").toString();
		} catch (Exception e) {
			alert.prepareError("Exception occurred while fetching session ", e).log();
		}
		if (StringUtils.isNotBlank(sessionId))
			otherData.addProperty(URLConstants.SESSIONIDOTHERDATA, sessionId);

	}
	
	public static void setAppSessionId(FabricRequestManager fabricRequestManager, JsonObject customParams) {
		String authkey = fabricRequestManager.getHeadersHandler().getHeader(URLConstants.XKONYAUTHORIZATION);
		String appsessionid = HelperMethods.getParamFromIToken(authkey, URLConstants.SESSIONID);
		if (StringUtils.isNotBlank(appsessionid)) {
			customParams.addProperty(URLConstants.APPSESSIONID.toString(), appsessionid);
		}

	}


}
