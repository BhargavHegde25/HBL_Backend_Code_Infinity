package com.temenos.dbx.eum.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PasswordHistoryDTO;

public interface PasswordHistoryBusinessDelegate extends BusinessDelegate {

    public DBXResult update(PasswordHistoryDTO passwordHistoryDTO, Map<String, Object> headerMap);

	public DBXResult get(PasswordHistoryDTO passwordHistoryDTO, Map<String, Object> headerMap);
	
	public DBXResult getPasswordLockoutSetting(Map<String, Object> headerMap);
	
}
