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
import com.kony.memorymgmt.SavingsPotManager;
import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class SavingsPotHelper {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	    private SavingsPotHelper() {
	    }

	    
	 public static void reloadSavingsPotsOfUserIntoSession(FabricRequestManager fabricRequestManager) {
	        try {
	        	
	            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null,
	                    URLConstants.SAVINGSPOT_GETALL);
	            diagnostic.prepareDebug("response in reloadSavingsPotsOfUserIntoSession: " + response.toString()).log();
	            saveSavingsPotsofUserIntoSession(response, fabricRequestManager);

	        } catch (Exception e) {
	            alert.prepareError("Error while reloading internal accounts:", e).log();
	        }
	    }
	 public static void saveSavingsPotsofUserIntoSession(JsonObject response,
	            FabricRequestManager fabricRequestManager) {
	        String savingsPot = "savingsPot";
	        diagnostic.prepareDebug("SavingsPot Helper: SavingsPot Response: " + response.toString()).log();
	        if (null != response && !response.isJsonNull()) {
	            if (response.has(savingsPot)) {
	                JsonArray savingsPots = response.getAsJsonArray(savingsPot);
	                SessionMap savingsPotsMap = getSavingsPotsMap(savingsPots);
	                diagnostic.prepareDebug("SessionMap of savingsPots: " + savingsPotsMap.toString()).log();
	                SavingsPotManager savingsPotManager = new SavingsPotManager(fabricRequestManager);
	                savingsPotManager.saveSavingsPotIntoSession(savingsPotsMap);
	            }
	        }
	 }
	 private static SessionMap getSavingsPotsMap(JsonArray savingsPots) {
	            SessionMap savingsPotMap = new SessionMap();
	            if (null != savingsPots && !savingsPots.isJsonNull() && savingsPots.size() > 0) {
	                Iterator<JsonElement> itr = savingsPots.iterator();
	                while (itr.hasNext()) {
	                    JsonElement ele = itr.next();
	                    savingsPotMap.addAttributeForKey(ele.getAsJsonObject().get("savingsPotId").getAsString(),"fundingAccountId",ele.getAsJsonObject().get("fundingAccountId").getAsString());
	                    savingsPotMap.addAttributeForKey(ele.getAsJsonObject().get("savingsPotId").getAsString(),"availableBalance",ele.getAsJsonObject().get("availableBalance").getAsString());
	                    savingsPotMap.addAttributeForKey(ele.getAsJsonObject().get("savingsPotId").getAsString(),"status",ele.getAsJsonObject().get("status").getAsString());
	                    savingsPotMap.addAttributeForKey(ele.getAsJsonObject().get("savingsPotId").getAsString(),"fundingAccountHoldingsId",ele.getAsJsonObject().get("fundingAccountHoldingsId").getAsString());
	                    savingsPotMap.addAttributeForKey(ele.getAsJsonObject().get("savingsPotId").getAsString(),"potType",ele.getAsJsonObject().get("potType").getAsString());      
	                }
	            }
	            return savingsPotMap;
	        }        

}
