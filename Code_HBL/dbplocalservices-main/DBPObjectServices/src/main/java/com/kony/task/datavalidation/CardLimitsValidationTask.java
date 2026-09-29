package com.kony.task.datavalidation;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.CardsManager;
import com.kony.memorymgmt.SessionMap;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;
import com.kony.cards.config.ManageCardsApiAPIServices;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;

public class CardLimitsValidationTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		if(!HelperMethods.isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
    	JsonObject resPayload = null;
    	resPayload = ErrorCodeEnum.ERR_10152.setErrorCode(resPayload);
		String userId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			
			String	cardId = HelperMethods.getStringFromJsonObject(requestPayload, "cardId");
			String	Action =HelperMethods.getStringFromJsonObject(requestPayload, "Action");
			String	withdrawlLimit = HelperMethods.getStringFromJsonObject(requestPayload, "withdrawlLimit");
			String	purchaseLimit = HelperMethods.getStringFromJsonObject(requestPayload, "purchaseLimit");
			if(StringUtils.isNotBlank(cardId)&&StringUtils.isNotBlank(Action))
			{
				double limit = 0.00;
				double maxLimit = 0.00;
				double minLimit = 0.00;
				
				JsonObject jsonObjectCard = getCardDetailsFromDB(cardId);
				JsonArray cards = jsonObjectCard.getAsJsonArray("card");
				JsonObject jsonCard = cards.get(0).getAsJsonObject();
			
				String strMaxLimit = "";
				String strMinLimit = "";
				
				if("updateWithdrawalLimit".equals(Action))
				{
					if (!StringUtils.isNotBlank(withdrawlLimit)) {
						resPayload = ErrorCodeEnum.ERR_10419.setErrorCode(resPayload);
						fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
						return false;
					} else {
						limit = Double.parseDouble(withdrawlLimit);
					}
					strMaxLimit = HelperMethods.getStringFromJsonObject(jsonCard, "withdrawalMaxLimit");
					strMinLimit = HelperMethods.getStringFromJsonObject(jsonCard, "withdrawalMinLimit");
				}
				else if("updatePurchaseLimit".equals(Action))
				{
					if (!StringUtils.isNotBlank(purchaseLimit)) {
						resPayload = ErrorCodeEnum.ERR_10418.setErrorCode(resPayload);
						fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
						return false;
					} else {
						limit = Double.parseDouble(purchaseLimit);
					}
					strMaxLimit = HelperMethods.getStringFromJsonObject(jsonCard, "purchaseMaxLimit");
					strMinLimit = HelperMethods.getStringFromJsonObject(jsonCard, "purchaseMinLimit");
				}
				if (StringUtils.isNotBlank(strMaxLimit)) {
					maxLimit = Double.parseDouble(strMaxLimit);
					if (limit > maxLimit) {
						resPayload = ErrorCodeEnum.ERR_10416.setErrorCode(resPayload);
						fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
						return false;
					}
				}
				if (StringUtils.isNotBlank(strMinLimit)) {
					minLimit = Double.parseDouble(strMinLimit);
					if (limit < minLimit) {
						resPayload = ErrorCodeEnum.ERR_10417.setErrorCode(resPayload);
						fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
						return false;
					}
				}
				CardsManager cardManager = new CardsManager(fabricRequestManager, fabricResponseManager);
				SessionMap cardsMap = cardManager.getCardsFromSession(userId);
		        if ((null == cardsMap)||(cardsMap.hasKey(cardId)==false)) {
					fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
		            return false;
		        }
		        
		        return cardsMap.hasKey(cardId);
			}
		
		}
		fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);	
		return false;
	}
	 
	private static JsonObject getCardDetailsFromDB(String cardId) {

		JsonObject serviceResponse = null;
		Map<String, Object> inputMap = new HashMap<>();
		StringBuffer queryString = new StringBuffer();
		queryString.append("Id" + " ");
		queryString.append("eq" + " ");
		queryString.append(cardId);

		inputMap.put("$filter", queryString.toString());
		String Response = null;
		try {
			Response = Executor.invokeService(ManageCardsApiAPIServices.GETCARDDETAILS, inputMap, null);
			diagnostic.prepareDebug("Card Details" + Response).log();
		} catch (Exception e1) {
			alert.prepareError("Service call to dbxdb failed " + e1.toString()).log();
		}
		JsonParser jsonParser = new JsonParser();
		JSONObject serviceResponseJSON = Utilities.convertStringToJSON(Response);
		if (serviceResponseJSON == null) {
			alert.prepareError("EmptyResponse no card details available for cardId").log();
		} else {
	    	serviceResponse = (JsonObject)jsonParser.parse(serviceResponseJSON.toString());
		}
		return serviceResponse;

	}	
}
