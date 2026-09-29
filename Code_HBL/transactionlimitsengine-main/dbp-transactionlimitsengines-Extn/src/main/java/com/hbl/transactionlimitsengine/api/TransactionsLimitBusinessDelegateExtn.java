package com.hbl.transactionlimitsengine.api;

import com.dbp.transactionslimitengine.businessdelegate.api.TransactionsLimitBusinessDelegate;
import com.google.gson.JsonObject;

public interface TransactionsLimitBusinessDelegateExtn extends TransactionsLimitBusinessDelegate {
	public JsonObject getTransactionLimits(String featureactionid, String companyid, String transactionstartdate,
			String roleid, String customerid, String accountid, String limitGroup);
}
