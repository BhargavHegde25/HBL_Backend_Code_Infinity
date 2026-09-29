package com.dbp.transactionslimitengine.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;

public interface TransactionsLimitBusinessDelegate extends BusinessDelegate {

	JsonObject getTransactionLimits(String featureactionid, String companyid, String date, String roleid,
			String customerid, String accountid, String limitGroup);
	

}
