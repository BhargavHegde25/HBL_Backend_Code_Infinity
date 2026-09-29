package com.temenos.infinity.api.transactionadviceapi.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormCookie;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormDownload;

public interface TransactionAdviceBusinessDelegate extends BusinessDelegate {

	AutoFormCookie login(String auth_token);

	String download(String documentId, String revision, String xsrf,String jsessionid,String auth_token);

	AutoFormDownload search(String customerId, String accountId, String transactionRef, String mediaType, String transactionType,
			String page,String xsrf, String jsessionid,String auth_token);
}
