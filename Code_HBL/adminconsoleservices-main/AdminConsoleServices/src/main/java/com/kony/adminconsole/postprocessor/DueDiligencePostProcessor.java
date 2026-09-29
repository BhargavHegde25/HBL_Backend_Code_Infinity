package com.kony.adminconsole.postprocessor;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class DueDiligencePostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		try {
			String customerId = StringUtils.EMPTY;
			boolean status = false;
			JSONObject resultJsonObject = new JSONObject(ResultToJSON.convert(result));
			String serviceType = request.getParameter("current_serviceID").toLowerCase();
			if (resultJsonObject.has("success") && StringUtils.isNotBlank(resultJsonObject.getString("success")))
				status = true;
			else if (resultJsonObject.has("status") && StringUtils.isNotBlank(resultJsonObject.getString("status")))
				status = true;
			else if (resultJsonObject.has(ACConstants.DBP_ERROR_MESSAGE)
					&& StringUtils.isNotBlank(resultJsonObject.getString(ACConstants.DBP_ERROR_MESSAGE)))
				status = false;
			if (resultJsonObject.has("id")) {
				customerId = resultJsonObject.getString("id");
			} else if (StringUtils.isBlank(customerId)) {
				customerId = request.getParameter("id");
			}
			if (serviceType.contains("create"))
				auditActivity(customerId, status, EventEnum.CREATE, serviceType, request);
			else if (serviceType.contains("update"))
				auditActivity(customerId, status, EventEnum.UPDATE, serviceType, request);

		} catch (Exception e) {
			alert.prepareError("Error occured in Due Diligence PostProcessor" + e).log();
		}
		return result;
	}

	/**
	 * Method to audit the Activity Information
	 * 
	 * @param customerId
	 * @param loggedInUser
	 * @param status
	 * @param requestInstance
	 */
	private void auditActivity(String customerId, boolean status, EventEnum eventType, String service,
			DataControllerRequest requestInstance) {
		if (status == true) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, eventType,
					ActivityStatusEnum.SUCCESSFUL,
					"Due Diligenece Updated. Customer id: " + customerId + " for Service " + service);
		} else {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, eventType,
					ActivityStatusEnum.FAILED,
					"Failed in Due Diligenece. Customer id: " + customerId + " for Service " + service);
		}
	}

}
