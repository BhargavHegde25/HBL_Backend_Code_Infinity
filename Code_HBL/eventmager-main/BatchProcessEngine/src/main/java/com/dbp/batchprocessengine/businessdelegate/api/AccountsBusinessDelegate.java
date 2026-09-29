package com.dbp.batchprocessengine.businessdelegate.api;

import java.time.LocalDateTime;

import com.google.gson.JsonObject;

public interface AccountsBusinessDelegate {
	JsonObject getAccountDetails(String coreCustomerId, LocalDateTime lastsynctime, LocalDateTime curtime,String companyLegalUnit);

	JsonObject getAccountDetails(String coreCustomerId, String accountId, LocalDateTime lastsynctime,
			LocalDateTime curtime,String companyLegalUnit);
}
