package com.temenos.infinity.api.cards.datavalidation;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
//import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
//import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.infinity.api.cards.dto.CardsDTO;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.infinity.api.cards.utils.CardsManager;
import com.temenos.infinity.api.cards.utils.SessionMap;
import com.temenos.infinity.api.cards.utils.HelperMethods;
import com.temenos.infinity.api.cards.constants.ServiceId;
import com.temenos.infinity.api.cards.constants.OperationName;
//import com.kony.dbputilities.util.HelperMethods;

import java.util.HashMap;
import java.util.Map;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateCardValidationTask  implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private String isTypeBusiness = "0";
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
			if(requestPayload.has("MFAAttributes"))
			{
				requestPayload.remove("MFAAttributes");
			}
			if(requestPayload.has("mfaState"))
			{
				requestPayload.remove("mfaState");
			}
			if(requestPayload.has("mfaMode"))
			{
				requestPayload.remove("mfaMode");
			}
			if(requestPayload.has("mfaType"))
			{
				requestPayload.remove("mfaType");
			}
			if(requestPayload.has("serviceKey"))
			{
				requestPayload.remove("serviceKey");
			}
			if(requestPayload.has("isMFARequired")) {
				requestPayload.remove("isMFARequired");
			}
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
			String	cardId = HelperMethods.getStringFromJsonObject(requestPayload, "cardId");
			String	Action =HelperMethods.getStringFromJsonObject(requestPayload, "Action");
			String	newPin = HelperMethods.getStringFromJsonObject(requestPayload, "newPin");
			String currentPin = HelperMethods.getStringFromJsonObject(requestPayload, "pinNumber");
			if(StringUtils.isNotBlank(cardId)&&StringUtils.isNotBlank(Action))
			{
				if(Action.equals("PinChange"))
				{
					if((StringUtils.isAllBlank(newPin)||newPin.length()!=4)||(!StringUtils.isNumeric(newPin)))
					{
				    resPayload = ErrorCodeEnum.ERR_10118.setErrorCode(resPayload);
					fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
					return false;
					}
				}
				if(Action.equals("PinChange")&& !isValidPin(userId,cardId,currentPin)) {
					resPayload = ErrorCodeEnum.ERR_10118.setErrorCode(resPayload);
					fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
					return false;
				}
				if(requestPayload.has("pinNumber"))
				{
					requestPayload.remove("pinNumber");
				}
			CardsManager cardManager = new CardsManager(fabricRequestManager, fabricResponseManager);
			 SessionMap cardsMap = cardManager.getCardsFromSession(userId);
			 SessionMap cardsMapWithType = cardManager.getCardsFromSessionWithType(userId);
		        if ((null == cardsMap)||(cardsMap.hasKey(cardId)==false)) {
					fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
		            return false;
		        }
		        if(cardsMap.hasKey(cardId))
		        {
		        	if(cardsMapWithType.hasKey(cardId+"-"+"Credit"))
		        	{
		        		HelperMethods.setCardType("Credit");
		        	}
		        	else if(cardsMapWithType.hasKey(cardId+"-"+"Debit"))
		        	{
		        		HelperMethods.setCardType("Debit");
		        	}
		        }
		        return cardsMap.hasKey(cardId);
			}
		
		}
		fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);	
		return false;
	}

	boolean isValidPin(String userId,String cardId,String currentPinNumber)
	{
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationNameForGet = OperationName.DB_CARDS_GET;
		String filtercurrentpin = "User_id" + " eq " + userId + " and " + "Id"
				+ " eq " + cardId +" and " + "pinNumber" + " eq " + currentPinNumber;
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("$filter", filtercurrentpin);
		String updateCards = "";
		try {
			updateCards = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationNameForGet, hashMap, null, "");
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the cards",e).log();
		}
		hashMap.clear();

		JSONObject jsonRs = new JSONObject(updateCards);
		JSONArray countJsonArray = jsonRs.getJSONArray("card");
		if(countJsonArray.length()>0)
			return true;
		else
			return false;
	}
	 
		
}
