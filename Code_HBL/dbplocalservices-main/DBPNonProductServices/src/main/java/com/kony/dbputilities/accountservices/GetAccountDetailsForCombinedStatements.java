package com.kony.dbputilities.accountservices;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountDetailsForCombinedStatements implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		HashMap<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String accountId = inputParams.get("accountID");
		HashMap<String, Object> accountsParam = new HashMap<String, Object>();
		accountsParam.put("$filter", "Account_id eq " + accountId);
		Map<String, Object> accountHeaders = new HashMap<>();
		JsonObject getAccountsResult = ServiceCallHelper.invokeServiceAndGetJson(accountsParam, accountHeaders,
				URLConstants.ACCOUNT_GET);
		String accountName = "";
		String accountCurrencyCode = "";
		String nickName = "";
		if (getAccountsResult.has("accounts") && getAccountsResult.get("accounts") != null) {
			JsonArray accountArray = getAccountsResult.get("accounts").getAsJsonArray();
			if (accountArray.size() > 0) {
				JsonObject accountJsonObject = accountArray.get(0).getAsJsonObject();
				if (accountJsonObject.has("AccountName") && accountJsonObject.get("AccountName") != null) {
					accountName = accountJsonObject.get("AccountName").getAsString();
				}
				if (accountJsonObject.has("CurrencyCode") && accountJsonObject.get("CurrencyCode") != null) {
					accountCurrencyCode = accountJsonObject.get("CurrencyCode").getAsString();
				}
				if (accountJsonObject.has("NickName") && accountJsonObject.get("NickName") != null) {
					nickName = accountJsonObject.get("NickName").getAsString();
				}
			}
		}
		result.addParam("accountName", accountName);
		result.addParam("currencyCode", accountCurrencyCode);
		result.addParam("nickName", nickName);
		return result;
	}

}
