package com.hbl.adminconsoleextn.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.impl.ServiceDefinitionBusinessDelegateImpl;
import com.kony.adminconsole.service.servicedefinition.dto.ActionLimitDTO;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class ServiceDefinitionBusinessDelegateImplExtn extends ServiceDefinitionBusinessDelegateImpl{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	@Override
	public List<ActionLimitDTO> getMinTransactionLimits() {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_ACTIONLIMIT_GET;
       
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "LimitType_id eq MIN_TRANSACTION_LIMIT or  LimitType_id eq MB_MIN_TRANSACTION_LIMIT";
		requestParameters.put("$filter", filter);
		List<ActionLimitDTO> actionDTOs= null;
		
		String serviceDefinitionActionLimitResponse = null;
		JSONArray jsonArray = null;
		try {
			serviceDefinitionActionLimitResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionActionLimitResponse);
		    jsonArray = responseObj.optJSONArray("actionlimit");
		    actionDTOs = JSONUtils.parseAsList(jsonArray.toString(), ActionLimitDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch min action limits from actionlimit table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getactionlimit: " + e).log();
			return null;
		}
		
		return actionDTOs;
	}

}
