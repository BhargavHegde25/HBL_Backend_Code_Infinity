package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.DBXResult;

public interface CustomerSecurityQuestionsBusinessDelegate extends BusinessDelegate {

    public DBXResult get(String customerId, Map<String, Object> headerMap)
            throws ApplicationException;

	public DBXResult getAreSecurityQuestionsConfigured(String customerId, Map<String, Object> headerMap);

}
