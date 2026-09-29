package com.hbl.resource.api;

import java.util.Map;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface ProfileManagementBackendDelegateExtn extends ProfileManagementBackendDelegate{
	public DBXResult fetchHBLRetailCustomerDetails(Map<String, Object> payload, Map<String, Object> headersMap)
            throws ApplicationException;
}
