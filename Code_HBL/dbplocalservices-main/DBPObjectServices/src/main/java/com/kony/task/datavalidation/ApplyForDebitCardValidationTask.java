package com.kony.task.datavalidation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.AccountsManager;
import com.kony.memorymgmt.CardsManager;
import com.kony.memorymgmt.SessionMap;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.HeadersHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class ApplyForDebitCardValidationTask implements ObjectProcessorTask {
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
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(requestPayload);
			String	pinNumber = HelperMethods.getStringFromJsonObject(requestPayload, "pinNumber");
			String	accountId = HelperMethods.getStringFromJsonObject(requestPayload, "accountId");
			String	cardProductName = HelperMethods.getStringFromJsonObject(requestPayload, "cardProductName");
			String	withdrawlLimit = HelperMethods.getStringFromJsonObject(requestPayload, "withdrawlLimit");
			String	purchaseLimit = HelperMethods.getStringFromJsonObject(requestPayload, "purchaseLimit");
			String	cardHolderName = HelperMethods.getStringFromJsonObject(requestPayload, "cardHolderName");
			String	currentBalance = HelperMethods.getStringFromJsonObject(requestPayload, "currentBalance");
			String	availableBalance = HelperMethods.getStringFromJsonObject(requestPayload, "availableBalance");
			String	billingAddress = HelperMethods.getStringFromJsonObject(requestPayload, "billingAddress");
			String AccountType = HelperMethods.getStringFromJsonObject(requestPayload, "AccountType");
			if(StringUtils.isNotBlank(pinNumber)&&StringUtils.isNotBlank(accountId)&&StringUtils.isNotBlank(cardProductName)&&StringUtils.isNotBlank(withdrawlLimit)&&StringUtils.isNotBlank(purchaseLimit)&&StringUtils.isNotBlank(cardHolderName)&&StringUtils.isNotBlank(currentBalance)&&StringUtils.isNotBlank(availableBalance)&&StringUtils.isNotBlank(billingAddress))
			{
				AccountsManager accountManager = new AccountsManager(fabricRequestManager, fabricResponseManager);
				 SessionMap accountMap = accountManager.getInternalBankAccountsFromSession(userId);
			        if ((null == accountMap)||(accountMap.hasKey(accountId)==false)) {
						fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
			            return false;
			        }
			        if(accountMap.hasKey(accountId)==true)
			        {
			   Map<String, String> accdata=  accountMap.getValue(accountId);
			   AccountType=accdata.get("accountType");
			   if(!("Savings".equals(AccountType)||("Checking".equals(AccountType))))
				{
				   fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);	
					return false;
				}
			        }
		return true;
			
			}
		}
		fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);	
		return false;
	}
	 
		
}