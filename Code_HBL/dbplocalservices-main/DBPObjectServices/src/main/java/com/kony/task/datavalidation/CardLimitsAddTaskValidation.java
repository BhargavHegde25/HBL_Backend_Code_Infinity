package com.kony.task.datavalidation;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.memorymgmt.CardsManager;
import com.kony.utilities.Constants;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class CardLimitsAddTaskValidation implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
if (!HelperMethods.isDACEnabled()) {
	diagnostic.prepareDebug("data access control is disabled").log();
	return true;
}
JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
	JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
	String requestBody=requestPayload.get("requestBody").getAsString();
	JSONObject jsonObject = new JSONObject(requestBody);
	String action=jsonObject.get("Action").toString();
	String updateCardSubType="";
//	String type = URLFinder.getPropertyValue("activateCardType", "objectservice.properties");
	//String subtype = URLFinder.getPropertyValue("activateCardSubType", "objectservice.properties");
	if("updateWithdrawalLimit".equals(action))
	{
		updateCardSubType=Constants.DailyCashLimit;
	}
	else if("updatePurchaseLimit".equals(action))
	{
		updateCardSubType=Constants.DailyPurchaseLimit;
	}
	requestPayload.addProperty("type", Constants.DomesticUsageLimits);
	requestPayload.addProperty("subtype", updateCardSubType);
	fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
}
return true;
}
}
