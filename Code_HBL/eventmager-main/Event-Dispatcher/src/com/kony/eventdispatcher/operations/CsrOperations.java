package com.kony.eventdispatcher.operations;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.kony.utils.HelperMethods;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class CsrOperations {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	public static void getCSRrelatedParams(FabricRequestManager fabricRequestManager, JsonObject otherData) {
		try {
			if (fabricRequestManager.getServicesManager().getIdentityHandler().getUserAttributes()
					.get(URLConstants.CSRUSERNAME) != null) {
				otherData.addProperty(URLConstants.CSRUSERNAME, (String) fabricRequestManager.getServicesManager()
						.getIdentityHandler().getUserAttributes().get(URLConstants.CSRUSERNAME));
			}

			if (fabricRequestManager.getServicesManager().getIdentityHandler().getUserAttributes()
					.get(URLConstants.CSRUSERID) != null) {
				otherData.addProperty(URLConstants.CSRUSERID, (String) fabricRequestManager.getServicesManager()
						.getIdentityHandler().getUserAttributes().get(URLConstants.CSRUSERID));
			}

			if (fabricRequestManager.getServicesManager().getIdentityHandler().getUserAttributes()
					.get(URLConstants.CSRROLE) != null) {
				otherData.addProperty(URLConstants.CSRROLE, (String) fabricRequestManager.getServicesManager()
						.getIdentityHandler().getUserAttributes().get(URLConstants.CSRROLE));
			}

			if (fabricRequestManager.getServicesManager().getIdentityHandler().getUserAttributes()
					.get(URLConstants.CSRNAME) != null) {
				otherData.addProperty(URLConstants.CSRNAME, (String) fabricRequestManager.getServicesManager()
						.getIdentityHandler().getUserAttributes().get(URLConstants.CSRNAME));
			}
		} catch (Exception e) {
			alert.prepareError("Error while fetching csr related params").log();
		}
	}
	
	public static void getCSRrelatedParams(JsonObject customParams, JsonObject otherData) {
		try {
			if (customParams.has(URLConstants.CSRUSERNAME))
				otherData.addProperty(URLConstants.CSRUSERNAME,
					HelperMethods.getStringFromJsonObject(customParams, URLConstants.CSRUSERNAME, true));

			if (customParams.has(URLConstants.CSRUSER))
				otherData.addProperty(URLConstants.CSRUSER,
						HelperMethods.getStringFromJsonObject(customParams, URLConstants.CSRUSER, true));

			if (customParams.has(URLConstants.CSRROLE))
				otherData.addProperty(URLConstants.CSRROLE,
						HelperMethods.getStringFromJsonObject(customParams, URLConstants.CSRROLE, true));

			if (customParams.has(URLConstants.CSRNAME))
				otherData.addProperty(URLConstants.CSRNAME,
						HelperMethods.getStringFromJsonObject(customParams, URLConstants.CSRNAME, true));
		} catch (Exception e) {
			alert.prepareError("Error while fetching csr UserName").log();
		}

	}

}
