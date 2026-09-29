package com.kony.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.memorymgmt.PayeeManager;
import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class PayeeHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private PayeeHelper() {
    }

    public static SessionMap reloadPayeesIntoSession(FabricRequestManager fabricRequestManager) {
        try {
            Map<String, Object> input = new HashMap<>();
            input.put("sortBy", "payeeNickName");
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, input, null,
                    URLConstants.PAYEES_OS_GET);
            if (null != response && !response.isJsonNull()) {
                JsonArray payees = response.getAsJsonArray("Payee");
                SessionMap payeesMap = getPayeesMap(payees);
                PayeeManager payeeManager = new PayeeManager(fabricRequestManager, null);
                payeeManager.savePayeesIntoSession(payeesMap);
                return payeesMap;
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading payees:", e).log();
        }
        return new SessionMap();
    }

    public static SessionMap reloadWireTransferPayeesIntoSession(FabricRequestManager fabricRequestManager) {
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null,
                    URLConstants.PAYEES_OS_GETWIRETRANSFERRECIPIENT);
            if (null != response && !response.isJsonNull()) {
                JsonArray payees = response.getAsJsonArray("Payee");
                SessionMap wirePayeesMap = getWirePayeesMap(payees);
                PayeeManager payeemanager = new PayeeManager(fabricRequestManager, null);
                payeemanager.saveWireTransferPayeesIntoSession(wirePayeesMap);
                return wirePayeesMap;
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading wire recipient payees:", e).log();
        }
        return new SessionMap();
    }

    private static SessionMap getPayeesMap(JsonArray payees) {
        SessionMap payeesMap = new SessionMap();
        if (null != payees && !payees.isJsonNull() && payees.size() > 0) {
            Iterator<JsonElement> itr = payees.iterator();
            while (itr.hasNext()) {
                payeesMap.addKey(itr.next().getAsJsonObject().get("payeeId").getAsString());
            }
        }
        return payeesMap;
    }

    private static SessionMap getWirePayeesMap(JsonArray payees) {
        SessionMap payeesMap = new SessionMap();
        if (null != payees && !payees.isJsonNull() && payees.size() > 0) {
            Iterator<JsonElement> itr = payees.iterator();
            while (itr.hasNext()) {
                JsonObject payee = itr.next().getAsJsonObject();
                payeesMap.addAttributeForKey(payee.get("payeeId").getAsString(), "wireAccountType",
                        payee.get("wireAccountType").getAsString());
                payeesMap.addAttributeForKey(payee.get("payeeId").getAsString(), "payeeAccountNumber",
                        payee.get("accountNumber").getAsString());
            }
        }
        return payeesMap;
    }

}
