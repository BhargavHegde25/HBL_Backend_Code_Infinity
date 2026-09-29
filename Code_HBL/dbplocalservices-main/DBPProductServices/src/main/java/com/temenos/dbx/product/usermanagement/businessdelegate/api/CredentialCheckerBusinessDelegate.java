package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface CredentialCheckerBusinessDelegate extends BusinessDelegate {

	public DBXResult update(CredentialCheckerDTO credentialCheckerDTO, Map<String, Object> headerMap);

	public DBXResult get(CredentialCheckerDTO credentialCheckerDTO, Map<String, Object> headerMap);

	public boolean delete(CredentialCheckerDTO credentialCheckerDTO, Map<String, Object> headerMap)
			throws ApplicationException;

	public CredentialCheckerDTO create(CredentialCheckerDTO credentialCheckerDTO, Map<String, Object> headerMap)
			throws ApplicationException;
}
