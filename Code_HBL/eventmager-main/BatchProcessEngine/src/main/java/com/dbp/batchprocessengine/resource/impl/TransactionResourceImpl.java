package com.dbp.batchprocessengine.resource.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.batchprocessengine.businessdelegate.api.TransactionsBusinessDelegate;
import com.dbp.batchprocessengine.businessdelegate.impl.TransactionsBusinessDelegateImpl;
import com.dbp.batchprocessengine.resource.api.TransactionResource;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class TransactionResourceImpl implements TransactionResource {
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public Result getTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
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
		TransactionsBusinessDelegate transactionBusinessDelegate = new TransactionsBusinessDelegateImpl();
		if (customerId != null && !customerId.equals("") && accountId != null && !accountId.equals("null")) {

			JsonObject transactions = transactionBusinessDelegate.getAllTransactions(customerId, accountId, synctime,
					curtimestamp,companyLegalUnit);
			result = JSONToResult.convert(transactions.toString());
		} else if (customerId != null && !customerId.equals("") && (accountId == null || accountId.equals("null"))) {

			JsonObject transactions = transactionBusinessDelegate.getAllTransactions(customerId, synctime,
					curtimestamp,companyLegalUnit);
			result = JSONToResult.convert(transactions.toString());
		} else {
			result.addParam(new Param("error", "Invalid input", "String"));
		}
		return result;

	}

}