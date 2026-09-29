package com.kony.adminconsole.service.contract.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.contract.businessdelegate.api.ApprovalMatrixManageBusinessDelegate;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

/**
 * @author kaushik.mondal
 *
 */

public class ApprovalMatrixManageBusinessDelegateImpl implements ApprovalMatrixManageBusinessDelegate {

	@Override
	public JSONObject updateApprovalMatrixStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {

		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_UPDATEAPPROVALMATRIXSTATUS).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject isApprovalMatrixDisabled(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_ISAPPROVALMATRIXDISABLED).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	@Override
	public JSONObject getApprovalMatrix(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", dbpServicesClaimsToken);
        String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
                        .withOperationId(OperationName.OP_GETAPPROVALMATRIX).withRequestHeaders(headerMap)
                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

}
