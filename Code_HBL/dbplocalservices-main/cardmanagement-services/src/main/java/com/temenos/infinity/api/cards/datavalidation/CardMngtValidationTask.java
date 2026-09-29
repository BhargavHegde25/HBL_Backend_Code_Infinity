package com.temenos.infinity.api.cards.datavalidation;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.infinity.api.cards.utils.CardsManager;
import com.temenos.infinity.api.cards.constants.Constants;
import com.temenos.infinity.api.cards.utils.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
public class CardMngtValidationTask implements ObjectProcessorTask {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		if (!HelperMethods.isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		CardsManager cardManager = new CardsManager(fabricRequestManager, fabricResponseManager);
		String cardType=HelperMethods.getCardType();
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			String requestBody=requestPayload.get("requestBody").getAsString();
			JSONObject jsonObject = new JSONObject(requestBody);
			String action=jsonObject.get("Action").toString();
			
		     
			
			String updateCardSubType="";
			if ("Activate".equals(action)) {
				updateCardSubType =Constants.UnlockCard;
	    	 } else if ("Deactivate".equals(action)) {
	    		 updateCardSubType = Constants.LockCard;
	        } else if ("Cancel".equals(action)) {
	        	updateCardSubType = Constants.CancelCreditCard;
	        } else if ("Report Lost".equals(action)) {
	        	updateCardSubType = Constants.ReportLost;
	        } else if ("Replace".equals(action)) {
	        	updateCardSubType =Constants.ReplaceCard;
	        } else if ("Lock".equals(action)) {
	        	updateCardSubType =Constants.UnlockCard;
	        } else if ("Cancel Request".equals(action)) {
	        	updateCardSubType =Constants.CancelCreditCard;
	        } else if ("Replace Request".equals(action)) {
	        	updateCardSubType =Constants.ReplaceCard;
	        } else if ("PinChange".equals(action)) {
	        	updateCardSubType =Constants.ChangePIN;
	        } 
			
			if(cardType=="Debit")
			{
				requestPayload.addProperty("type", Constants.ManageDebitCard);
			}
			else
			{
				requestPayload.addProperty("type", Constants.ManageCreditCard);
			}
			requestPayload.addProperty("subtype", updateCardSubType);
			
			fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
		}
		return true;
	}
}
