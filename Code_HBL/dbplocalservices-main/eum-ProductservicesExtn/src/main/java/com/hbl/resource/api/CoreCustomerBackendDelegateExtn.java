package com.hbl.resource.api;

import java.util.Map;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.product.dto.DBXResult;

public interface CoreCustomerBackendDelegateExtn extends CoreCustomerBackendDelegate {
	public DBXResult searchHBLCoreCustomers(Map<String, Object> payload, Map<String, Object> headersMap)
			throws ApplicationException;

}
