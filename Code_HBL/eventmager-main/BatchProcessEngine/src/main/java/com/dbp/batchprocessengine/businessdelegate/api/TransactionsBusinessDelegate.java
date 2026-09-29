package com.dbp.batchprocessengine.businessdelegate.api;

import java.time.LocalDateTime;

import com.google.gson.JsonObject;

public interface TransactionsBusinessDelegate {

	JsonObject getAllTransactions(String coreCustomerId, String accountId, LocalDateTime lastsynctime,
			LocalDateTime curtime,String companyLegalUnit);

	JsonObject getAllTransactions(String coreCustomerId, LocalDateTime lastsynctime, LocalDateTime curtime,String companyLegalUnit);

}
