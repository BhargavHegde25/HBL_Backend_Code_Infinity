package com.temenos.dbx.object.businessdelegate.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kony.dbputilities.mfa.PostLoginMFAUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.dbx.actions.businessdelegate.api.AccountActionBusinessDelegate;
import com.temenos.dbx.constants.ActionConstant;

public class BulkBillPayTransactionActionValidationBDImpl implements ObjectProcessorTask {
	private final LoggerUtil logger = new LoggerUtil(BulkBillPayTransactionActionValidationBDImpl.class);
	private static final String FRM_ACCOUNT_NUMBER_FIELD = "fromAccountNumber";
	private static final String BULK_BILL_PAY_PARAM = "bulkPayString";
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
		if (!HelperMethods.isJsonEleNull(reqPayloadJEle)) {
			JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
			if (HelperMethods.isMFAVerify(requestPayload)) {
				return true;
			}
			ServicesManager sm = fabricRequestManager.getServicesManager();
			String appendedString = HelperMethods.getOperationString(sm);
			diagnostic.prepareDebug("appendedString {}"+appendedString).log();
			PostLoginMFAUtil postLoginMFAUtil = new PostLoginMFAUtil(fabricRequestManager, "");
			String serviceName = postLoginMFAUtil.getValidServiceName(requestPayload, appendedString.toLowerCase());
			AccountActionBusinessDelegate accountActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AccountActionBusinessDelegate.class);
			
			String userId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
			String payeeLi = HelperMethods.getStringFromJsonObject(requestPayload, BULK_BILL_PAY_PARAM);
			List<Map<String, String>> payees = getPayees(payeeLi);
			for (Map<String, String> payee : payees) {
				diagnostic.prepareDebug("from account number "+payee.get(FRM_ACCOUNT_NUMBER_FIELD)+", service name "+serviceName).log();
				if (!accountActionBusinessDelegate.hasUserAccountFeatureAction(userId, payee.get(FRM_ACCOUNT_NUMBER_FIELD),
						ActionConstant.valueOf(serviceName)) || 
						!accountActionBusinessDelegate.hasUserAccountFeatureAction(userId, payee.get(FRM_ACCOUNT_NUMBER_FIELD),
								ActionConstant.BILL_PAY_CREATE)) {
					JsonObject resPayload = null;
					if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
						resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
					}
					resPayload = ErrorCodeEnum.ERR_12007.setErrorCode(resPayload);
					fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
					return false;
				}
			}
		}
		return true;
	}
	
	public List<Map<String, String>> getPayees(String payeeLi) {
		List<Map<String, String>> inputs = new ArrayList<>();
		JsonArray jArray = getJsonArray(payeeLi);
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Gson gson = new Gson();
		if (jArray.isJsonArray()) {
			for (int i = 0; i < jArray.size(); i++) {
				Map<String, String> temp = gson.fromJson(jArray.get(i), type);
				inputs.add(temp);
			}
		}
		return inputs;
	}

	public JsonArray getJsonArray(String jsonString) {
		JsonParser jsonParser = new JsonParser();
		return (JsonArray) jsonParser.parse(jsonString);
	}

}
