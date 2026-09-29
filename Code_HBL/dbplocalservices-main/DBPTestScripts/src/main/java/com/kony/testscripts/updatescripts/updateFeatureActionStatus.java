package com.kony.testscripts.updatescripts;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class updateFeatureActionStatus implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		String actionId = request.getParameter("actionId");

		String filter = "Action_id" + DBPUtilitiesConstants.EQUAL + getServiceName(actionId);
		Result get = HelperMethods.callGetApi(request, filter, HelperMethods.getHeaders(request), URLConstants.MFA_GET);
		if (HelperMethods.hasRecords(get)) {
			String id = HelperMethods.getFieldValue(get, "id");
			Map<String, String> inputParams = new HashMap<>();
			inputParams.put("id", id);
			inputParams.put("Status_id", "SID_INACTIVE");
			inputParams.put("App_id", "RETAIL_AND_BUSINESS_BANKING");
			inputParams.put("Action_id", getServiceName(actionId));
			HelperMethods.callApi(request, inputParams, HelperMethods.getHeaders(request), URLConstants.MFA_UPDATE);
			result.addStringParam("success", "success");
		}
		return result;
	}

	private String getServiceName(String operationName) {
		String serviceName = "";
		if (StringUtils.isNotBlank(operationName)) {
			switch (operationName) {
			case "lockCard":
				serviceName = "CARD_MANAGEMENT_LOCK_CARD";
				break;
			case "unlockCard":
				serviceName = "CARD_MANAGEMENT_UNLOCK_CARD";
				break;
			case "replaceCard":
				serviceName = "CARD_MANAGEMENT_REPLACE_CARD";
				break;
			case "reportLost":
				serviceName = "CARD_MANAGEMENT_REPORT_CARD_STOLEN";
				break;
			case "changePIN":
				serviceName = "CARD_MANAGEMENT_CHANGE_PIN";
				break;
			case "cancelCard":
				serviceName = "CARD_MANAGEMENT_CANCEL_CARD";
				break;
			case "partialupdate":
				serviceName = "CARD_MANAGEMENT_PARTIAL_UPDATE";
				break;
			case "createCardRequest":
				serviceName = "CARD_MANAGEMENT_CREATE_CARD_REQUEST";
				break;
			case "activateCards":
				serviceName = "CARD_MANAGEMENT_ACTIVATE_CARD";

			}
		}
		return serviceName;
	}

}
