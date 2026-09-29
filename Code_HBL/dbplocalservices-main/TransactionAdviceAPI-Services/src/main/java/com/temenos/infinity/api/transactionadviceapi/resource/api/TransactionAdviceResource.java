package com.temenos.infinity.api.transactionadviceapi.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.dataobject.Result;

public interface TransactionAdviceResource extends Resource {

	Result loginAndDownload(String customerId, String accountId, String transactionRef, String mediaType, String transactionType, String page,String operation,String auth_token);


}
