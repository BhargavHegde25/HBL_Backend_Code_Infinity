package com.kony.adminconsole.service.customer.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface InfinityCustomerBusinessDelegate extends BusinessDelegate {
	
	public JSONObject getInfinityAccounts(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
}
