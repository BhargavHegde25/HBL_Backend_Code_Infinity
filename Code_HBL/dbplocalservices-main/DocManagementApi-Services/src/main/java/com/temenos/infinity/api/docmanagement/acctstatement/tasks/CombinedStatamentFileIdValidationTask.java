package com.temenos.infinity.api.docmanagement.acctstatement.tasks;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.dataobject.Result;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.dbx.product.constants.Constants;

public class CombinedStatamentFileIdValidationTask implements ObjectProcessorTask{
	

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		String userId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			String fileId = requestPayload.get("fileId").getAsString();
			Map<String, String> inputMap = new HashMap<>();
			inputMap.put(Constants.$FILTER, "userId  eq '"+ userId + "' and "+ "id  eq '" + fileId + "'");
			Result templateDetails = HelperMethods.callApi(fabricRequestManager, inputMap,
		                HelperMethods.getHeaders(fabricRequestManager), URLConstants.ACCOUNTS_STATEMENT_FILES_GET);
			if (!HelperMethods.hasRecords(templateDetails)) {
				JsonObject resPayload = null;
				if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
					resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
				}
				resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
				fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
				return false;
			}	
		}
		return true;
	}

}
