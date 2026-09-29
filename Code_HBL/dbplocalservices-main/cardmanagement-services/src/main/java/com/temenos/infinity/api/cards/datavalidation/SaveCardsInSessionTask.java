package com.temenos.infinity.api.cards.datavalidation;

import java.util.Iterator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.temenos.infinity.api.cards.utils.CardsManager;
import com.temenos.infinity.api.cards.utils.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class SaveCardsInSessionTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
			if (null != response && !response.isJsonNull()) {
				JsonArray cards = response.getAsJsonArray("records");
				SessionMap cardsMap = getCardsMap(cards);
				SessionMap cardsMapWithCardType = getCardsMapWithCardType(cards);
				CardsManager cardManager = new CardsManager(fabricRequestManager, fabricResponseManager);
				cardManager.saveCardsIntoSession(cardsMap);
				cardManager.saveCardsIntoSessionWithType(cardsMapWithCardType);
			}
		} catch (Exception e) {
			alert.prepareError("Exception while caching accounts in session", e).log();
		}
		return true;
	}

	private SessionMap getCardsMap(JsonArray cards) {
		SessionMap cardsMap = new SessionMap();
		if (null != cards && !cards.isJsonNull() && cards.size() > 0) {
			Iterator<JsonElement> itr = cards.iterator();
			while (itr.hasNext()) {
				cardsMap.addKey(itr.next().getAsJsonObject().get("cardId").getAsString());
						}
		}
		return cardsMap;
	}
	
	private SessionMap getCardsMapWithCardType(JsonArray cards) {
		SessionMap cardsMap = new SessionMap();
		if (null != cards && !cards.isJsonNull() && cards.size() > 0) {
			Iterator<JsonElement> itr = cards.iterator();
			while (itr.hasNext()) {
				JsonObject card=itr.next().getAsJsonObject();
				cardsMap.addKey(card.get("cardId").getAsString()+"-"+card.get("cardType").getAsString());
						}
		}
		return cardsMap;
	}
	
	
}
