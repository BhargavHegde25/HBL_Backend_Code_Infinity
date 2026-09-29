package com.dbp.batchprocessengine.resource.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.batchprocessengine.businessdelegate.api.AccountsBusinessDelegate;
import com.dbp.batchprocessengine.businessdelegate.impl.AccountsBusinessDelegateImpl;
import com.dbp.batchprocessengine.resource.api.AccountsResource;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class AccountsResourceImpl implements AccountsResource {
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public Result getAccounts(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		String customerId = request.getParameter("customerId");
		String accountId = request.getParameter("accountId");
		String companyLegalUnit = request.getParameter("companyLegalUnit");
		String lastsynctime = request.getParameter("lastsynctime");
		String currenttimestamp = request.getParameter("currenttimestamp");
		LocalDateTime curtimestamp = null;
		LocalDateTime synctime = null;
		try {
			if (lastsynctime != null && !lastsynctime.equals("null") && !lastsynctime.equals(""))
				synctime = LocalDateTime.parse(lastsynctime, dtf);
			if (currenttimestamp != null && !currenttimestamp.equals("null") && !currenttimestamp.equals(""))
				curtimestamp = LocalDateTime.parse(currenttimestamp, dtf);

		} catch (Exception e) {
			diagnostic.prepareDebug("Error in parsing timestamp", e).log();
			return result;
		}
		JsonObject accounts;
		AccountsBusinessDelegate accountBusinessDelegate = new AccountsBusinessDelegateImpl();
		if (customerId != null && !customerId.equals("") && accountId != null && !accountId.equals("null")) {
			accounts = accountBusinessDelegate.getAccountDetails(customerId, accountId, synctime, curtimestamp,companyLegalUnit);
			result = JSONToResult.convert(accounts.toString());
		} else if (customerId != null && !customerId.equals("") && (accountId == null || accountId.equals("null"))) {
			accounts = accountBusinessDelegate.getAccountDetails(customerId, synctime, curtimestamp,companyLegalUnit);
			result = JSONToResult.convert(accounts.toString());
		} else {
			result.addParam(new Param("error", "Invalid input", "String"));
		}

		return result;
	}

}
