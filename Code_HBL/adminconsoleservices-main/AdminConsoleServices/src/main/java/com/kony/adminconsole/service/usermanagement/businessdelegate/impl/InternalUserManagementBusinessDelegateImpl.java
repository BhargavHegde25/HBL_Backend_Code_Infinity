package com.kony.adminconsole.service.usermanagement.businessdelegate.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.InternalUserManagementBusinessDelegate;

import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class InternalUserManagementBusinessDelegateImpl implements InternalUserManagementBusinessDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	
	@Override
	public JSONObject createInternalUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
//		// TODO Auto-generated method stub
//		String serviceName = ServiceId.CRUDLAYER;
//        String operationName = OperationName.DB_LIMITGROUP_VIEW;
//       
//		String limitgroupResponse = null;
//		List<LimitGroupDTO> limitGroupDTOs = null;
//		
//		try {
//			limitgroupResponse = DBPServiceExecutorBuilder.builder().
//					withServiceId(serviceName).
//					withObjectId(null).
//					withOperationId(operationName).
//					build().getResponse();
//			JSONObject responseObj = new JSONObject(limitgroupResponse);
//		    JSONArray jsonArray = responseObj.optJSONArray("limitgroups_view");
//		    limitGroupDTOs = JSONUtils.parseAsList(jsonArray.toString(), LimitGroupDTO.class);
//		}
//		catch (JSONException e) {
//			alert.prepareError("Failed to fetch limit group from table: " + e).log();
//			return null;
//		}
//		catch (Exception e) {
//			alert.prepareError("Caught exception at fetchAllLimitGroups: " + e).log();
//			return null;
//		}
//		
//		return (JSONObject) limitGroupDTOs;
		return null;
	}

	@Override
	public JSONObject editInternalUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getInternalUser(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public JSONObject downloadUsersList(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject updateUserStatus(Map<String, Object> postParametersMap, String dbpServicesClaimsToken)
			throws DBPApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
}
