package com.kony.model;

import java.util.Iterator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.memorymgmt.PayPersonManager;
import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class PayPersonHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private PayPersonHelper() {
    }

    public static SessionMap reloadPayPersonsIntoSession(FabricRequestManager fabricRequestManager) {
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null,
                    URLConstants.PAYPERSON_OS_GET);
            if (null != response && !response.isJsonNull()) {
                JsonArray payPersons = response.getAsJsonArray("Recipients");
                SessionMap payPersonMap = getPayPersonsMap(payPersons);
                PayPersonManager payPersonManager = new PayPersonManager(fabricRequestManager, null);
                payPersonManager.savePayPersonsIntoSession(payPersonMap);
                return payPersonMap;
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading payperson:", e).log();
        }
        return new SessionMap();
    }

    private static SessionMap getPayPersonsMap(JsonArray payPersons) {
        SessionMap payPersonMap = new SessionMap();
        if (null != payPersons && !payPersons.isJsonNull() && payPersons.size() > 0) {
            Iterator<JsonElement> itr = payPersons.iterator();
            while (itr.hasNext()) {
                payPersonMap.addKey(itr.next().getAsJsonObject().get("PayPersonId").getAsString());
            }
        }
        return payPersonMap;
    }

}
