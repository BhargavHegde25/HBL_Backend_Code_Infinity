package com.temenos.infinity.api.arrangements.postprocessors;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.temenos.infinity.api.arrangements.utils.AccountHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class SaveAccountsInSessionTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		try {
			Log4j2Configurator.getInstance();
			JsonObject response = (JsonObject) fabricResponseManager.getPayloadHandler().getPayloadAsJson();
			JsonObject request = (JsonObject) fabricRequestManager.getPayloadHandler().getPayloadAsJson();
			//suppressing save in session for single core customer flow
			if (request!=null && request.has("Membership_id") && StringUtils.isNotBlank(request.get("Membership_id").getAsString()))
				return true;
			AccountHelper.saveInternalBankAccountsIntoSession(response, fabricRequestManager);
		} catch (Exception e) {
			alert.prepareError("Exception while caching accounts in session", e).log();
		}
		return true;
	}

}
