package com.temenos.infinity.api.cards.preprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.object.task.ObjectProcessorTaskManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.scaintegration.helper.Helper;
import com.temenos.infinity.api.cards.datavalidation.CardValidationTask;
import com.temenos.infinity.api.cards.utils.CardsManager;
import com.temenos.infinity.api.cards.utils.HelperMethods;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

public class CardStatementsPreprocessor implements ObjectServicePreProcessor{
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
			FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
		String cardId = HelperMethods.getStringFromJsonObject(requestPayload, "Card_id");
		diagnostic.prepareDebug("validating cardId {}",cardId).log();
		CardsManager cardManager = new CardsManager(fabricRequestManager, fabricResponseManager);
		if (!cardManager.validateCard(null, cardId)) {
			JsonObject resPayload = null;
			if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
				resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
			}
			resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
		}
		else
			fabricRequestChain.execute();
	}
	
}
