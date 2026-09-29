package com.kony.dbputilities.customerentitlement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class EntitlementsGetPermissions implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final String INPUT_CUSTOMER_ID = "customerId";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result processedResult = new Result();

		try {
			String customerId = requestInstance.getParameter(INPUT_CUSTOMER_ID);

			// Validate Service Inputs
			if (StringUtils.isBlank(customerId)) {
				ErrorCodeEnum.ERR_10688.setErrorCode(processedResult);
				return processedResult;
			}

			// Fetch customer entitlements
			String customerEntitlementsResponse = getCustomerEntitlements(customerId, requestInstance);
			JSONObject responseJSON = HelperMethods.getStringAsJSONObject(customerEntitlementsResponse);
			if (responseJSON == null || !responseJSON.has(DBPConstants.FABRIC_OPSTATUS_KEY)
					|| responseJSON.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0 || !responseJSON.has("records")) {
				alert.prepareError("Failed to fetch customer entitelements. Service Response" + customerEntitlementsResponse).log();
				ErrorCodeEnum.ERR_10692.setErrorCode(processedResult);
				return processedResult;
			}

			JSONArray customerEntitlements = responseJSON.getJSONArray("records");
			Dataset customerEntitlementsDataset = HelperMethods.constructDatasetFromJSONArray(customerEntitlements);
			customerEntitlementsDataset.setId("services");
			processedResult.addDataset(customerEntitlementsDataset);
			return processedResult;

		} catch (Exception e) {
			alert.prepareError("Exception occurred while processing customer entitlements service ", e).log();
			ErrorCodeEnum.ERR_10700.setErrorCode(processedResult);
		}
		return processedResult;
	}

	public static String getCustomerEntitlements(String customerId, DataControllerRequest requestInstance)
			throws HttpCallException {
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("_customerID", customerId);
		String readCustomerResponse = HelperMethods.callApiAndGetString(requestInstance, inputMap, null,
				URLConstants.CUSTOMER_ENTITLEMENTS_PROC);
		return readCustomerResponse;

	}

}
