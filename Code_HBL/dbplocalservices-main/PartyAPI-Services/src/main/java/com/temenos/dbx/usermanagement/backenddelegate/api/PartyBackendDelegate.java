package com.temenos.dbx.usermanagement.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.temenos.dbx.product.dto.DBXResult;

public interface PartyBackendDelegate extends BackendDelegate {
		public DBXResult getReferenceByID(String referenceByID, Map<String, Object> headerMap);
		public void getUpdateAddressType(Map<String, String> input, Map<String, Object> headerMap);
}
