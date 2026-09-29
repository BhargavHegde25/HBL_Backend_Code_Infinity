package com.kony.adminconsole.service.customer.backenddelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface InfinityCustomerBackendDelegate extends BackendDelegate {
	
	public JSONObject getConvertedAmount(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;

}
