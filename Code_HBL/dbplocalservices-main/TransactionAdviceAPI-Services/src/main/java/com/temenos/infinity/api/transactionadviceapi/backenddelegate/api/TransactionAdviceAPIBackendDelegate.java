package com.temenos.infinity.api.transactionadviceapi.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormCookie;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormDownload;

public interface TransactionAdviceAPIBackendDelegate extends BackendDelegate {

    AutoFormCookie login(String auth_token);

    byte[] download(String documentId, String revision, String xsrf, String jsessionid, String auth_token);

    AutoFormDownload search(String cuk, String xsrf, String jsessionid, String auth_token) throws ApplicationException;

}
