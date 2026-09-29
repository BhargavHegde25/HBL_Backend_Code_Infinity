package com.temenos.infinity.api.arrangements.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.api.arrangements.memorymgmt.AccountsManager;
import com.temenos.infinity.api.arrangements.memorymgmt.SessionMap;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;

public class AccountNumberValidationTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
						
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		//String accountNumberInQP = fabricRequestManager.getQueryParamsHandler().getParameter("accountNumber");
		String accountNumber = null;
		String customerId = null;
		if (!CommonUtils.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			accountNumber = CommonUtils.getStringFromJsonObject(requestPayload, "accountNumber");
			customerId = CommonUtils.getStringFromJsonObject(requestPayload, "customerid");
		}
		JsonObject resPayload = null;
		Properties accountprops = ArrangementsUtils.loadProps("accounttype");
		if (StringUtils.isNotBlank(accountNumber) ) {
			AccountsManager accountManager = new AccountsManager(fabricRequestManager, fabricResponseManager);
			SessionMap internalAccountsMap = accountManager.getInternalBankAccountsFromSession(customerId);
			if (null == internalAccountsMap || internalAccountsMap.isEmpty()) {
				resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
				fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
				return false;
	        }
			if (internalAccountsMap.hasKey(accountNumber)) {
				Map<String, String> account = internalAccountsMap.getValue(accountNumber);
				String accountType = account.get("accountType");
					if(!accountType.equalsIgnoreCase(accountprops.getProperty("SAVINGS.ACCOUNT"))
							&& !accountType.equalsIgnoreCase(accountprops.getProperty("CURRENT.ACCOUNT")) ) {
						resPayload = ErrorCodeEnum.ERR_29058.setErrorCode(resPayload);
						fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
						return false;
				}
				
			
				if (!CommonUtils.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
					resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
				}
			}
		
			else {
				resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
				fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
				return false;
			}
	}
		
			
	
		return true;
	
	}

}

