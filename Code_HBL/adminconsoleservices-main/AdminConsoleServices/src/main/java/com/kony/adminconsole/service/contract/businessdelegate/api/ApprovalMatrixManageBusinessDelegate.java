package com.kony.adminconsole.service.contract.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface ApprovalMatrixManageBusinessDelegate extends BusinessDelegate {
	
	public JSONObject updateApprovalMatrixStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
	
	public JSONObject isApprovalMatrixDisabled(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
	
	public JSONObject getApprovalMatrix(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
            throws DBPApplicationException;
}
