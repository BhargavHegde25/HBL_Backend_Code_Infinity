package com.temenos.dbx.party.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.temenos.dbx.party.businessdelegate.api.AuditActivityPartyCustomerInformationUpdateBusinessDelegate;

public class AuditActivityPartyCustomerInformationUpdateBusinessDelegateImpl
		implements AuditActivityPartyCustomerInformationUpdateBusinessDelegate {

	private LoggerUtil logger;
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean updateAuditLogsWithCustomerInformation(Map<String, Object> inputParams,
			Map<String, Object> headersMap, String url) {

		logger = new LoggerUtil(AuditActivityPartyCustomerInformationUpdateBusinessDelegateImpl.class);

		String customerId = "";
		String coreCustomerId = "";
		if (inputParams.containsKey("id") && inputParams.get("id") != null
				&& StringUtils.isNotBlank(inputParams.get("id").toString()))
			customerId = inputParams.get("id").toString();
		if (inputParams.containsKey("coreCustomerID") && inputParams.get("coreCustomerID") != null
				&& StringUtils.isNotBlank(inputParams.get("coreCustomerID").toString()))
			coreCustomerId = inputParams.get("coreCustomerID").toString();

		Map<String, Object> input = new HashMap<>();
		input.put("_partyId", inputParams.get("partyID").toString());
		input.put("_customerId", customerId);
		input.put("_coreCustomerId", coreCustomerId);

		JsonObject response = new JsonObject();

		try {
			response = ServiceCallHelper.invokeServiceAndGetJson(input, headersMap, url);
		} catch (Exception e) {
			alert.prepareError("Exception occured while updating the party customer information : " + e.getMessage()).log();
			return false;
		}
		if (response.has("errmsg"))
			return false;
		return true;
	}

}
